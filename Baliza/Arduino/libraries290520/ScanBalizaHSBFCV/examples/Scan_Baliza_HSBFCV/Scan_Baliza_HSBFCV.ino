String mensajeEnviar = "{'loro':'periquito'}";

#include "ScanBalizaHSBFCV.h"
#include "BalizaHSBFCV.h"

BalizaHSBFCV balizahsbfcv;
ScanBalizaHSBFCV scanbalizahsbfcv;


void setup() {
  Serial.begin(9600);
  scanbalizahsbfcv.InitSystem();
  balizahsbfcv.start_at();
}

long timeScanBeacon = 0;

void loop() {
  if (  (millis() - timeScanBeacon) > 5000  ) {
    timeScanBeacon = millis();
    balizahsbfcv.loopScanear();
    String Beaconsescaneados = balizahsbfcv.Totalbeacons();
    if (Beaconsescaneados.length() > 20) {
      String datos = Beaconsescaneados;
      mensajeEnviar = base64::encode(datos);
  
      scanbalizahsbfcv.enviarMensajePost();
    }

  }
  scanbalizahsbfcv.OTA();
}
