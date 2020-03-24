/*Funciones de la libreria
  pulsocardiaco.StartSensorPPM() => Enciende el SPPM e inicia el Ticker
  pulsocardiaco.LoopSensorPPM() => Actualiza el Ticker
  pulsocardiaco.ValorSPPM() => Trae el dato del SPPM en una variable int
  pulsocardiaco.StopSensorPPM() => Apaga el sensor y detiene el Ticker
*/
#include "PulsoCardiacoFCV.h"   //Libreria PPM

#define pinLecturaSensorppm A0  //Puerto de lectura del sensor de pulso cardiaco
#define pinVccSensorppm 7      //Puerto de lectura del sensor de pulso cardiaco
#define intervaloPPM 20000

void ControlsensorPPM();
int sppm = 0;
unsigned long TSppm = 0;

PulsoCardiacoFCV pulsocardiaco (pinLecturaSensorppm, pinVccSensorppm);

Ticker TikerControlSPPM(ControlsensorPPM, 10, 4, MILLIS);

void ControlsensorPPM() {

  static bool estabilidadppm = true;
  static bool lecturappm = false;
  static bool apagadoppm = false;
  static bool sendBleppm = false;

  if (sendBleppm) {
    Serial.print("PPM: ");
    Serial.println(sppm);
    sendBleppm = false;
    estabilidadppm = true;
    TikerControlSPPM.interval(10);
  }

  if (apagadoppm) {
    pulsocardiaco.StopSensorPPM();
    apagadoppm = false;
    sendBleppm = true;
    TikerControlSPPM.interval(10);
  }

  if (lecturappm) {
    sppm = pulsocardiaco.ValorSPPM();
    lecturappm = false;
    apagadoppm = true;
    TikerControlSPPM.interval(10);
  }

  if (estabilidadppm && TikerControlSPPM.counter() == 1) {
    pulsocardiaco.StartSensorPPM();
    lecturappm = true;
    estabilidadppm = false;
    TikerControlSPPM.interval(16000);
  }
}

void setup() {
  Serial.begin(9600);
  analogReference(INTERNAL);
  Serial.println("Setud finished");
}

void loop() {
  pulsocardiaco.LoopSensorPPM();
  if (millis() > (intervaloPPM + TSppm)) {
    TSppm = millis();
    TikerControlSPPM.start();
  }
  TikerControlSPPM.update();
}