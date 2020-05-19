#ifndef BalizaHSBFCV_h
#define BalizaHSBFCV_h

#include "Arduino.h"
#include <ArduinoJson.h>
#include <HTTPClient.h>
#include <WiFi.h>
#include <DNSServer.h>
#include <WebServer.h>
#include <WiFiManager.h>
#include <Hash.h>
#include <AsyncTCP.h>
#include <ESPAsyncWebServer.h>
#include <AsyncElegantOTA.h>

class BalizaHSBFCV
{

  public:
    void start_at();
    void loopScanear();
    String Totalbeacons();
    void enviarMensajePost();
	void InitSystem();
	void OTA();

	private:

};

#endif
