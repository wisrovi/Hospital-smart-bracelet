from django import forms

from apps.baliza.models import Sede, Piso


class PackBraceletForm(forms.Form):
    key = forms.CharField(
        label='Key',
        max_length=5000,
        widget=forms.TextInput(
            attrs={'class': 'form-control', 'type': 'text', 'name': 'y_position_baliza',
                   'placeholder': 'Escribe una key', 'autocomplete': 'off'}))
    string_pack = forms.CharField(
        label='StringPack',
        max_length=5000,
        widget=forms.TextInput(
            attrs={'class': 'form-control', 'type': 'text', 'name': 'y_position_baliza',
                   'placeholder': 'Escribe un value', 'autocomplete': 'off'}))


class FiltrarGrafica(forms.Form):
    sede = forms.ModelChoiceField(
        queryset=Sede.objects.all(),
        widget=forms.Select(
            attrs={
                'class': 'form-control',
                'name': 'Sede'
            }
        )
    )

    ubicacion = forms.ModelChoiceField(
        queryset=Piso.objects.all(),
        widget=forms.Select(
            attrs={
                'class': 'form-control',
                'name': 'Piso',
                'disable': 'true'
            }
        )
    )
