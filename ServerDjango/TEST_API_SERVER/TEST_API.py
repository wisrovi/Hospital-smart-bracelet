listaMacs = [ "90E202048AE8", "90E202048AE9" ]
semilla = ["0000"]
balizas = ["80:E2:02:04:8A:E9"]

import json 

pulsera1 = {
    "SED" : semilla[0],
    "MAC" : listaMacs[1],
    "BAT" : str(40),
    "PPM" : str(95),
    "CAI" : str(int(False)),
    "TEM" : str(328),
    "RSI" : str(72),
    "PRO" : str(int(True))
}

pulsera2 = {
    "SED" : semilla[0],
    "MAC" : listaMacs[0],
    "BAT" : str(40),
    "PPM" : str(95),
    "CAI" : str(int(False)),
    "TEM" : str(327),
    "RSI" : str(72),
    "PRO" : str(int(True))
}

pulsera3 = {
    "SED" : semilla[0],
    "MAC" : listaMacs[1],
    "BAT" : str(40),
    "PPM" : str(95),
    "CAI" : str(int(False)),
    "TEM" : str(328),
    "RSI" : str(72),
    "PRO" : str(int(False))
}

pulsera4 = {
    "SED" : semilla[0],
    "MAC" : listaMacs[1],
    "BAT" : str(33),
    "PPM" : str(75),
    "CAI" : str(int(False)),
    "TEM" : str(328),
    "RSI" : str(72),
    "PRO" : str(int(True))
}

listadoPulseras = list()
listadoPulseras.append(pulsera1)
listadoPulseras.append(pulsera2)
listadoPulseras.append(pulsera3)
listadoPulseras.append(pulsera4)

paqueteEnviar = {
    "beacons" : listadoPulseras,
    "baliza" : balizas[0]
}

import requests 
URL = "http://localhost:8000/baliza/received/"
PARAMS = {    
    'key': "ESP32",
    "string_pack" : json.dumps(paqueteEnviar)
} 

r = requests.post(url = URL, data = PARAMS) 
if r.status_code == 200:
    print("Paquete enviado correctamente")
    print(r.text)
else:
    print("Failed")