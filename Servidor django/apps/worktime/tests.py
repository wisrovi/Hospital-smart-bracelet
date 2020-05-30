from django.test import TestCase
from apps.worktime.models import Projects


# Create your tests here.
db = Projects()
db.project = 'ivAdventure'
db.desc = 'Proyecto ivAdventure'
db.save()