import requests

URL = "http://192.168.1.57:8080/esp32"

PARAMS = {
    "Data" : "probanbdo emulador por Cesar"
}

#r = requests.get(url=URL, params=PARAMS)
r = requests.post(url=URL, data=PARAMS)
print(r.text)