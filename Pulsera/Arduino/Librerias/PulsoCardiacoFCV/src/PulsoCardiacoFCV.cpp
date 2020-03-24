#include "PulsoCardiacoFCV.h"
#include <Ticker.h>

int _pinSensorPPM;
int _pinVccSensorPPM;
#define _constanteLectura 15000 //Tiempo establecido para tomar la cantidad de pulsos.
int _conteoPulsos = 0;
int _pulsosAnterior = 0;


void FinCheck();
void ReadPulso();
bool banderaControl = false;

PulsoCardiacoFCV::PulsoCardiacoFCV(int _pinSensor, int _pinVccSensor) {
  _pinSensorPPM = _pinSensor;
  _pinVccSensorPPM = _pinVccSensor;
  pinMode(_pinVccSensorPPM, OUTPUT);
}

boolean banderaPrimerPulso = false;
Ticker CheckPulso(FinCheck, _constanteLectura);


//Entrega la cantidad final de pulsos
void FinCheck() {
  if (_conteoPulsos > 10) {
    _pulsosAnterior = _conteoPulsos;
  }
  _conteoPulsos = 0;

  banderaPrimerPulso = false;
  CheckPulso.stop();
  banderaControl = false;
}

Ticker LectorPulso(ReadPulso, 20);
boolean cambioEstado = false;

//Establece el conteo de pulsos
void ReadPulso() {
  if (banderaControl) {
    int heartValue = analogRead(_pinSensorPPM);
    if (heartValue > 1000) {
      if ( !cambioEstado) {
        cambioEstado = true;
        _conteoPulsos++;
      }
      if (!banderaPrimerPulso) {
        banderaPrimerPulso = true;
        CheckPulso.start();
      }
    } else {
      cambioEstado = false;
    }
  }

}

//establece el pin de lectura y la cantidad de tiempo para tomar los pulsos

void PulsoCardiacoFCV::StartSensorPPM() {
  digitalWrite(_pinVccSensorPPM, HIGH);
  delay(10);
  LectorPulso.start();
  banderaControl = true;
}

void PulsoCardiacoFCV::LoopSensorPPM() {
  CheckPulso.update();
  LectorPulso.update();
}

void PulsoCardiacoFCV::StopSensorPPM() {
  CheckPulso.stop();
  LectorPulso.stop();
  digitalWrite(_pinVccSensorPPM, LOW);
}

boolean PulsoCardiacoFCV::isBanderaPrimerPulso() {
  return banderaPrimerPulso;
}

int PulsoCardiacoFCV::ValorSPPM() {
  int producto = 60000 / _constanteLectura;
  return _pulsosAnterior * producto;
}
