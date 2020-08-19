#include "ScanBalizaHSBFCV.h"
#include "AsyncElegantOTA.h"

String _mensajeEnviarBaliza = "prueba";

AsyncWebServer server(80);


void ScanBalizaHSBFCV::enviarMensajePostBaliza() {
  if (WiFi.status() == WL_CONNECTED) { //Check WiFi connection status
    HTTPClient http;    //Declare object of class HTTPClient
    http.begin("http://192.168.1.57:8080/esp32"); //Specify request destination
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

