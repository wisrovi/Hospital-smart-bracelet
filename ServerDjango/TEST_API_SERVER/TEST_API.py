listaMacs = ["90E202048AE8", "90E202048AE9", "90E202048AE7", ]
semilla = ["0000"]
balizas = ["80:E2:02:04:8A:E7"]

import json

pulsera1 = {
    "SED": semilla[0],
    "MAC": listaMacs[0],
    "BAT": str(35),
    "PPM": str(85),
    "CAI": str(int(False)),
    "TEM": str(366),
    "RSI": str(60),
    "PRO": str(int(True))
}

pulsera2 = {
    "SED": semilla[0],
    "MAC": listaMacs[1],
    "BAT": str(33),
    "PPM": str(78),
    "CAI": str(int(False)),
    "TEM": str(327),
    "RSI": str(69),
    "PRO": str(int(True))
}

pulsera3 = {
    "SED": semilla[0],
    "MAC": listaMacs[2],
    "BAT": str(33),
    "PPM": str(95),
    "CAI": str(int(False)),
    "TEM": str(357),
    "RSI": str(80),
    "PRO": str(int(True))
}

listadoPulseras = list()
listadoPulseras.append(pulsera1)
listadoPulseras.append(pulsera2)
listadoPulseras.append(pulsera3)

paqueteEnviar = {
    "beacons": listadoPulseras,
    "baliza": balizas[0]
}

import base64

paqueteEnviar = base64.b64encode(bytes(json.dumps(paqueteEnviar), 'utf-8'))

import requests

# URL = "http://192.168.1.3:5000/baliza/received/"
URL = "http://localhost:8000/hospitalsmartbracelet/received2/"
PARAMS = {
    'key': "ESP32",
    "string_pack": paqueteEnviar
}

print(PARAMS)

try:
    r = requests.post(url=URL, data=PARAMS)
    if r.status_code == 200:
        print("[TEST_API]: Paquete enviado correctamente")
        # print(r.text)
    else:
        print("Failed")
except Exception as e:
    print(str(e))
