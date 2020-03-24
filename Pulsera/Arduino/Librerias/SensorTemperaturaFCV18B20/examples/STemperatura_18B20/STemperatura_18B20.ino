/*Funciones de la libreria
sensortemperaturafcv18B20.SetupSTemperatura1820() => Inicia las librerias del STemp
sensortemperaturafcv18B20.StartSTemperatura1820() => Enciende el sensor de temperatura e inicia el Ticker
sensortemperaturafcv18B20.LoopSTemperatura1820() => Actualiza el Ticker
sensortemperaturafcv18B20.ValorTemperatura1820() => Trae el dato de la temperatura en una variable float
sensortemperaturafcv18B20.StopSTemperatura1820() => Apaga el sensor y detiene el Ticker
*/
#include "SensorTemperaturaFCV18B20.h"   // Libreria STemp PinRead 6/ PinVcc 5

SensorTemperaturaFCV18B20 sensortemperaturafcv18B20;

float stemperatura = 0;

void setup() {
  Serial.begin(9600);
  Serial.println("Iniciando Setup");
  sensortemperaturafcv18B20.SetupSTemperatura1820();
  sensortemperaturafcv18B20.StartSTemperatura1820();
  Serial.println("setup terminado");
}

void loop() {
  sensortemperaturafcv18B20.LoopSTemperatura1820();
  stemperatura = sensortemperaturafcv18B20.ValorTemperatura1820();
  Serial.print("Temperatura es de: ");
  Serial.print(stemperatura);
  Serial.println(" grados Centigrados");
  delay(1000);
}