#include "BateriaHSB.h"

void setup() {
  Serial.begin(9600);
  analogReference(INTERNAL);
  delay(10);
  SetupSensoresHSB();
  Serial.println("setup terminado");
}

void loop() {
  mastersensorsfcv.loopBLE();
  ControlSensoresHSB();
  delay(1);
}
