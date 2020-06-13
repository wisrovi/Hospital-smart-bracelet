from django.urls import path

from apps.baliza.views.Bracelet.views import BraceletListView, BraceletCreateView
from apps.baliza.views.server.views import getPackStringBaliza, setReceivedOK
from apps.baliza.views.baliza.views import BalizaListView, BalizaCreateView

from apps.baliza.views.sede.views import SedeListView, SedeCreateView
from apps.baliza.views.piso.views import PisoListView, PisoCreateView
from apps.baliza.views.area.views import AreaListView, AreaCreateView
from apps.baliza.views.historialUbicacion.views import HistorialUbicacionListView
from apps.baliza.views.historialSensores.views import HistorialSensoresListView


app_name = 'project'

urlpatterns = [
    # Server
    path('received/', getPackStringBaliza, name='form_received_baliza'),
    path('receivedOK/', setReceivedOK, name='form_received_baliza_ok'),

    # Bracelet
    path('bracelet/list/', BraceletListView.as_view(), name='form_readlist_bracelet'),
    path('bracelet/create/', BraceletCreateView.as_view(), name='form_create_bracelet'),

    # Baliza
    path('baliza/list/', BalizaListView.as_view(), name='form_readlist_baliza'),
    path('baliza/create/', BalizaCreateView.as_view(), name='form_create_baliza'),

    # Sedes
    path('sede/list/', SedeListView.as_view(), name='form_readlist_sede'),
    path('sede/create/', SedeCreateView.as_view(), name='form_create_sede'),

    # Piso
    path('piso/list/', PisoListView.as_view(), name='form_readlist_piso'),
    path('piso/create/', PisoCreateView.as_view(), name='form_create_piso'),

    # Area
    path('area/list/', AreaListView.as_view(), name='form_readlist_area'),
    path('area/create/', AreaCreateView.as_view(), name='form_create_area'),

    # Historial
    path('historial/Ubicacion/', HistorialUbicacionListView.as_view(), name='form_read_historial_ubicacion'),
    path('historial/Sensores/', HistorialSensoresListView.as_view(), name='form_read_historial_sensores'),
]
