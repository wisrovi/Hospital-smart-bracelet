from bottle import run, request, post
 
@post('/esp32')
def index():
    data = request.body.read()
    print(data) 
    return "Pajarito"

run(host='localhost', port=2020)
