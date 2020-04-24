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
#include "Arduino.h"


AsyncWebServer server(80);

void ScanBalizaHSBFCV::enviarMensajePost() {
  if (WiFi.status() == WL_CONNECTED) { //Check WiFi connection status
    HTTPClient http;    //Declare object of class HTTPClient
    http.begin("http://172.30.19.88:2525/");      //Specify request destination
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

void ScanBalizaHSBFCV::InitSystem() {
  WiFiManager wifiManager;
  wifiManager.autoConnect("AutoConnectAP");


  AsyncElegantOTA.begin(&server);    // Start ElegantOTA
  server.begin();

}


void ScanBalizaHSBFCV::OTA(){
  AsyncElegantOTA.loop(); 
}

