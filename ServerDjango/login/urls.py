from django.urls import path
from baliza.views import getPackStringBaliza, setReceivedOK

app_name = 'login'

urlpatterns = [
    path('/', getPackStringBaliza, name='form_login'),
    path('loginOK/', setReceivedOK, name='form_login_ok'),
]