from django.contrib.auth.decorators import login_required
from django.http import HttpResponseRedirect, JsonResponse
from django.shortcuts import render
from django.template.loader import render_to_string
from django.urls import reverse_lazy
from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_exempt
from django.views.generic import FormView

import apps.Util_apps.Util_braceletBLE as Utilities
from apps.baliza.models import Bracelet, HistorialBraceletSensors, Baliza, UsuarioRol, RolUsuario, HistorialRSSI
import authentication.Config.PREFERENCES as Preferences

# Create your views here.
from apps.baliza.views.server.ProcessSensorsData import ValidarExisteBaliza, ValidarExisteBracelet, ExtractMac, \
    ValidarCaida, ValidadProximidad, ValidarTemperatura, ValidarPPM, DeterminarIgualdad_o_cercano, ValidarNivelBateria, \
    ProcesarUbicacion
from apps.baliza.views.server.forms import PackBraceletForm


@method_decorator(csrf_exempt, name='dispatch')
class ServerReceivedCreateView(FormView):
    form_class = PackBraceletForm
    template_name = 'FORM.html'
    success_url = reverse_lazy('project:form_received_baliza_ok')

    def post(self, request, *args, **kwargs):
        data = dict()
        try:
            key = request.POST['key']
            if key == 'ESP32':
                forms = PackBraceletForm(request.POST)
                if forms.is_valid():
                    string_pack = forms.cleaned_data['string_pack']

                    # Despues de obtener el dato se decodifica y se convierte en JSON
                    import base64
                    string_pack = base64.b64decode(string_pack).decode("utf-8")
                    import json
                    string_pack = json.loads(string_pack)

                    # Se extrae del JSON los beacons y la baliza que entrega el reporte
                    listBracelets = string_pack['beacons']
                    baliza = string_pack['baliza']

                    # Se valida que la baliza exista, de lo contrario se envía un correo notificando newBaliza
                    if ValidarExisteBaliza(baliza, request):
                        # evaluo cada Bracelet (Beacon) por separado
                        for bracelet in listBracelets:
                            # Valido que el Bracelet exista, de lo contrario envio correo notificando newBracelet
                            if ValidarExisteBracelet(bracelet['MAC'], baliza, request):
                                ####################################################################
                                ####################################################################
                                ###################                       ##########################
                                ###################  Procesando Sensores  ##########################
                                ###################                       ##########################
                                ####################################################################
                                ####################################################################

                                # print("Procesando datos de los sensores")

                                # leo en la DB el objeto de Bracelet y traigo todos los campos
                                pulsera = Bracelet.objects.get(macDispositivo=ExtractMac(bracelet['MAC']))
                                balizaNow = Baliza.objects.get(macDispositivoBaliza=baliza)

                                # Creo un nuevo registro de historial, pero aún no lo guardo
                                histNew = HistorialBraceletSensors()
                                histNew.bracelet = pulsera
                                histNew.baliza = balizaNow
                                histNew.caida_sensor = bool(int(bracelet['CAI']))
                                histNew.nivel_bateria = int(bracelet['BAT'])
                                histNew.proximidad_sensor = bool(int(bracelet['PRO']))
                                histNew.temperatura_sensor = int(bracelet['TEM'])
                                histNew.rssi_signal = int(bracelet['RSI'])
                                histNew.ppm_sensor = int(bracelet['PPM'])

                                # Leo el ultimo registro para este Bracelet que se está procesando
                                hist = HistorialBraceletSensors.objects.filter(bracelet=pulsera).order_by(
                                    '-fechaRegistro')
                                registroYaExiste = False
                                if len(hist) > 0:
                                    if hist[0].bracelet.macDispositivo == ExtractMac(bracelet['MAC']):
                                        # Valido que no hayan registros seguidos repetidos
                                        factorVariacion = 0.05
                                        if hist[0].caida_sensor == bool(int(bracelet['CAI'])) \
                                                and hist[0].proximidad_sensor == bool(int(bracelet['PRO'])) \
                                                and DeterminarIgualdad_o_cercano(hist[0].nivel_bateria,
                                                                                 int(bracelet['BAT']), factorVariacion) \
                                                and DeterminarIgualdad_o_cercano(hist[0].temperatura_sensor,
                                                                                 int(bracelet['TEM']), factorVariacion) \
                                                and DeterminarIgualdad_o_cercano(hist[0].ppm_sensor,
                                                                                 int(bracelet['PPM']), factorVariacion):
                                            registroYaExiste = True

                                if registroYaExiste == False:
                                    # Si no existe ningún registro para este bracelet, guardo el registro que se preparó anteriormente
                                    # Si el registro actual no es un registro repetido al anterior, entonces guardo el registro que se preparó anteriormente
                                    histNew.save()

                                ValidarCaida(baliza, bracelet['MAC'], bool(int(bracelet['CAI'])), request)
                                ValidadProximidad(baliza, bracelet['MAC'], bool(int(bracelet['PRO'])), request)
                                ValidarTemperatura(bracelet['MAC'], int(bracelet['TEM']), request)
                                ValidarPPM(bracelet['MAC'], int(bracelet['PPM']), request)
                                ValidarNivelBateria(int(bracelet['BAT']), baliza, bracelet['MAC'], request)

                                print("Procesando datos de ubicación")
                                ProcesarUbicacion(baliza, bracelet['MAC'], bracelet['RSI'])
                else:
                    data['error'] = 'Error en datos, favor intentelo de nuevo'
            else:
                data['error'] = 'No ha ingresado a ninguna opción'
        except Exception as e:
            data['error'] = str(e)
        return JsonResponse(data)

    def get_context_data(self, **kwargs):
        context = super().get_context_data(**kwargs)
        context['title'] = 'Recibir un dato de Baliza'
        context['entity'] = 'Crear Dato'
        return context


