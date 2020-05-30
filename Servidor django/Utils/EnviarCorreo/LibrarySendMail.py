import smtplib
from datetime import datetime
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from string import Template

from Utils.EnviarCorreo.GMAIL_CONFIG import EMAIL_HOST, EMAIL_PORT, EMAIL_HOST_PASSWORD, EMAIL_HOST_USER

REMITENTE = str()
DESTINARIO = str()

def setDestinatario(nuevoDestinatario=str()):
    global DESTINARIO
    DESTINARIO = nuevoDestinatario

def setRemitente(nuevoRemitente=str()):
    global REMITENTE
    REMITENTE = nuevoRemitente

def read_template(filename):
    filename = "Utils/EnviarCorreo/templatesHTML/" + filename
    with open(filename, mode='r') as template_file:
        template_file_content = template_file.read()
    return Template(template_file_content)

def SendMail(asunto, body):
    global REMITENTE
    global DESTINARIO
    if len(REMITENTE) == 0:
        REMITENTE = EMAIL_HOST_USER

    if len(DESTINARIO) > 0:
        destino = DESTINARIO
        remitente = REMITENTE
        try:
            msg = MIMEMultipart()
            msg['Subject'] = asunto
            msg['To'] = destino
            msg['From'] = remitente
            msg.attach(MIMEText(body, 'html'))

            print('\nSending message, Please wait...')
            server = smtplib.SMTP(EMAIL_HOST, EMAIL_PORT)
            server.starttls()
            server.login(remitente, EMAIL_HOST_PASSWORD)
            server.sendmail(remitente, destino, msg.as_string())
            status_close_mail = server.quit()
            print('\nComplete!! :)\n')
        except KeyError:
            print(':( Error trying send mail with Python!\n')
        return True
    else:
        print("No hay un destinatario a quien enviarle el correo.")
        return False

def getDateNow():
    now = datetime.now()
    dt_string = now.strftime("%d/%m/%Y %H:%M:%S")
    return dt_string