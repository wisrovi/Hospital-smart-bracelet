from django.template.loader import render_to_string

from apps.baliza.models import Baliza, RolUsuario, UsuarioRol, Bracelet, BraceletUmbrals, HistorialRSSI
import apps.Util_apps.Util_braceletBLE as Utilities
from apps.baliza.test.LibraryRSSItoMts import CalcularDistancia


def getDestinatariosCorreos():
    rolBuscar = 'Server'
    listaCorreosDestinatarios = list()
    for rol in RolUsuario.objects.all():
        if rol.rolUsuario.find(rolBuscar) > 0:
            for usuarioRevisar in UsuarioRol.objects.all():
                if usuarioRevisar.rolUsuario == rol:
                    listaCorreosDestinatarios.append(usuarioRevisar.usuario.email)
    if len(listaCorreosDestinatarios) == 0:
        print("[System]: No hay destinatarios para reportar los correos")
    return listaCorreosDestinatarios


def ValidarExisteBaliza(baliza, request):
    balizasExistentes = Baliza.objects.all()
    for baliz in balizasExistentes:
        if baliz.macDispositivoBaliza == baliza:
            return True

    diccionarioDatos = dict()
    diccionarioDatos['ADMIN'] = str('Admin Server')
    diccionarioDatos['BALIZA'] = str(baliza)
    diccionarioDatos['PROJECT'] = str('Hospital Smart Bracelet')
    diccionarioDatos['FIRMA'] = str('WISROVI')
    html_message = render_to_string('email/nuevo_baliza_encontrada.html',
                                    diccionarioDatos)
    asunto = "Nueva Baliza por registrar (" + baliza + ")"
    firmaResumenRemitente = "Hospital Smart Bracelet"

    listaDestinatarios = getDestinatariosCorreos()
    if len(listaDestinatarios) > 0:
        import threading
        x = threading.Thread(target=Utilities.sendMail,
                             args=(asunto, html_message, firmaResumenRemitente,
                                   listaDestinatarios, request,))
        x.start()
        print("Nueva Baliza encontrada")
    return False


def ValidarExisteBracelet(bracelet, baliza, request):
    bracelet = ExtractMac(bracelet)

    braceletsExistentes = Bracelet.objects.all()
    for brac in braceletsExistentes:
        if bracelet == brac.macDispositivo:
            return True

    diccionarioDatos = dict()
    diccionarioDatos['ADMIN'] = str('Admin Server')
    diccionarioDatos['BALIZA'] = str(baliza)
    diccionarioDatos['MAC'] = str(bracelet)
    diccionarioDatos['PROJECT'] = str('Hospital Smart Bracelet')
    diccionarioDatos['FIRMA'] = str('WISROVI')
    html_message = render_to_string('email/bracelet_report_bad_sensors.html',
                                    diccionarioDatos)
    asunto = "Nuevo Bracelet por registrar (" + bracelet + ")"
    firmaResumenRemitente = "Hospital Smart Bracelet"

    listaDestinatarios = getDestinatariosCorreos()
    if len(listaDestinatarios) > 0:
        import threading
        x = threading.Thread(target=Utilities.sendMail,
                             args=(asunto, html_message, firmaResumenRemitente,
                                   listaDestinatarios, request,))
        x.start()
        print("Nuevo Bracelet encontrado")
    return False


def ExtractMac(string):
    macPulsera_complete = string[0:2] \
                          + ":" + string[2:4] \
                          + ":" + string[4:6] \
                          + ":" + string[6:8] \
                          + ":" + string[8:10] \
                          + ":" + string[10:12]
    return macPulsera_complete


def getUmbrales(macPulsera):
    pulsera = Bracelet.objects.get(macDispositivo=ExtractMac(macPulsera))
    umbrales = BraceletUmbrals.objects.get(bracelet=pulsera)
    return umbrales


