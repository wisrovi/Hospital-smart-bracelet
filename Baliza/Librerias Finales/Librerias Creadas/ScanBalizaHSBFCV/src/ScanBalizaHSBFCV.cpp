#include "ScanBalizaHSBFCV.h"
#include "AsyncElegantOTA.h"

String _mensajeEnviarBaliza = "prueba";

AsyncWebServer server(80);

#define server_url  "http://190.131.247.226:5000"
#define route_server  "/esp32"

void ScanBalizaHSBFCV::enviarMensajePostBaliza() {
  if (WiFi.status() == WL_CONNECTED) { //Check WiFi connection status
    HTTPClient http;    //Declare object of class HTTPClient
	
	String url = server_url;
    url.concat(route_server);

    http.begin(url); //Specify request destination
	
    String mensajeEnviar64 = base64::encode(_mensajeEnviarBaliza);    
    int httpCodeBaliza = http.POST(mensajeEnviar64);   //Send the request
    String payloadBaliza = http.getString();
    Serial.print("httpCode: ");
    Serial.println(httpCodeBaliza);   //Print HTTP return code
    Serial.print("payload: ");
    Serial.println(payloadBaliza);    //Print request response payload
  } else {
    Serial.println("Error in WiFi connection");
  }
}

void ScanBalizaHSBFCV::InitSystemBaliza() {
  WiFiManager wifiManager;
  wifiManager.autoConnect("AutoConnectAP");

  AsyncElegantOTA.begin(&server);    // Start ElegantOTA
  server.begin();

}

void ScanBalizaHSBFCV::OTABaliza() {
  AsyncElegantOTA.loop();
}

void ScanBalizaHSBFCV::setMensajeBaliza(String _datobaliza) {
  _mensajeEnviarBaliza = _datobaliza;
}

