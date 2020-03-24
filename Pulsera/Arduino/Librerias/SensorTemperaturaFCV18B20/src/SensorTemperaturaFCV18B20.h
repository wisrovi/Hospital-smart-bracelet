#ifndef SensorTemperaturaFCV18B20_h
#define SensorTemperaturaFCV18B20_h

#include <OneWire.h> //Se importan las librerías
#include <DallasTemperature.h>

class SensorTemperaturaFCV18B20

{
  public:
    SensorTemperaturaFCV18B20();
    float ValorTemperatura1820();
    void LoopSTemperatura1820();
    void StartSTemperatura1820();
	void SetupSTemperatura1820();
	void StopSTemperatura1820();

  private:

};

#endif