def ValidarCaida(baliza, macPulsera, caida, request):
    if caida:
        diccionarioDatos = dict()
        diccionarioDatos['ADMIN'] = str('Admin Server')
        diccionarioDatos['BALIZA'] = str(baliza)
        diccionarioDatos['MAC'] = str(ExtractMac(macPulsera))
        diccionarioDatos['PROJECT'] = str('Hospital Smart Bracelet')
        diccionarioDatos['FIRMA'] = str('WISROVI')
        html_message = render_to_string(
            'email/bracelet_alerta_persona_caida.html',
            diccionarioDatos)
        asunto = "Alerta, persona caida (" + ExtractMac(macPulsera) + ")"
        firmaResumenRemitente = "Hospital Smart Bracelet"

        listaCorreosDestinatarios = getDestinatariosCorreos()
        if len(listaCorreosDestinatarios) > 0:
            import threading
            x = threading.Thread(target=Utilities.sendMail,
                                 args=(asunto, html_message, firmaResumenRemitente,
                                       listaCorreosDestinatarios, request,))
            x.start()
            print("Bracelet alerta caida")


def ValidadProximidad(baliza, macPulsera, proximidad, request):
    if proximidad == False:
        diccionarioDatos = dict()
        diccionarioDatos['ADMIN'] = str('Admin Server')
        diccionarioDatos['BALIZA'] = str(baliza)
        diccionarioDatos['MAC'] = str(macPulsera)
        diccionarioDatos['PROJECT'] = str('Hospital Smart Bracelet')
        diccionarioDatos['FIRMA'] = str('WISROVI')
        html_message = render_to_string(
            'email/bracelet_alerta_persona_seQuitoPulsera.html',
            diccionarioDatos)
        asunto = "Alerta, persona se quitó el bracelet (" + macPulsera + ")"
        firmaResumenRemitente = "Hospital Smart Bracelet"

        listaCorreosDestinatarios = getDestinatariosCorreos()
        if len(listaCorreosDestinatarios) > 0:
            import threading
            x = threading.Thread(target=Utilities.sendMail,
                                 args=(asunto, html_message, firmaResumenRemitente,
                                       listaCorreosDestinatarios, request,))
            x.start()
            print("Bracelet alerta proximidad")


def ValidarTemperatura(macPulsera, temperaturaActual, request):
    umbrales = getUmbrales(macPulsera)

    diccionarioDatos = dict()
    diccionarioDatos['ADMIN'] = str('Admin Server')
    diccionarioDatos['MAC'] = str(macPulsera)
    diccionarioDatos['PROJECT'] = str('Hospital Smart Bracelet')
    diccionarioDatos['FIRMA'] = str('WISROVI')
    html_message = render_to_string(
        'email/bracelet_alerta_persona_seQuitoPulsera.html',
        diccionarioDatos)
    asunto = None
    hayAlarma = False

    temperaturaActual = temperaturaActual / 10

    if temperaturaActual > umbrales.maximaTemperatura:
        asunto = "Alerta, persona (" + macPulsera + ")" + " tiene temperatura alta (" + str(temperaturaActual) + ")"
        hayAlarma = True

    if temperaturaActual < umbrales.minimaTemperatura:
        asunto = "Alerta, persona (" + macPulsera + ")" + " tiene temperatura baja (" + str(temperaturaActual) + ")"
        hayAlarma = True

    firmaResumenRemitente = "Hospital Smart Bracelet"

    listaCorreosDestinatarios = getDestinatariosCorreos()
    if len(listaCorreosDestinatarios) > 0 and hayAlarma:
        import threading
        x = threading.Thread(target=Utilities.sendMail,
                             args=(asunto, html_message, firmaResumenRemitente,
                                   listaCorreosDestinatarios, request,))
        x.start()
        print("Bracelet alerta temperatura")


