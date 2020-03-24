/*Funciones de la libreria
sensorcaidafcv.SetupSCaida() => Inicia las librerias del STemp
sensorcaidafcv.StartSCaida() => Enciende el SCaida e inicia el Ticker
sensorcaidafcv.LoopSCaida() => Actualiza el Ticker
sensorcaidafcv.ValorCaida() => Trae el dato del SCaida en una variable boolean
sensorcaidafcv.StopSCaida() => Apaga el sensor y detiene el Ticker
sensorcaidafcv.ResetCaida() => Reinicia la variable de caida a False
*/

#include "SensorCaidaFCV.h" //Libreria SensorCaida //PortRead EjeX A2/EjeY A3/EjeZ A4// PortVcc 10
SensorCaidaFCV sensorcaidafcv;
boolean scaida = false;

void setup() {
  Serial.begin(9600);
  sensorcaidafcv.SetupSCaida();
  Serial.println("Iniciando lectura ");
  sensorcaidafcv.StartSCaida();
  Serial.println("Setup finished");
}

void loop() {
  sensorcaidafcv.LoopSCaida();
  scaida = sensorcaidafcv.ValorCaida();
  if(scaida){
  Serial.print("Sensor Caida: ");
  Serial.println(scaida);
  sensorcaidafcv.ResetCaida();
  }
}