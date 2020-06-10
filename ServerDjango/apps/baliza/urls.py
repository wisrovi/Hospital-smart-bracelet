from django.urls import path
from apps.baliza.views.views_server import getPackStringBaliza, setReceivedOK
from apps.baliza.views.views import createBaliza

app_name = 'baliza'

urlpatterns = [
    path('received/', getPackStringBaliza, name='form_received_baliza'),
    path('receivedOK/', setReceivedOK, name='form_received_baliza_ok'),
    path('createbaliza/', createBaliza, name='form_create_baliza'),
]