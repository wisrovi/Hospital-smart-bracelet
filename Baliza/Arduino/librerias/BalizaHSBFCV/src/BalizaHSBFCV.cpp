#include "BalizaHSBFCV.h"
#include "Arduino.h"
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


#define HM10 Serial2
char disi_result[5000];
int d1 = 0;
int i = 0;


String sedeBeacon = "";
String macBeacon1 = "";
String ppmBeacon = "";
String rssiBeacon = "";
String proximidadBeacon = "";
String caidaBeacon = "";
String temperaturaBeacon = "";
String voltajeBateria = "";
String totalbeacons = "";

//****************************
//Detecta HM10
//****************************

void BalizaHSBFCV::start_at() {
  Serial.begin(9600);
  Serial2.begin(9600);
  String c = "";
  do {
    HM10.print("AT");
    delay(500);
    if (HM10.available())
    {
      while (HM10.available())
      {
        c.concat( char(HM10.read() ) );
      }
    }
  } while (c != "OK");
  Serial.println("Detección HM10 completa.");
}

//*****************************************
//Imprime los datos recibidos
//****************************************

DynamicJsonDocument ProcesarDatos(String sedeBeacon, String voltajeBateria, String ppmBeacon, String proximidadBeacon, String caidaBeacon, String temperaturaBeacon, String rssiBeacon, String macBeacon) {
  DynamicJsonDocument ibeacon(128);
  ibeacon["SED"] = sedeBeacon;
  ibeacon["MAC"] = macBeacon;
  ibeacon["BAT"] = voltajeBateria;
  ibeacon["PPM"] = ppmBeacon;
  ibeacon["CAI"] = caidaBeacon;
  ibeacon["TEM"] = temperaturaBeacon;
  ibeacon["RSI"] = rssiBeacon;
  ibeacon["PRO"] = proximidadBeacon;
  return ibeacon;
}

//*********************************
//Escanea sensores y clasifica la información para ser enviada
//*********************************

void BalizaHSBFCV::loopScanear() {

  if (d1 == 0)  {
    Serial.println("Scanning started");
    HM10.println("AT+DISI?");
    Serial.println(" ");
    d1 = 1;
    i = 0;
  }

  if (d1 == 1) {
    if (HM10.available() ) {
      while (HM10.available()) {
        disi_result[i] = char(HM10.read());
        i++;
      }
    }
    String str = "";
    for (int j = i - 15; j <= i; j++)    {
      str = str + disi_result[j];
    }
    if ( str.indexOf("OK+DISCE") >= 0  ) {
      d1 = 2;
      Serial.println("fin escaneo");
    }
    delay(50);
  }

  if (d1 == 2)  {
    int j = 0;
    String lineaEncontrada = "";
	DynamicJsonDocument beacons(1024);
	JsonArray data = beacons.createNestedArray("beacons");
    for (j = 0; j <= i; j++)    {
      char c = disi_result[j];
      if (c != '\n') {
        lineaEncontrada = lineaEncontrada + c;
      } else {
        if (  lineaEncontrada.indexOf("OK+DISC:0000") < 0  ) {
          //si si es beacon
          if (  lineaEncontrada.indexOf("OK+DISC:4C000215:") >= 0  ) {
            lineaEncontrada.replace("OK+DISC:4C000215:", "");

            String fcv =  lineaEncontrada.substring(4, 10);
            if (fcv.indexOf("706786") >= 0 ) {
              String macBeacon = lineaEncontrada.substring(10, 22);
              String mac_Beacon = lineaEncontrada.substring(44, 56 );
              if (mac_Beacon.equals(macBeacon)) {
                sedeBeacon = lineaEncontrada.substring(0, 4);
                voltajeBateria = lineaEncontrada.substring(22, 24);
                ppmBeacon = lineaEncontrada.substring(24, 27);
                proximidadBeacon = lineaEncontrada.substring(27, 28);
                caidaBeacon = lineaEncontrada.substring(28, 29);
                temperaturaBeacon = lineaEncontrada.substring(29, 32 );
                rssiBeacon =  lineaEncontrada.substring(58);
                rssiBeacon.replace("\r","");

                DynamicJsonDocument ibea = ProcesarDatos(sedeBeacon, voltajeBateria, ppmBeacon, proximidadBeacon, caidaBeacon, temperaturaBeacon, rssiBeacon, macBeacon);
				
				data.add(ibea);
              }
            }
          }
        }
        lineaEncontrada = "";
      }
    }
	String output = "";
	serializeJson(beacons, output);
	//Serial.println(output);
	totalbeacons = output;
    d1 = 0;
    delay(1000);
  }
}

String BalizaHSBFCV::Totalbeacons() {
  return totalbeacons; 
}

  //aca empieza el codigo scanbaliza

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


