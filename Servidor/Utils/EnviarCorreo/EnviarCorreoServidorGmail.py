import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText

from django.template.loader import render_to_string

from config import settings
from django.contrib.auth.models import User
from django.core.mail import send_mail

from string import Template
from datetime import datetime


def prueba(id):
    user = User.objects.get(pk=id)
    #path_template_m = 'templates/Email/welcome.html'
    #message_template = read_template(path_template_m)
    #body = message_template
    send_mail(
        'Welcome ',
        'mensaje de prueba',
        'wisrovi.rodriguez@gmail.com',
        ['wisrovi.rodriguez@gmail.com'],
        fail_silently=False,
    )


def send_emai(id, html_template='templates/EMAIL/welcome.html'):
    user = User.objects.get(pk=id)
    message = MIMEMultipart('alternative')  # si vas a enviar texto plano pones text, este sirve para html
    message['Subject'] = 'Reseteo de contraseña'
    message['From'] = settings.EMAIL.EMAIL_HOST_USER
    message['To'] = user.email

    html = render_to_string(html_template)
    content = MIMEText(html, 'html')
    message.attach(content)
    server = smtplib.SMTP(settings.EMAIL.EMAIL_HOST, settings.EMAIL.EMAIL_PORT)
    server.starttls()
    server.login(settings.EMAIL.EMAIL_HOST_USER, settings.EMAIL.EMAIL_HOST_PASSWORD)
    server.sendmail(
        settings.EMAIL.EMAIL_HOST_USER, user.email, message.as_string()
    )
    server.quit()


def getDateNow():
    now = datetime.now()
    dt_string = now.strftime("%d/%m/%Y %H:%M:%S")
    return dt_string


def read_template(filename):
    """
    import Utils.EnviarCorreo.EnviarCorreoServidorGmail as Util

    message_template = Util.read_template(path_template_m)
    body = message_template.substitute(
        PERSON_NAME=user,
        FIRMA=firma)
    """
    with open(filename, mode='r') as template_file:
        template_file_content = template_file.read()
        print(template_file_content)
    return Template(template_file_content)


def PrepareBody():
    pass


def SendMailUserLogging(asunto, body, id):
    user = User.objects.get(pk=id)
    try:
        msg = MIMEMultipart()
        msg['Subject'] = asunto
        msg['To'] = user.email
        msg['From'] = settings.EMAIL.EMAIL_HOST_USER
        print(body)
        # body = body.substitute(
        #     PERSON_NAME=str(user))
        msg.attach(MIMEText(body, 'html'))

        print('\nSending message, Please wait...')
        server = smtplib.SMTP(settings.EMAIL.EMAIL_HOST, settings.EMAIL.EMAIL_PORT)
        server.starttls()
        server.login(settings.EMAIL.EMAIL_HOST_USER, settings.EMAIL.EMAIL_HOST_PASSWORD)
        server.sendmail(
            settings.EMAIL.EMAIL_HOST_USER, user.email, msg.as_string()
        )
        status_close_mail = server.quit()
        print('\nComplete!! :)\n')
        return True
    except KeyError:
        print(':( Error trying send mail with Python!\n')
        return False
