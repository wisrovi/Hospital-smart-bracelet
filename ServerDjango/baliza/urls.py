from django.urls import path
from baliza.views import getPackStringBaliza, setReceivedOK

app_name = 'baliza'

urlpatterns = [
    path('received/', getPackStringBaliza, name='form_received_baliza'),
    path('receivedOK/', setReceivedOK, name='form_received_baliza_ok'),
]