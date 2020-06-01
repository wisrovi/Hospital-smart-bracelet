import requests 

URL = "http://localhost:8000/baliza/received/"

PARAMS = {
    'key': "ESP32",
    "string_pack" : '{"beacons":[{"SED":"0000","MAC":"90E202048AE8","BAT":"38","PPM":"095","CAI":"1","TEM":"328","RSI":"075","PRO":"1"},{"SED":"0000","MAC":"90E202048AE9","BAT":"40","PPM":"095","CAI":"1","TEM":"328","RSI":"075","PRO":"1"},{"SED":"0000","MAC":"90E202048AE8","BAT":"40","PPM":"082","CAI":"1","TEM":"328","RSI":"075","PRO":"1"},{"SED":"0000","MAC":"90E202048AE9","BAT":"40","PPM":"095","CAI":"1","TEM":"328","RSI":"075","PRO":"1"},{"SED":"0000","MAC":"90E202048AE9","BAT":"40","PPM":"095","CAI":"1","TEM":"328","RSI":"075","PRO":"1"}]}'
} 


r = requests.post(url = URL, data = PARAMS) 

print(r.text)