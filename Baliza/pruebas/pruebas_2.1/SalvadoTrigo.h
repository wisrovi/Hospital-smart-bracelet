/***
  para hacer post
***/
#include <HTTPClient.h>
#include <WiFi.h>
/***
  para wifi manager
***/
#include <DNSServer.h>
#include <WebServer.h>
#include <WiFiManager.h>
/****
  para OTA
***/
#include <Hash.h>
#include <AsyncTCP.h>
#include <ESPAsyncWebServer.h>
#include <AsyncElegantOTA.h>

AsyncWebServer server(80);

void enviarMensajePost() {
  if (WiFi.status() == WL_CONNECTED) { //Check WiFi connection status
    HTTPClient http;    //Declare object of class HTTPClient
    http.begin("http://192.168.1.2:2020/esp32"); //Specify request destination
    http.header("POST / HTTP/1.1");
    http.header("Host: server_name");
    http.header("Accept: */*");
    int httpCode = http.POST(mensajeEnviar);   //Send the request
    String payload = http.getString();
    Serial.println(httpCode);   //Print HTTP return code
    Serial.println(payload);    //Print request response payload
    http.end();  //Close connection
  } else {
    Serial.println("Error in WiFi connection");
  }
}

void InitSystem() {
  WiFiManager wifiManager;
  wifiManager.autoConnect("AutoConnectAP");

  AsyncElegantOTA.begin(&server);    // Start ElegantOTA
  server.begin();

}

void OTA() {
  AsyncElegantOTA.loop();
}
