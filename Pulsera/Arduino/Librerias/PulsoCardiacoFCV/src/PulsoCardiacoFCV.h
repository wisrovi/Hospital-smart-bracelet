#ifndef PulsoCardiacoFCV_h
#define PulsoCardiacoFCV_h

#include "Arduino.h"
#include <Ticker.h>


 class PulsoCardiacoFCV
 
{
	
		
  public:
  
  PulsoCardiacoFCV(int _pinSensor, int _pinVccSensor);
  void LoopSensorPPM();
  void StartSensorPPM();
  boolean isBanderaPrimerPulso();
  int ValorSPPM();
  void StopSensorPPM();
  

    
  private:
  
  void FinCheck();
  void ReadPulso();
  boolean banderaPrimerPulso = false;
  int _conteoPulsos = 0;
  int _pinSensorPPM = 0;
  boolean cambioEstado = false;
  int _pinVccSensorPPM;


};
  
#endif















