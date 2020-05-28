import base64

from flask import Flask, request

app = Flask(__name__)

def decoBase64UrlSafe(s):
    mensajeFinal = ""
    
    arrayStrings = list()
    while True:
        hayEspacio = s.find(' ')
        if hayEspacio > 0:
            primeraParte = s [:hayEspacio]
            s = s[hayEspacio:]
            arrayStrings.append(  primeraParte  )
        else:
            arrayStrings.append(  s  )
            break
            
    try:        
        for i in range(  len(arrayStrings)  ):
            stringDecodificado = base64.urlsafe_b64decode(arrayStrings[i] + "=" * (4 - len(s) % 4))
            stringDecodificado = stringDecodificado.decode("utf-8")
            mensajeFinal = mensajeFinal + stringDecodificado
        print(mensajeFinal)
    except:
        pass
    
    return mensajeFinal

def decoBase64(s):
    stringDecodificado = base64.b64decode(s)
    #print(stringDecodificado)
    return stringDecodificado.decode("utf-8") 

@app.route('/', methods=['GET', 'POST'])
def email():
    print("Email solicitado")
    TO_EMAIL = ""
    if request.method == 'GET':
        datos = request.values.get('Datos')
        print(datos)
        return "OK"
    else:
        print("No se pudo enviar el correo, no se solicito un GET")
        return "POST"




if __name__ == '__main__':
    app.run(debug=True, host='192.168.1.1', port=2020)


