from django import forms

class PackBracelet(forms.Form):
    key = forms.CharField(label='Key', max_length=5000)
    string_pack = forms.CharField(label='StringPack', max_length=5000)
