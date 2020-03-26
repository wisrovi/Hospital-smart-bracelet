from bottle import run, request, post
 
@post('/')
def index():
    data = request.body.read()
    print(data) 
    return "aveFrutera"
 
run(host='0.0.0.0', port=2020, debug=True)
