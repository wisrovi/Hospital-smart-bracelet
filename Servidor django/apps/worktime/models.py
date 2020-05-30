from django.contrib.auth.models import User
from django.db import models

# Create your models here.
from django.forms import model_to_dict


class Projects(models.Model):
    project = models.CharField(max_length=100, verbose_name='Nombre Proyecto', unique=True)
    desc = models.CharField(max_length=500, null=True, blank=True, verbose_name='Descripción')

    def __str__(self):
        return self.project

    def toJSON(self):
        item = model_to_dict(self)
        return item

    class Meta:
        verbose_name = 'Proyecto'
        verbose_name_plural = 'Proyectos'
        ordering = ['id']

class UserDestinations(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE, verbose_name='Usuario')
    ip = models.GenericIPAddressField(auto_created=True, verbose_name='Ip Donde se crea el registro', blank=False, null=False)
    email = models.EmailField(blank=False, null=False, verbose_name='Correo destinatario')

    def __str__(self):
        return '{} - {}'.format(self.user, self.email)

    def toJSON(self):
        item = model_to_dict(self)
        return item

    class Meta:
        verbose_name = 'Correo Destino'
        verbose_name_plural = 'Correos Destinatarios'
        ordering = ['id']


class RegisterTaskWork(models.Model):
    meses = [
        ('Ene', 'Enero'),
        ('Feb', 'Febrero'),
        ('Mar', 'Marzo'),
        ('Abr', 'Abril'),
        ('May', 'Mayo'),
        ('Jun', 'Junio'),
        ('Jul', 'Julio'),
        ('Ago', 'Agosto'),
        ('Sep', 'Septiembre'),
        ('Oct', 'Octubre'),
        ('Nov', 'Noviembre'),
        ('Dic', 'Diciembre'),
    ]

    dias = list()
    for i in range(31):
        dias.append((str(i + 1), i + 1))

    horas = list()
    for i in range(24):
        cuartos_hora = [15, 30, 45]
        for j in cuartos_hora:
            opcion_hora = str(i) + ":" + str(j)
            horas.append((opcion_hora, opcion_hora))

    user = models.ForeignKey(User, on_delete=models.CASCADE, verbose_name='Usuario')
    dateStart = models.DateTimeField(verbose_name='Fecha Inicio', null=True, blank=True)
    project = models.ForeignKey(Projects, on_delete=models.CASCADE, verbose_name='Proyecto')
    tasks = models.TextField(max_length=500, null=True, blank=True, verbose_name='Tareas realizadas', default="")
    dateEnd = models.DateTimeField(verbose_name='Fecha Fin', null=True, blank=True)

    def __str__(self):
        rta = "{} - {} - {}"
        return rta.format(self.project ,self.tasks, self.dateEnd)

    class Meta:
        verbose_name = 'Tarea realizada'
        verbose_name_plural = 'Tareas Realizadas'
        ordering = ['id']
