# Create your tests here.
from django.contrib.auth.models import User

from apps.baliza.models import Sede

data = list()
data.append('HIC')
data.append('CTE')
data.append('ICV')
data.append('Cañaveral')
data.append('Florida')
data.append('Giron')
data.append('Bucaramanga')
data.append('Piedecuesta')
data.append('Santa Marta')
data.append('bogota')
data.append('Cali')

for i in data:
    sed = Sede(nombreSede=i, descripcion="", usuarioRegistra=User.objects.all()[0])
    sed.save()
    print("Guardando registro N°{}, con valor de {}".format(sed.id, i))

