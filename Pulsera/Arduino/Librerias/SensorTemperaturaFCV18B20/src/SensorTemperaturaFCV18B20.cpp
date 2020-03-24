#include <SoftwareSerial.h>
#include "SensorTemperaturaFCV18B20.h"
#include <Ticker.h>
#include "Arduino.h"
#include <OneWire.h> //Libreria sensor Temp18b20
#include <DallasTemperature.h> //Libreria sensor Temp18b20

#define pinLecturaSensorTemp 6 //Se declara el pin donde se conectará la DATA
#define pinVccSensorTemp 5 //Pin Vcc Sensor Temperatura
OneWire ourWire(pinLecturaSensorTemp);
DallasTemperature sensors(&ourWire);
float ValorSensorTemp;
void ScanSTemperatura1820();

SensorTemperaturaFCV18B20::SensorTemperaturaFCV18B20() {
}

Ticker temporizadorSensorTemperatura(ScanSTemperatura1820, 3000);

void SensorTemperaturaFCV18B20::SetupSTemperatura1820() {
  pinMode(pinVccSensorTemp, OUTPUT);
  sensors.begin();  
}

void ScanSTemperatura1820() {
  sensors.requestTemperatures();
  ValorSensorTemp = sensors.getTempCByIndex(0);
}

void SensorTemperaturaFCV18B20::StartSTemperatura1820() {
  digitalWrite(pinVccSensorTemp, HIGH);
  delay(10);
  temporizadorSensorTemperatura.start();  
}

void SensorTemperaturaFCV18B20::LoopSTemperatura1820() {
  temporizadorSensorTemperatura.update();
}

void SensorTemperaturaFCV18B20::StopSTemperatura1820() {
  digitalWrite(pinVccSensorTemp, LOW);
  temporizadorSensorTemperatura.stop();  
}

float SensorTemperaturaFCV18B20::ValorTemperatura1820() {
  return ValorSensorTemp;
}