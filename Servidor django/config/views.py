from django.contrib.auth.models import User
from django.shortcuts import render, redirect
from django.contrib.auth.decorators import login_required
from django.contrib import admin

from apps.worktime.models import Projects, RegisterTaskWork, UserDestinations

import Utils.EnviarCorreo.LibrarySendMail as EMAIL_G
import config.settings as SETT


@login_required(login_url='signin')
def home(request):
    rta = dict()

    if not request.user.has_usable_password():
        rta['status'] = 'cretePassword'
    else:
        userDestination = [ ] #UserDestinations.objects.get(user=request.user)
        #print(userDestinations)
        if len(userDestination) == 0:
            rta['status'] = 'creteDestinations'
        else:
            rta['status'] = 'startWork'

    if request.method == 'POST':
        if not request.user.has_usable_password():
            try:
                email = request.POST['email']
                password = request.POST['pass']
                user = request.user
                u = User.objects.get(username=user)
                u.set_password(password)
                u.email = email

                message_template = EMAIL_G.read_template('welcome.html')
                body = message_template.substitute(
                    APP_NAME=SETT.APP_NAME,
                    USER_USERNAME=user,
                    USER_NAME=user.first_name,
                    USER_PWD=password,
                    USER_EMAIL=email,
                    APP_URL=SETT.APP_URL)
                asunto = 'WELCOME TO {}'.format(SETT.APP_NAME)

                # EMAIL_G.setRemitente(email)
                EMAIL_G.setDestinatario(u.email)
                EMAIL_G.SendMail(asunto, body)

                u.save()
                rta = {
                    'rta': 'ok'
                }
            except:
                pass
        else:
            print('Creando destinatarios de correos de usuario')

    return render(request, 'home.html', rta)


admin.site.register(Projects)
admin.site.register(RegisterTaskWork)
admin.site.register(UserDestinations)
