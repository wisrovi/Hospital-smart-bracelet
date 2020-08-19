#ifndef ScanBalizaHSBFCV_h
#define ScanBalizaHSBFCV_h

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
#include "base64.h"
#include "Arduino.h"

class ScanBalizaHSBFCV
{

	public:
		void enviarMensajePostBaliza();
		void InitSystemBaliza();
		void OTABaliza();
		void setMensajeBaliza(String _datobaliza);

	private:

};

#endif
