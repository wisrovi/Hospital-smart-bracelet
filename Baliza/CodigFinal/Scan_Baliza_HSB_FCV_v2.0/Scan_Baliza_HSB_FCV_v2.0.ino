
#include "ScanBalizaHSBFCV.h"
#include "BalizaHSBFCV.h"

BalizaHSBFCV balizahsbfcv;
ScanBalizaHSBFCV scanbalizahsbfcv;

long timeScanBeacon = 0;
 
void setup() {
  Serial.begin(9600);
  scanbalizahsbfcv.InitSystemBaliza();
  balizahsbfcv.start_at();
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
  Serial.println("Setup Finalizado");
}


void loop() {
  if (  (millis() - timeScanBeacon) > 2000  ) {
    timeScanBeacon = millis();
    String Beaconsescaneados = balizahsbfcv.Totalbeacons();
    if (Beaconsescaneados.length() > 20) {
      scanbalizahsbfcv.setMensajeBaliza(Beaconsescaneados);
      Serial.println(Beaconsescaneados);
      scanbalizahsbfcv.enviarMensajePostBaliza();
      scanbalizahsbfcv.ResetESP32();
    }

  }
  scanbalizahsbfcv.OTABaliza();
  balizahsbfcv.loopScanear();
}
