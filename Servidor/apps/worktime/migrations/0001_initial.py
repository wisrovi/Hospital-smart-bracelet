# Generated by Django 3.0.5 on 2020-05-04 10:08

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
    ]

    operations = [
        migrations.CreateModel(
            name='Projects',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('project', models.CharField(max_length=100, unique=True, verbose_name='Nombre Proyecto')),
                ('desc', models.CharField(blank=True, max_length=500, null=True, verbose_name='Descripción')),
            ],
            options={
                'verbose_name': 'Proyecto',
                'verbose_name_plural': 'Proyectos',
                'ordering': ['id'],
            },
        ),
        migrations.CreateModel(
            name='UserDestinations',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('ip', models.GenericIPAddressField(auto_created=True, verbose_name='Ip Donde se crea el registro')),
                ('email', models.EmailField(max_length=254, verbose_name='Correo destinatario')),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL, verbose_name='Usuario')),
            ],
            options={
                'verbose_name': 'Correo Destino',
                'verbose_name_plural': 'Correos Destinatarios',
                'ordering': ['id'],
            },
        ),
        migrations.CreateModel(
            name='RegisterTaskWork',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('dateStart', models.DateTimeField(blank=True, null=True, verbose_name='Fecha Inicio')),
                ('tasks', models.TextField(blank=True, default='', max_length=500, null=True, verbose_name='Tareas realizadas')),
                ('dateEnd', models.DateTimeField(blank=True, null=True, verbose_name='Fecha Fin')),
                ('project', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='worktime.Projects', verbose_name='Proyecto')),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL, verbose_name='Usuario')),
            ],
            options={
                'verbose_name': 'Tarea realizada',
                'verbose_name_plural': 'Tareas Realizadas',
                'ordering': ['id'],
            },
        ),
    ]