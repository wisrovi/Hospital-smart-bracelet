from django import forms

from apps.baliza.models import Piso


class PackBracelet(forms.Form):
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


class CreateBalizaForm(forms.Form):
    mac_baliza = forms.CharField(
        label='MAC Baliza',
        max_length=17,
        widget=forms.TextInput(
            attrs={'class': 'form-control', 'type': 'text', 'name': 'mac_baliza',
                   'placeholder': 'Escribe la MAC de la Baliza', 'autocomplete': 'off'}))
    description_baliza = forms.CharField(
        label='Descripción Baliza',
        max_length=17,
        widget=forms.TextInput(
            attrs={'class': 'form-control', 'type': 'text', 'name': 'description_baliza',
                   'placeholder': 'Escribe una descripción para la Baliza', 'autocomplete': 'off'}))
    x_position_baliza = forms.CharField(
        label='Posición en X para la Baliza',
        widget=forms.TextInput(
            attrs={'class': 'form-control', 'type': 'text', 'name': 'x_position_baliza',
                   'placeholder': 'Escribe una posición en X para la Baliza', 'autocomplete': 'off'}))
    y_position_baliza = forms.CharField(
        label='Posición en Y para la Baliza',
        widget=forms.TextInput(
            attrs={'class': 'form-control', 'type': 'text', 'name': 'y_position_baliza',
                   'placeholder': 'Escribe una posición en Y para la Baliza', 'autocomplete': 'off'}))
    piso_instalacion_baliza = forms.ModelChoiceField(
        queryset=Piso.objects.all(),
        widget=forms.Select(
            attrs={'class': 'form-control',
                   'name': 'piso_instalacion_baliza',}))
