#include "SensorProximidadFCV.h"
#include <Ticker.h>

#define pinVccProx 8
#define pinReadProx2 9

boolean sProximidad = false;
void loopSensorProximidad();

Ticker temporizadorSensorProximidad(loopSensorProximidad, 100);

void loopSensorProximidad() {
  //proceso que se ejecuta en el sensor
  int var = digitalRead(pinReadProx2);
  if (var == 1)
  {
    sProximidad = false;
  }
  else {
    sProximidad = true;
  }  
}

void SensorProximidadFCV::SetupSProximidad(){
	pinMode(pinVccProx, OUTPUT);
	pinMode(pinReadProx2, INPUT);
}

void SensorProximidadFCV::StartSProximidad(){
	digitalWrite(pinVccProx, HIGH);
	delay(10);
	sProximidad = false;
	temporizadorSensorProximidad.start();
}

void SensorProximidadFCV::StopSProximidad(){
	temporizadorSensorProximidad.stop();
	digitalWrite(pinVccProx, LOW);
}

void SensorProximidadFCV::LoopSProximidad() {
	temporizadorSensorProximidad.update();
}

boolean SensorProximidadFCV::ValorProximidad() {
  return sProximidad;
}