def ValidarNivelBateria(nivelBateria, baliza, macPulsera, request):
    if nivelBateria < 30:
        diccionarioDatos = dict()
        diccionarioDatos['ADMIN'] = str('Admin Server')
        diccionarioDatos['BALIZA'] = str(baliza)
        diccionarioDatos['MAC'] = str(macPulsera)
        diccionarioDatos['PROJECT'] = str('Hospital Smart Bracelet')
        diccionarioDatos['FIRMA'] = str('WISROVI')
        html_message = render_to_string(
            'email/bracelet_alerta_persona_seQuitoPulsera.html',
            diccionarioDatos)
        asunto = "Alerta, bracelet (" + macPulsera + ") con bateria baja."
        firmaResumenRemitente = "Hospital Smart Bracelet"

        listaCorreosDestinatarios = getDestinatariosCorreos()
        if len(listaCorreosDestinatarios) > 0:
            import threading
            x = threading.Thread(target=Utilities.sendMail,
                                 args=(asunto, html_message, firmaResumenRemitente,
                                       listaCorreosDestinatarios, request,))
            x.start()
            print("Bracelet alerta bateria baja")


def ValidarPPM(macPulsera, ppmActual, request):
    umbrales = getUmbrales(macPulsera)

    diccionarioDatos = dict()
    diccionarioDatos['ADMIN'] = str('Admin Server')
    diccionarioDatos['MAC'] = str(macPulsera)
    diccionarioDatos['PROJECT'] = str('Hospital Smart Bracelet')
    diccionarioDatos['FIRMA'] = str('WISROVI')
    html_message = render_to_string(
        'email/bracelet_alerta_persona_seQuitoPulsera.html',
        diccionarioDatos)
    asunto = None

    hayAlarma = False

    if ppmActual > umbrales.maximaPulsoCardiaco:
        asunto = "Alerta, persona (" + macPulsera + ")" + " tiene PPM alta (" + str(ppmActual) + ")"
        hayAlarma = True

    if ppmActual < umbrales.minimoPulsoCardiaco:
        asunto = "Alerta, persona (" + macPulsera + ")" + " tiene PPM baja (" + str(ppmActual) + ")"
        hayAlarma = True

    firmaResumenRemitente = "Hospital Smart Bracelet"

    listaCorreosDestinatarios = getDestinatariosCorreos()
    if len(listaCorreosDestinatarios) > 0 and hayAlarma:
        import threading
        x = threading.Thread(target=Utilities.sendMail,
                             args=(asunto, html_message, firmaResumenRemitente,
                                   listaCorreosDestinatarios, request,))
        x.start()
        print("Bracelet alerta temperatura")


def DeterminarIgualdad_o_cercano(valorAnterior, valorActual, variacion):
    if valorAnterior == valorActual:
        return True

    indiceVariacion = valorAnterior * variacion
    variacionSuperior = valorAnterior + indiceVariacion
    variacionInferior = valorAnterior - indiceVariacion

    if valorActual > variacionSuperior:
        return False

    if valorActual < variacionInferior:
        return False

    return True


def ProcesarUbicacion(baliza, macPulsera, rssi):
    macPulsera = ExtractMac(macPulsera)
    pulsera = Bracelet.objects.get(macDispositivo=macPulsera)

    measuredPower = pulsera.txPower
    rssi = int(rssi)

    distancia = CalcularDistancia(measuredPower, rssi)

    balizaNow = Baliza.objects.get(macDispositivoBaliza=baliza)
    histoRssi = HistorialRSSI()
    histoRssi.baliza = balizaNow
    histoRssi.bracelet = pulsera
    histoRssi.rssi_signal = rssi

    ultimoRegistro = HistorialRSSI.objects.filter(bracelet=pulsera,
                                                  baliza=balizaNow).order_by('-fechaRegistro')
    if len(ultimoRegistro) > 0:
        if ultimoRegistro[0].rssi_signal != rssi:
            histoRssi.save()
    else:
        histoRssi.save()

    print("Baliza:", baliza, ", Pulsera:", macPulsera, ", metros:", distancia)

# fin clase
