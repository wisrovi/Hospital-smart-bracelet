# Generated by Django 3.0.5 on 2020-05-30 01:48

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('baliza', '0005_auto_20200529_2044'),
    ]

    operations = [
        migrations.AlterField(
            model_name='piso',
            name='piso',
            field=models.PositiveIntegerField(verbose_name='Piso'),
        ),
    ]