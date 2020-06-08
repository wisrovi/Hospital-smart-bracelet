from bottle import run, request, post
 
@post('/esp32')
def index():
    data = request.body.read()
    print(data) 
    return "OK"
 
run(host='192.168.1.1', port=2020)
