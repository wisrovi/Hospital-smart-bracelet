from bottle import run, request, post
 
@post('/esp32')
def index():
    data = request.body.read()
    print(data) 
    return "Pajarito"
 
run(host='0.0.0.0', port=2020, debug=True)
