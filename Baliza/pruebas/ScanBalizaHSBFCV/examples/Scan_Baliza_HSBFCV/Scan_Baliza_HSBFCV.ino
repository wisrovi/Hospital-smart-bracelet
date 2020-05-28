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
   /*   
 String stringBeaconsescaneados = "<html><head><body>";
  Serial.println(stringBeaconsescaneados);
  // replace() changes all instances of one substring with another:
  // first, make a copy of the original string:
  String stringTwo = stringBeaconsescaneados;
  // then perform the replacements:
  stringTwo.replace("<", "</");
  // print the original:
  Serial.println("Original string: " + stringBeaconsescaneados);
  // and print the modified string:
  Serial.println("Modified string: " + stringTwo);*/

      scanbalizahsbfcv.enviarMensajePost();
    }

  }
  scanbalizahsbfcv.OTA();
}