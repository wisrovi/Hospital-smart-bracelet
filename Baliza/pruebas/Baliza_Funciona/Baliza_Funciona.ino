String mensajeEnviar = "{'loro':'periquito'}";
#include "SalvadoTrigo.h"
#include "BalizaHSBFCV.h"
BalizaHSBFCV balizahsbfcv;


void setup() {
  Serial.begin(9600);
  InitSystem();
  balizahsbfcv.start_at();
  Serial.println(" ");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
  Serial.println("Setup Finalizado");
}

long timeScanBeacon = 0;
void loop() {
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
