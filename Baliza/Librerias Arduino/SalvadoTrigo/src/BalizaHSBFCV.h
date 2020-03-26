#ifndef SalvadoTrigo_h
#define SalvadoTrigo_h

#include <HTTPClient.h>
#include <WiFi.h>
#include <DNSServer.h>
#include <WebServer.h>
#include <WiFiManager.h>
#include <Hash.h>
#include <AsyncTCP.h>
#include <ESPAsyncWebServer.h>
#include <AsyncElegantOTA.h>
#include "BalizaHSBFCV.h"
#include "base64.h"


class SalvadoTrigo
{

  public:
     void enviarMensajePost();
    void InitSystem();
    void OTA();

	private:

};

#endif
