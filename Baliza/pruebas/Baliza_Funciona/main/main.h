#ifndef main_main
#define main_main

String mensajeEnviar = "{'loro':'periquito'}";



#include "../Task0/SalvadoTrigo.h"
#include "BalizaHSBFCV.h"
BalizaHSBFCV balizahsbfcv;

long timeScanBeacon = 0;

void SETUP_MAIN(){
  InitSystem();
  balizahsbfcv.start_at();
  Serial.println(" ");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
  Serial.println("Setup Finalizado");
}


void LOOP_MAIN(){
  if (  (millis() - timeScanBeacon) > 2000  ) {
    timeScanBeacon = millis();
    String Beaconsescaneados = balizahsbfcv.Totalbeacons();
    Beaconsescaneados = balizahsbfcv.Totalbeacons();
    if (Beaconsescaneados.length() > 20) {
      mensajeEnviar = Beaconsescaneados;
      Serial.println(Beaconsescaneados);
      enviarMensajePost();
    }

  }
  OTA();
  balizahsbfcv.loopScanear();
}


#endif