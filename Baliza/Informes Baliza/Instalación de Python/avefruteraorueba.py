

from bottle import run, request, post
 
@post('/esp32')
def index():
    data = request.body.read()
    print(data) 
    return "OK"
 
run(host='192.168.1.57', port=8080, debug=True)

"""
from flask import Flask
app = Flask(__name__)
open('avefruteraorueba.py',encoding="IS0-8859-1")

@app.route("/")
def hello():
    return "Hello World!"

if __name__ == '__main__':
    app.run(host="192.168.1.57", port=8000, debug=True)
"""