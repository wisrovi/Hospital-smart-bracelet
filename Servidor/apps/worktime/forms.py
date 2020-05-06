from django.forms import *
from apps.worktime.models import RegisterTaskWork


class RegisterTaskWorkForm(ModelForm):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        # for form in self.visible_fields():
        #     form.field.widget.attrs['class'] = 'form-control'
        #     form.field.widget.attrs['autocomplete'] = 'off'
        #self.fields['name'].widget.attrs['autofocus'] = True

    class Meta:
        model = RegisterTaskWork
        fields = '__all__'
        widgets = {
            'project': Select(
                attrs={
                    'placeholder': 'Elija un proyecto',
                }
            ),
        }

    def save(self, commit=True):
        data = {}
        form = super()
        try:
            if form.is_valid():
                form.save()
            else:
                data['error'] = form.errors
        except Exception as e:
            data['error'] = str(e)
        return data

    def clean(self):
        cleaned = super().clean()
        if len(cleaned['project']) <= 50:
            raise forms.ValidationError('Validacion xxx')
            #self.add_error('name', 'Le faltan caracteres')
        return cleaned


