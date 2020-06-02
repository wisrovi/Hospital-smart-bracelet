from django.shortcuts import render
from django.contrib.auth.decorators import login_required
from django.http import HttpResponseRedirect
from django.core.mail import send_mail
from django.views.decorators.csrf import csrf_exempt

from baliza.models import Bracelet, HistorialBraceletSensors

import baliza.Util as BALIZ

from baliza.Form import PackBracelet


# Create your views here.

# @login_required
@csrf_exempt
def getPackStringBaliza(request):
    if request.method == 'POST':
        form = PackBracelet(request.POST)
        if form.is_valid():
            objetos = BALIZ.UnZipPackBracelets()
            string_data = form.cleaned_data['string_pack']
            key = form.cleaned_data['key']
            if key:
                if key == "ESP32":
                    if string_data:
                        listBracelets = objetos.setString(string_data)

                        todasPulserasRegistradas = Bracelet.objects.all()

                        for bracelet in listBracelets:
                            macPulsera = bracelet.MAC
                            sensores = dict()
                            sensores["temperatura"] = bracelet.TEM
                            sensores["ppm"] = bracelet.PPM
                            sensores["caidas"] = bracelet.CAI
                            sensores["proximidad"] = bracelet.PRO

                            distancia = dict()
                            distancia["rssi"] = bracelet.RSI

                            otrosDatosPulsera = dict()
                            otrosDatosPulsera["bat"] = bracelet.BAT
                            otrosDatosPulsera["semilla"] = bracelet.SED

                            macPulsera_complete = macPulsera[0:2] \
                                                  + ":" + macPulsera[2:4] \
                                                  + ":" + macPulsera[4:6] \
                                                  + ":" + macPulsera[6:8] \
                                                  + ":" + macPulsera[8:10] \
                                                  + ":" + macPulsera[10:12]
                            pulseraEncontrada = None
                            for i in todasPulserasRegistradas:
                                if i.macDispositivo == macPulsera_complete:
                                    pulseraEncontrada = i

                            if pulseraEncontrada is not None:
                                histBraceSensors = HistorialBraceletSensors()
                                histBraceSensors.bracelet = pulseraEncontrada
                                histBraceSensors.ppm_sensor = int(sensores["ppm"])
                                histBraceSensors.caida_sensor = bool(sensores["caidas"])
                                histBraceSensors.proximidad_sensor = bool(sensores["proximidad"])
                                histBraceSensors.nivel_bateria = int(otrosDatosPulsera["bat"])
                                histBraceSensors.temperatura_sensor = int(sensores["temperatura"])
                                histBraceSensors.rssi_signal = int(distancia["rssi"])

                                isRegisterSave = False
                                if len(HistorialBraceletSensors.objects.all()) > 0:
                                    ultimoRegistroParaEstaMac = None
                                    try:
                                        ultimoRegistroParaEstaMac = HistorialBraceletSensors.objects.get(
                                            id=pulseraEncontrada.id)
                                    except:
                                        pass

                                    if ultimoRegistroParaEstaMac is not None \
                                            and ultimoRegistroParaEstaMac.ppm_sensor == histBraceSensors.ppm_sensor \
                                            and ultimoRegistroParaEstaMac.caida_sensor == histBraceSensors.caida_sensor \
                                            and ultimoRegistroParaEstaMac.proximidad_sensor == histBraceSensors.proximidad_sensor \
                                            and ultimoRegistroParaEstaMac.temperatura_sensor == histBraceSensors.temperatura_sensor \
                                            and ultimoRegistroParaEstaMac.rssi_signal == histBraceSensors.rssi_signal \
                                            and ultimoRegistroParaEstaMac.nivel_bateria == histBraceSensors.nivel_bateria:
                                        print("Registro ya existe")
                                    else:
                                        isRegisterSave = True
                                        histBraceSensors.save()
                                else:
                                    isRegisterSave = True
                                    histBraceSensors.save()

                                if len(listBracelets) > 0 and isRegisterSave:
                                    send_mail(
                                        "asunto prueba",
                                        "Hola mundo",
                                        "WISROVI",
                                        ["wisrovi.rodriguez@gmail.com"],
                                        fail_silently=False,
                                    )
                                    return HttpResponseRedirect('../receivedOK')
    else:
        form = PackBracelet()
    return render(request, "receivedBaliza.html", {'form': form})


def setReceivedOK(request):
    return render(request, 'receivedOK.html', {})
