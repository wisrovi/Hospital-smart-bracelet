# -*- coding: utf-8 -*-

import smtplib
from datetime import datetime
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from string import Template

from Utils.EnviarCorreo.GMAIL_CONFIG import EMAIL_HOST, EMAIL_PORT, EMAIL_HOST_PASSWORD

REMITENTE = str()
DESTINARIO = str()


def setDestinatario(nuevoDestinatario=str()):
    global DESTINARIO
    DESTINARIO = nuevoDestinatario

def setRemitente(nuevoRemitente=str()):
    global REMITENTE
    REMITENTE = nuevoRemitente


def read_template(filename):
    filename = "apps/worktime/Library/" + filename
    with open(filename, mode='r') as template_file:
        template_file_content = template_file.read()
    return Template(template_file_content)


def SendMail(asunto, body):
    global REMITENTE
    global DESTINARIO
    if len(REMITENTE) > 0 and len(DESTINARIO) > 0:
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
        return False


def getDateNow():
    now = datetime.now()
    dt_string = now.strftime("%d/%m/%Y %H:%M:%S")
    return dt_string


def EnviarCorreoInicio(nombreDestinatario='ATMOSFERIA', firma='William Rodriguez'):
    path_template_m = '../../../Utils/EnviarCorreo/templatesHTML/mensajeStart.html'
    message_template = read_template(path_template_m)
    body = message_template.substitute(
        PERSON_NAME=nombreDestinatario,
        FIRMA=firma)
    asunto = 'VIU-WilliamRodriguezVillamizar-{}-INICIO'.format(getDateNow())
    SendMail(asunto, body)


def EnviarCorreoCierre(tareasRealizadas=[]):
    path_template_m = '../../../Utils/EnviarCorreo/templatesHTML/mensajeEnd.html'
    message_template = read_template(path_template_m)

    contenido = str()
    for i in range(len(tareasRealizadas)):
        path_template_task = '../../../Utils/EnviarCorreo/templatesHTML/tablaTareas.html'
        task_template = read_template(path_template_task)
        task = task_template.substitute(
            NUMBER_TASK=str(i + 1) + ')  ',
            DESCRIPTION_TASK=tareasRealizadas[i])
        contenido += task

    body = message_template.substitute(
        PERSON_NAME='ATMOSFERIA',
        PROJECT_NAME='ivAdventure',
        FIRMA='William Rodriguez',
        CONTEN=contenido)

    asunto = 'VIU-WilliamRodriguezVillamizar-{}-FINAL'.format(getDateNow())
    SendMail(asunto, body)
