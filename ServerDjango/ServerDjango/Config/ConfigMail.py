#from django.core.mail import send_mail
#EXAMPLE:
# send_mail(
#     "asunto prueba",
#     "Hola mundo",
#     "WISROVI",
#     ["wisrovi.rodriguez@gmail.com"],
#     fail_silently=False,
# )

EMAIL_BACKEND = 'django.core.mail.backends.smtp.EmailBackend'
EMAIL_HOST = 'smtp.gmail.com'
EMAIL_PORT = 587
EMAIL_HOST_USER = 'wisrovi.rodriguez@gmail.com'
EMAIL_HOST_PASSWORD = 'FC5JB6EM'
EMAIL_USE_TLS = True
EMAIL_USE_SSL = False