from django.urls import path
from apps.worktime.views import IniciarTurnoTrabajo, RegisterTaskWorkCreateView

app_name = 'atmosferia'

urlpatterns = [
    path('startWork/', IniciarTurnoTrabajo, name='task_start'),
    path('create/', RegisterTaskWorkCreateView.as_view(), name='task_create'),
]