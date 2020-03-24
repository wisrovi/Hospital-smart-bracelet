/*Funciones de la libreria
sensorproximidadfcv.SetupSProximidad(); => Asigna el pin Vcc y Read del SProx
sensorproximidadfcv.StartSProximidad(); => Enciende el SProx e inicia el Ticker
sensorproximidadfcv.LoopSProximidad(); => Actualiza el Ticker
sensorproximidadfcv.ValorProximidad(); => Trae el dato del SProx en una variable boolean
sensorproximidadfcv.StopSProximidad(); => Apaga el sensor y detiene el Ticker
*/

#include "SensorProximidadFCV.h"   //Libreria S.Prox pinRead 9 // pinVcc 8

SensorProximidadFCV sensorproximidadfcv;

boolean sproximidad = true;

void setup() {
  Serial.begin(9600);
  sensorproximidadfcv.SetupSProximidad();
  sensorproximidadfcv.StartSProximidad();
  Serial.println("Setup finished");
}
int conteo = 0;
void loop() {
  sensorproximidadfcv.LoopSProximidad();
  sproximidad = sensorproximidadfcv.ValorProximidad();
  conteo++;
  if (conteo > 3000) {
    Serial.print("Proximidad: ");
    Serial.println(sproximidad);
    conteo = 0;
  }
  delay(1);
}
