from django.shortcuts import render
from django.http import JsonResponse
from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_exempt
from django.views.generic import CreateView

from django.utils import timezone

from apps.worktime.models import RegisterTaskWork, Projects
from apps.worktime.forms import RegisterTaskWorkForm

from apps.worktime.Library.SendMail import EnviarCorreoInicio, setRemitente, setDestinatario

# Create your views here.

DESTINATARIOS_CORREOS = list()
DESTINATARIOS_CORREOS.append('wisrovi.rodriguez@gmail.com')
# DESTINATARIOS_CORREOS.append('wisrovi.ivAdventure@gmail.com')
# DESTINATARIOS_CORREOS.append('wisrovi.rodriguez@gmail.com')

def IniciarTurnoTrabajo(request):
    if False:
        setRemitente(usuarioLogeado.email)
        for destino in DESTINATARIOS_CORREOS:
            setDestinatario(destino)
            if EnviarCorreoInicio():
                print("Inicio turno trabajo Correcto.")
            else:
                print("Fallo inicio turno trabajo.")

    proy_db = Projects.objects.filter()
    for i in proy_db:
        print(i.project)

    usuarioLogeado = request.user

    db = RegisterTaskWork()
    db.user = usuarioLogeado

    # db.save()

    contex = {
        'proyectos': proy_db,
        'usuarioLogeado': usuarioLogeado,
    }

    return render(request, 'TrabajoIniciado.html', contex)


class RegisterTaskWorkCreateView(CreateView):
    model = RegisterTaskWork
    form_class = RegisterTaskWorkForm
    template_name = 'TrabajoIniciado.html'

    @method_decorator(csrf_exempt)
    def dispatch(self, request, *args, **kwargs):
        print("Cargando plantilla")
        return super().dispatch(request, *args, **kwargs)

    def post(self, request, *args, **kwargs):
        data = {}
        try:
            action = request.POST['action']
            if action == 'add':
                idProject = request.POST['project']
                ahora = timezone.datetime.now()

                #Guardar en la base de datos el inicio de la sesion
                form = RegisterTaskWork()
                form.project = Projects.objects.get(id=idProject)
                form.user = request.user
                form.dateStart = ahora
                data['rta'] = form.save()

                #Envio de correo de inicio de la sesion
                setRemitente(request.user.email)
                setPassword('FC5JB6EM')
                for destino in DESTINATARIOS_CORREOS:
                    setDestinatario(destino)
                    if EnviarCorreoInicio():
                        print("Inicio turno trabajo Correcto.")
                    else:
                        print("Fallo inicio turno trabajo.")

            else:
                data['error'] = 'No ha ingresado a ninguna opción'
        except Exception as e:
            print("perico")
            data['error'] = str(e)
        return JsonResponse(data)

    def get_context_data(self, **kwargs):
        context = super().get_context_data(**kwargs)
        context['action'] = 'add'
        context['usuarioLogeado'] = 'add'
        return context
