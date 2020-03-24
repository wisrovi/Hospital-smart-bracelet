#include "BateriaHSB.h"

void setup() {
  Serial.begin(9600);
  SetupSensoresHSB();
  Serial.println("setup terminado");
}

void loop() {
  mastersensorsfcv.loopBLE();
  ControlSensoresHSB();
  mastersensorsfcv.setBateria(sbateria);
  delay(1);
}
