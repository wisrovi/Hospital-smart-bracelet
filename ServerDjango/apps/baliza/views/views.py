from django.contrib.auth.decorators import login_required
from django.http import HttpResponseRedirect, JsonResponse
from django.shortcuts import render
from django.template.loader import render_to_string
from django.views.decorators.csrf import csrf_exempt

import authentication.PREFERENCES as Preferences
import apps.Util_apps.Util_braceletBLE as Utilities
from apps.baliza.forms import CreateBalizaForm
from apps.baliza.models import Piso


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
