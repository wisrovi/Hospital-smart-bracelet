String mensajeEnviar = "{'loro':'periquito'}";

#include "SalvadoTrigo.h"
#include "BalizaHSBFCV.h"
BalizaHSBFCV balizahsbfcv;
#include "base64.h"

void setup() {
  Serial.begin(9600);
  InitSystem();
  balizahsbfcv.start_at();
  Serial.println("finaliza setup");
}

long timeScanBeacon = 0;
void loop() {
  if (  (millis() - timeScanBeacon) > 5000  ) {
    timeScanBeacon = millis();
    balizahsbfcv.loopScanear();
    Serial.println("escaneando.");
    String Beaconsescaneados = balizahsbfcv.Totalbeacons();
  }
  OTA();
}
