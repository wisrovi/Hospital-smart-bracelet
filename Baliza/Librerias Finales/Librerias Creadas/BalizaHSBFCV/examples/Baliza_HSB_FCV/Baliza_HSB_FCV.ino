#include "BalizaHSBFCV.h"

BalizaHSBFCV balizahsbfcv;

String Beaconsescaneados = "";

void setup() {
  Serial.begin(9600);
  balizahsbfcv.start_at();
  Serial.println(" ");
  Serial.println( " setup terminado " );
}

void loop() {
  balizahsbfcv.loopScanear();
  Beaconsescaneados = balizahsbfcv.Totalbeacons();
  if (Beaconsescaneados.length() > 20) {
    Serial.println(Beaconsescaneados);
  }
  delay(2000);
}