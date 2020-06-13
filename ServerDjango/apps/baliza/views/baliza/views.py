from django.contrib.auth.decorators import login_required
from django.http import JsonResponse
from django.urls import reverse_lazy
from django.utils.decorators import method_decorator
from django.views.generic import ListView, FormView

from apps.baliza.models import InstalacionBaliza, Baliza
from apps.baliza.views.baliza.forms import CreateBalizaForm


@method_decorator(login_required(login_url='signin'), name='dispatch')
class BalizaCreateView(FormView):
    form_class = CreateBalizaForm
    template_name = 'FORM.html'
    success_url = reverse_lazy('project:form_readlist_baliza')

    def post(self, request, *args, **kwargs):
        data = dict()
        try:
            action = request.POST['action']
            if action == 'add':
                forms = CreateBalizaForm(request.POST)
                if forms.is_valid():
                    mac_baliza = forms.cleaned_data['mac_baliza']
                    description_baliza = forms.cleaned_data['description_baliza']
                    x_position_baliza = forms.cleaned_data['x_position_baliza']
                    y_position_baliza = forms.cleaned_data['y_position_baliza']
                    piso_instalacion_baliza = forms.cleaned_data['piso_instalacion_baliza']

                    if len(mac_baliza) == 17:
                        hayError = False
                        try:
                            baliza = Baliza()
                            baliza.macDispositivoBaliza = mac_baliza
                            baliza.descripcion = description_baliza
                            baliza.usuarioRegistra = request.user
                            baliza.save()
                        except:
                            hayError = True
                            data['error'] = 'La MAC ingresada ya existe para una baliza, no se pudo registrar'

                        if hayError == False:
                            baliza = Baliza.objects.get(macDispositivoBaliza=mac_baliza)

                            instalacion_baliza = InstalacionBaliza()
                            instalacion_baliza.baliza = baliza
                            instalacion_baliza.usuarioRegistra = request.user
                            instalacion_baliza.instalacionX = x_position_baliza
                            instalacion_baliza.instalacionY = y_position_baliza
                            instalacion_baliza.piso = piso_instalacion_baliza
                            instalacion_baliza.save()

                            data['redirec'] = reverse_lazy('project:form_readlist_baliza')
                    else:
                        data['error'] = 'La MAC ingresada no es valida'
                else:
                    data['error'] = 'Error en datos, favor intentelo de nuevo'
            else:
                data['error'] = 'No ha ingresado a ninguna opción'
        except Exception as e:
            data['error'] = str(e)
        return JsonResponse(data)

    def get_context_data(self, **kwargs):
        context = super().get_context_data(**kwargs)
        context['title'] = 'Crear una Baliza'
        context['action'] = 'add'
        context['entity'] = 'Crear Baliza'
        return context


@method_decorator(login_required(login_url='signin'), name='dispatch')
class BalizaListView(ListView):
    model = InstalacionBaliza
    template_name = 'Baliza/BalizaListView.html'

    def dispatch(self, request, *args, **kwargs):
        # todo lo que sucede antes que se cargue la web por primera vez
        # por ejemplo, comprobar que el rol del user le permite ver la lista
        print(request.user)
        return super().dispatch(request, *args, **kwargs)

    def post(self, request, *args, **kwargs):
        data = dict()
        try:
            data['name'] = 'William'
        except Exception as e:
            data['error'] = str(e)
        return JsonResponse(data)

    def get_context_data(self, **kwargs):
        context = super().get_context_data(**kwargs)
        context['title'] = 'Listado Balizas'
        context['list_url'] = reverse_lazy('project:form_create_baliza')
        context['create_url'] = reverse_lazy('project:form_create_baliza')
        context['entity'] = 'Balizas'
        return context
