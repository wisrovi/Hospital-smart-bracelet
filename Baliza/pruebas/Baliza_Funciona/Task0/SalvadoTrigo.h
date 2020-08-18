
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
#include "base64.h"

#include "../config.h"

AsyncWebServer server(80);

void enviarMensajePost() {
  if (WiFi.status() == WL_CONNECTED) { //Check WiFi connection status
    HTTPClient http;    //Declare object of class HTTPClient

    String url = "http://";
    url.concat(ip_server);
    url.concat(":");
    url.concat(port_server);
    url.concat(url_server);

    http.begin(url); //Specify request destination
    String mensajeEnviar64 = base64::encode(mensajeEnviar);    
    int httpCode = http.POST(mensajeEnviar64);   //Send the request
    String payload = http.getString();
    Serial.print("httpCode: ");
    Serial.println(httpCode);   //Print HTTP return code
    Serial.print("payload: ");
    Serial.println(payload);    //Print request response payload
  } else {
    Serial.println("Error in WiFi connection");
  }
}

void InitSystem() {
  Serial.println("Iniciando sistema");
  WiFiManager wifiManager;
  wifiManager.autoConnect("Baliza");

  Serial.println("Iniciando OTA");
  AsyncElegantOTA.begin(&server);    // Start ElegantOTA
  server.begin();

}

void OTA() {
  AsyncElegantOTA.loop();
}
