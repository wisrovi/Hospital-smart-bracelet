from django.http import JsonResponse
from django.shortcuts import render

from django.contrib.auth.decorators import login_required

from apps.baliza.views.baliza.forms import CreateBalizaForm
from apps.baliza.models import Piso
import authentication.Config.PREFERENCES as Preferences

@login_required(login_url='signin')
def createBaliza(request):

    forms = CreateBalizaForm()
    if request.method == 'POST':
        data = dict()
        forms = CreateBalizaForm(request.POST)
        if forms.is_valid():
            pass

        return JsonResponse(data, safe=False)

    context = dict()
    context['form'] = forms
    context['title'] = Preferences.NAME_APP

    if Piso.objects.all().count() == 0:
        context['error'] = 'No hay una ubicación creada para instalar la Baliza, por favor cree una ubicación.'
    return render(request, 'FORM.html', context)
