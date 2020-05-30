from django import forms

class PackBracelet(forms.Form):
    string_pack = forms.CharField(label='string pack bracelet', max_length=5000)
