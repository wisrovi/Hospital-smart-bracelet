class Bracelet:
    SED = str()
    MAC = str()
    BAT = str()
    PPM = str()
    CAI = str()
    TEM = str()
    RSI = str()
    PRO = str()

class UnZipPackBracelets:
    listBracelets = list()
    listConvert = list()

    def setString(self, string):
        if string is not None:
            import json
            contenidoJson = json.loads(string)

            for beacon in contenidoJson['beacons']:
                bracelec = Bracelet()
                bracelec.BAT = beacon['BAT']
                bracelec.MAC = beacon['MAC']
                bracelec.SED = beacon['SED']
                bracelec.PPM = beacon['PPM']
                bracelec.CAI = beacon['CAI']
                bracelec.TEM = beacon['TEM']
                bracelec.RSI = beacon['RSI']
                bracelec.PRO = beacon['PRO']
                self.listBracelets.append(bracelec)
            return self.listBracelets
        else:
            return None

    def convertList(self):
        import json
        self.listConvert = list()
        for bracelet in self.listBracelets:
            self.listConvert.append(json.dumps(bracelet.__dict__))
        return self.listConvert






from django.contrib import messages  # import messages
from django.core.mail import send_mail
from django.utils.html import strip_tags
def sendMail(asunto, html, firma, correo, request):
    if type(correo) != list:
        correo = [correo]

    send_mail(
        asunto,
        strip_tags(html),
        firma,
        correo,
        fail_silently=False,
        html_message=html
    )
    messages.success(request, "Mensaje enviado")