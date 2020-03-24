#include "MasterSensorsFCV.h"   // RX=4, TX=3  y Vcc_BLE=A1

MasterSensorsFCV mastersensorsfcv;

float sbateria = 3.0; //float (1 entero y 1 decimal) ejemplo 5.2
int sppm = 60;	//int (max 3 decimales) ejemplo 105
boolean sproximidad = true; //true o false
boolean scaida = false; //true o false
float stemperatura = 30.0; //float (2 entero y 1 decimal) ejemplo 30.2

void setup() {
  Serial.begin(9600);
  mastersensorsfcv.setupBLE();
  Serial.println("setup terminado");
}

unsigned long time_1 = 0;
unsigned long time_2 = 0;

void loop() {
  mastersensorsfcv.loopBLE();
  if ((millis() - time_2) >= 200) {
    time_2 = millis();
    mastersensorsfcv.setBateria(sbateria);
    mastersensorsfcv.setPPM(sppm);
    mastersensorsfcv.setProximidad(sproximidad);
    mastersensorsfcv.setCaida(scaida);
    mastersensorsfcv.setTemperatura(stemperatura);
  }

  if ((millis() - time_1) >= 1000) {
    time_1 = millis();
    mastersensorsfcv.Inicializa_Enviar();
  }
  delay(1);
}