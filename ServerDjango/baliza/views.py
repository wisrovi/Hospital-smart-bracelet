from django.shortcuts import render
from django.http import HttpResponseRedirect
from django.core.mail import send_mail

import baliza.Util as BALIZ


from baliza.Form import PackBracelet
# Create your views here.


def getPackStringBaliza(request):
    if request.method == 'POST':
        form = PackBracelet(request.POST)
        if form.is_valid():
            objetos = BALIZ.UnZipPackBracelets()
            string_data = form.cleaned_data['string_pack']
            if string_data:
                listBracelets = objetos.setString(string_data)
                if len(listBracelets) > 0:
                    send_mail(
                        "asunto prueba",
                        "Hola mundo",
                        "WISROVI",
                        ["wisrovi.rodriguez@gmail.com"],
                        fail_silently=False,
                    )
                    return HttpResponseRedirect('../receivedOK')
    else:
        form = PackBracelet()
    return render(request, "receivedBaliza.html", {'form':form})

def setReceivedOK(request):
    return render(request, 'receivedOK.html', {})