@csrf_exempt
def getPackStringBaliza(request):
    form = PackBraceletForm()
    if request.method == 'POST':
        form = PackBraceletForm(request.POST)
        if form.is_valid():
            objetos = Utilities.UnZipPackBracelets()
            string_data = form.cleaned_data['string_pack']
            key = form.cleaned_data['key']
            if key:
                if key == "ESP32":
                    if string_data:
                        # vamos a procesar los datos recibidos

                        # listamos los correos de los usuario con rol de server
                        # y se toman los correos para enviar los mensajes si algo sale mal en el proceso siguiente
                        rolBuscar = 'Server'
                        listaCorreosDestinatarios = list()
                        for rol in RolUsuario.objects.all():
                            if rol.rolUsuario.find(rolBuscar) > 0:
                                for usuarioRevisar in UsuarioRol.objects.all():
                                    if usuarioRevisar.rolUsuario == rol:
                                        listaCorreosDestinatarios.append(usuarioRevisar.usuario.email)

                        # Paso 1:
                        # Leer todos los datos de los sensores
                        listBracelets = objetos.setString(string_data)
                        listBracelets = listBracelets[::-1]

                        todasPulserasRegistradas = Bracelet.objects.all()
                        macEncontradasPaquete = list()
                        print("*****************", end="")
                        from time import gmtime, strftime
                        showtime = strftime("%Y-%m-%d %H:%M:%S", gmtime())
                        print(showtime, end="")
                        print("*****************")
                        print("Procesando datos de los sensores")

                        import json
                        baliza = json.loads(string_data)['baliza']
                        balizaEncontrada = None
                        for baliz in Baliza.objects.all():
                            if baliz.macDispositivoBaliza == baliza:
                                balizaEncontrada = baliz
                                break

                        if balizaEncontrada is not None:
                            for bracelet in listBracelets:
                                # Procesando cada bracelet por separado del paquete recibido
                                macPulsera_complete = bracelet.MAC[0:2] \
                                                      + ":" + bracelet.MAC[2:4] \
                                                      + ":" + bracelet.MAC[4:6] \
                                                      + ":" + bracelet.MAC[6:8] \
                                                      + ":" + bracelet.MAC[8:10] \
                                                      + ":" + bracelet.MAC[10:12]

                                distancia = dict()
                                distancia["rssi"] = bracelet.RSI

                                # print("*****************************************************")
                                if macPulsera_complete in macEncontradasPaquete:
                                    pass
                                else:
                                    # Se confirma que no se este procesando un bracelet repetido para este paquete
                                    macEncontradasPaquete.append(macPulsera_complete)

                                    sensores = dict()
                                    sensores["temperatura"] = bracelet.TEM
                                    sensores["ppm"] = bracelet.PPM
                                    sensores["caidas"] = bracelet.CAI
                                    sensores["proximidad"] = bracelet.PRO
                                    otrosDatosPulsera = dict()
                                    otrosDatosPulsera["bat"] = bracelet.BAT
                                    otrosDatosPulsera["semilla"] = bracelet.SED

                                    pulseraEncontrada = None
                                    for i in todasPulserasRegistradas:
                                        if i.macDispositivo == macPulsera_complete:
                                            pulseraEncontrada = i
                                            break

                                    if pulseraEncontrada is not None:

                                        # Paso 1: Procesar datos de  RSSI para determinar la ubicación de los sensores

                                        # Paso 2: Procesar datos de los sensores del bracelet que se reportó en este escaner

                                        dato_ppm_int = int(sensores["ppm"])
                                        dato_caida_bool = bool(sensores["caidas"])
                                        dato_proximidad_bool = bool(sensores["proximidad"])
                                        dato_bateria_int = int(otrosDatosPulsera["bat"])
                                        dato_temperatura_int = int(sensores["temperatura"])
                                        dato_rssi_int = int(distancia["rssi"])

                                        if dato_bateria_int > 0 \
                                                and dato_ppm_int > 0 \
                                                and dato_temperatura_int > 0 \
                                                and dato_rssi_int > 0:

                                            if bracelet.CAI == "1":
                                                diccionarioDatos = dict()
                                                diccionarioDatos['ADMIN'] = str('Admin Server')
                                                diccionarioDatos['BALIZA'] = str(baliza)
                                                diccionarioDatos['MAC'] = str(macPulsera_complete)
                                                diccionarioDatos['PROJECT'] = str('Hospital Smart Bracelet')
                                                diccionarioDatos['FIRMA'] = str('WISROVI')
                                                html_message = render_to_string(
                                                    'email/bracelet_alerta_persona_caida.html',
                                                    diccionarioDatos)
                                                asunto = "Alerta, persona caida (" + macPulsera_complete + ")"
                                                firmaResumenRemitente = "Hospital Smart Bracelet"

                                                import threading
                                                x = threading.Thread(target=Utilities.sendMail,
                                                                     args=(asunto, html_message, firmaResumenRemitente,
                                                                           listaCorreosDestinatarios, request,))
                                                x.start()
                                                print(macPulsera_complete + " - Persona caida")

                                            if not bracelet.PRO == "1":
                                                diccionarioDatos = dict()
                                                diccionarioDatos['ADMIN'] = str('Admin Server')
                                                diccionarioDatos['BALIZA'] = str(baliza)
                                                diccionarioDatos['MAC'] = str(macPulsera_complete)
                                                diccionarioDatos['PROJECT'] = str('Hospital Smart Bracelet')
                                                diccionarioDatos['FIRMA'] = str('WISROVI')
                                                html_message = render_to_string(
                                                    'email/bracelet_alerta_persona_seQuitoPulsera.html',
                                                    diccionarioDatos)
                                                asunto = "Alerta, persona se quitó el bracelet (" + macPulsera_complete + ")"
                                                firmaResumenRemitente = "Hospital Smart Bracelet"
                                                import threading
                                                x = threading.Thread(target=Utilities.sendMail,
                                                                     args=(asunto, html_message, firmaResumenRemitente,
                                                                           listaCorreosDestinatarios, request,))
                                                x.start()
                                                print(macPulsera_complete + " - Persona se quitó el bracelet")

                                            histBraceSensors = HistorialBraceletSensors()
                                            histBraceSensors.bracelet = pulseraEncontrada
                                            histBraceSensors.ppm_sensor = dato_ppm_int
                                            histBraceSensors.caida_sensor = dato_caida_bool
                                            histBraceSensors.proximidad_sensor = dato_proximidad_bool
                                            histBraceSensors.nivel_bateria = dato_bateria_int
                                            histBraceSensors.temperatura_sensor = dato_temperatura_int
                                            histBraceSensors.rssi_signal = dato_rssi_int
                                            histBraceSensors.baliza = balizaEncontrada

                                            todosRegistros = HistorialBraceletSensors.objects.filter(
                                                bracelet=pulseraEncontrada.id).order_by('-id')
                                            print(pulseraEncontrada, end=" ( id=")
                                            if todosRegistros.count() > 0:
                                                for esteRegistro in todosRegistros:
                                                    print(esteRegistro.id, "): ", end="")
                                                    if histBraceSensors.bracelet == esteRegistro.bracelet \
                                                            and histBraceSensors.rssi_signal == esteRegistro.rssi_signal \
                                                            and histBraceSensors.ppm_sensor == esteRegistro.ppm_sensor \
                                                            and histBraceSensors.caida_sensor == esteRegistro.caida_sensor \
                                                            and histBraceSensors.temperatura_sensor == esteRegistro.temperatura_sensor \
                                                            and histBraceSensors.proximidad_sensor == esteRegistro.proximidad_sensor \
                                                            and histBraceSensors.nivel_bateria == esteRegistro.nivel_bateria:
                                                        print("Ya existe el registro")
                                                    else:
                                                        print("Registro no existe...", end="")
                                                        histBraceSensors.save()
                                                        print("Registro Guardado")
                                                    break
                                            else:
                                                print("No existen registros para este bracelet...", end="")
                                                histBraceSensors.save()
                                                print("Primer Registro Guardado")
                                                break
                                        else:
                                            # Datos invalidos, se reporta en correo de que el bracelet esta enviando datos invalidos

                                            diccionarioDatos = dict()
                                            diccionarioDatos['ADMIN'] = str('Admin Server')
                                            diccionarioDatos['BALIZA'] = str(baliza)
                                            diccionarioDatos['MAC'] = str(macPulsera_complete)
                                            diccionarioDatos['PROJECT'] = str('Hospital Smart Bracelet')
                                            diccionarioDatos['FIRMA'] = str('WISROVI')
                                            html_message = render_to_string('email/bracelet_report_bad_sensors.html',
                                                                            diccionarioDatos)
                                            asunto = "Nuevo Bracelet por registrar (" + macPulsera_complete + ")"
                                            firmaResumenRemitente = "Hospital Smart Bracelet"
                                            import threading
                                            x = threading.Thread(target=Utilities.sendMail,
                                                                 args=(asunto, html_message, firmaResumenRemitente,
                                                                       listaCorreosDestinatarios, request,))
                                            x.start()
                                    else:
                                        print(macPulsera_complete, "- No existe el Bracelet")

                                        # como no existe la pulsera, entonces se envia un correo usando una plantilla
                                        # donde se reemplazan los datos en la plantilla y con esto se envia el correo
                                        # a los correos destinatarios y el asunto estipulado

                                        diccionarioDatos = dict()
                                        diccionarioDatos['ADMIN'] = str('Admin Server')
                                        diccionarioDatos['BALIZA'] = str(baliza)
                                        diccionarioDatos['MAC'] = str(macPulsera_complete)
                                        diccionarioDatos['PROJECT'] = str('Hospital Smart Bracelet')
                                        diccionarioDatos['FIRMA'] = str('WISROVI')
                                        html_message = render_to_string('email/nuevo_bracelet_encontrado.html',
                                                                        diccionarioDatos)
                                        asunto = "Nuevo Bracelet por registrar (" + macPulsera_complete + ")"
                                        firmaResumenRemitente = "Hospital Smart Bracelet"
                                        import threading
                                        x = threading.Thread(target=Utilities.sendMail,
                                                             args=(asunto, html_message, firmaResumenRemitente,
                                                                   listaCorreosDestinatarios, request,))
                                        x.start()

                            print("*****************************************************")
                            return HttpResponseRedirect('../receivedOK')
                        else:
                            diccionarioDatos = dict()
                            diccionarioDatos['ADMIN'] = str('Admin Server')
                            diccionarioDatos['BALIZA'] = str(baliza)
                            diccionarioDatos['PROJECT'] = str('Hospital Smart Bracelet')
                            diccionarioDatos['FIRMA'] = str('WISROVI')
                            html_message = render_to_string('email/nuevo_baliza_encontrada.html',
                                                            diccionarioDatos)
                            asunto = "Nueva Baliza por registrar (" + baliza + ")"
                            firmaResumenRemitente = "Hospital Smart Bracelet"
                            import threading
                            x = threading.Thread(target=Utilities.sendMail,
                                                 args=(asunto, html_message, firmaResumenRemitente,
                                                       listaCorreosDestinatarios, request,))
                            x.start()
                            print(baliza + " - Nueva Baliza por registrar")
                        # Entregar respuesta final
    context = dict()
    context['PROJECT'] = Preferences.NAME_APP
    context['form'] = form
    return render(request, "FORM.html", context)


def setReceivedOK(request):
    return render(request, 'Server/receivedOK.html', {})
