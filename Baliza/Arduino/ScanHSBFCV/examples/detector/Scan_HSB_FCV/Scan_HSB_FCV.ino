String mensajeEnviar = "{'loro':'periquito'}";

#include "ScanHSBFCV.h"

BalizaHSBFCV balizahsbfcv;
ScanHSBFCV scanhsbfcv;

void setup() {
  Serial.begin(9600);
  scanhsbfcv.InitSystem();
  balizahsbfcv.start_at();
}

long timeScanBeacon = 0;
void loop() {
  if (  (millis() - timeScanBeacon) > 3000  ) {
    timeScanBeacon = millis();
    balizahsbfcv.loopScanear();
    String Beaconsescaneados = balizahsbfcv.Totalbeacons();
    if (Beaconsescaneados.length() > 20) {
      String datos = Beaconsescaneados;
      mensajeEnviar = base64::encode(datos);
      Serial.println(Beaconsescaneados);
      
      scanhsbfcv.enviarMensajePost();
    }
//despues de que envie entre y limpie el dato anterior
  }
  scanhsbfcv.OTA();
}                             