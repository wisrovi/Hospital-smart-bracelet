#ifndef ScanBalizaHSBFCV_h
#define ScanBalizaHSBFCV_h

#include <Arduino.h>
#include <Ticker.h>
#include <HTTPClient.h>
#include <WiFi.h>

#include <DNSServer.h>
#include <WebServer.h>
#include <WiFiManager.h>

#include <Hash.h>
#include <AsyncTCP.h>
#include <ESPAsyncWebServer.h>
#include <AsyncElegantOTA.h>

 class ScanBalizaHSBFCV
 
{
			
  public:
  
	void enviarMensajePost();
	void InitSystem();
	void OTA();
      
  private:
  
};
  
#endif


