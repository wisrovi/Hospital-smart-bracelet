import requests

URL = "http://192.168.1.1:2020/esp32"

PARAMS = {
    "Data" : "Probando emulador de Yhon"
}

#r = requests.get(url=URL, params=PARAMS)
r = requests.post(url=URL, data=PARAMS)


print(r.text)


