#include "SensorTemperaturaFCV18B20.h"   //Libreria S.Temp pinRead 6 // pinVcc 5
#include "SensorProximidadFCV.h"   //Libreria S.Prox pinRead 9 // pinVcc 8
#include "PulsoCardiacoFCV.h"   //Libreria PPM
#include "SensorCaidaFCV.h" //Libreria S.Caida PortRead EjeX A2/EjeY A3/EjeZ A4 PortVcc 10
#include "MasterSensorsFCV.h"   // Libreria envio de datos BLE HM10 RX=4, TX=3  y Vcc_BLE=A1

#define pinLecturaSensorppm A0  //Puerto de lectura del sensor de pulso cardiaco
#define pinVccSensorppm 7      //Puerto de lectura del sensor de pulso cardiaco

PulsoCardiacoFCV pulsocardiaco (pinLecturaSensorppm, pinVccSensorppm);
MasterSensorsFCV mastersensorsfcv;
SensorTemperaturaFCV18B20 sensortemperaturafcv18B20;
SensorProximidadFCV sensorproximidadfcv;
SensorCaidaFCV sensorcaidafcv;

//Variables datos sensores
float sbateria = 0; //float (1 entero y 1 decimal) ejemplo 5.2
int sppm = 0; //int (max 3 decimales) ejemplo 105
boolean sproximidad = false; //true o false
boolean scaida = false; //true o false
float stemperatura = 0; //float (2 entero y 1 decimal) ejemplo 30.2
boolean cargabateria = false;

void ServicioBateria() {
  int valor = digitalRead(2);
  if (valor == 0) {
    cargabateria = false;
    digitalWrite(A1, HIGH);
  } else {
    digitalWrite(A1, LOW);
    cargabateria = true;
  }
}

//Variables definición de tiempo para los sensores
unsigned long TSTemp = 0;
unsigned long TSProx = 0;
unsigned long TSppm = 0;
unsigned long TSAcel = 0;
unsigned long TSBat = 0;
unsigned long TMble = 0;

#define intervaloP 5000
#define intervaloT 8000  //600000
#define intervaloPPM 30000
#define intervaloAcel 500
#define intervaloAcel2 5000
#define intervaloBAT 3000
#define intervaloble 1000

//Declaración de funciones
void ControlsensorTemperatura();
void ControlsensorProximidad();
void ControlsensorPPM();
void ControlsensorAcel();
void ControlBat();
void ControlModuloBLE();

//Ticker realizados
Ticker TikerControlSProx(ControlsensorProximidad, 10, 4, MILLIS);
Ticker TikerControlSTemp(ControlsensorTemperatura, 10, 4, MILLIS);
Ticker TikerControlSPPM(ControlsensorPPM, 10, 4, MILLIS);
Ticker TikerControlSAcel(ControlsensorAcel, 10, 4, MILLIS);
Ticker TikerControlBat(ControlBat, 1000, 1, MILLIS);
Ticker TikerControlModuloBLE(ControlModuloBLE, 10, 1, MILLIS);

//****************************************
//Funcion control del Sensor de Proximidad
//****************************************
void ControlsensorProximidad() {
  static bool estabilidadprox = true;
  static bool lecturaprox = false;
  static bool apagadoprox = false;
  static bool sendBleprox = false;

  if (sendBleprox) {
    mastersensorsfcv.setProximidad(sproximidad);
    sendBleprox = false;
    estabilidadprox = true;
    if (!sproximidad) {
      Serial.println("No hay proximidad");
    }
    TikerControlSProx.interval(10);
  }

  if (apagadoprox) {
    sensorproximidadfcv.StopSProximidad();
    apagadoprox = false;
    sendBleprox = true;
    TikerControlSProx.interval(10);
  }

  if (lecturaprox) {
    sproximidad = sensorproximidadfcv.ValorProximidad();
    lecturaprox = false;
    apagadoprox = true;
    TikerControlSProx.interval(10);
  }

  if (estabilidadprox && TikerControlSProx.counter() == 1) {
    sensorproximidadfcv.StartSProximidad();
    estabilidadprox = false;
    lecturaprox = true;
    TikerControlSProx.interval(70);
  }
}
//*****************************************
//Funcion control del Sensor de Temperatura
//*****************************************
void ControlsensorTemperatura() {

  static bool estabilidadtemp = true;
  static bool lecturatemp = false;
  static bool apagadotemp = false;
  static bool sendBletemp = false;

  if (sendBletemp) {
    mastersensorsfcv.setTemperatura(stemperatura);
    sendBletemp = false;
    estabilidadtemp = true;
    TikerControlSTemp.interval(10);

  }

  if (apagadotemp) {
    sensortemperaturafcv18B20.StopSTemperatura1820();
    apagadotemp = false;
    sendBletemp = true;
    TikerControlSTemp.interval(10);
  }

  if (lecturatemp) {
    stemperatura = sensortemperaturafcv18B20.ValorTemperatura1820();
    lecturatemp = false;
    apagadotemp = true;
    TikerControlSTemp.interval(10);
  }

  if (estabilidadtemp && TikerControlSTemp.counter() == 1) {
    sensortemperaturafcv18B20.StartSTemperatura1820();
    estabilidadtemp = false;
    lecturatemp = true;
    TikerControlSTemp.interval(7000);
  }
}
//*****************************************
//Funcion control del Sensor de PPM
//*****************************************
void ControlsensorPPM() {

  static bool estabilidadppm = true;
  static bool lecturappm = false;
  static bool apagadoppm = false;
  static bool sendBleppm = false;

  if (sendBleppm) {
    mastersensorsfcv.setPPM(sppm);
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
//*****************************************
//Funcion control del Sensor de Acelerometro
//*****************************************
void ControlsensorAcel() {

  static bool estabilidadacel = true;
  static bool lecturaacel = false;
  static bool apagadoacel = false;
  static bool sendBleacel = false;

  if (sendBleacel) {
    mastersensorsfcv.setCaida(scaida);
    sendBleacel = false;
    estabilidadacel = true;
    sensorcaidafcv.ResetCaida();
    TikerControlSAcel.interval(10);
  }

  if (apagadoacel) {
    sensorcaidafcv.StopSCaida();
    apagadoacel = false;
    sendBleacel = true;
    TikerControlSAcel.interval(10);
  }

  if (lecturaacel) {
    scaida = sensorcaidafcv.ValorCaida();
    lecturaacel = false;
    apagadoacel = true;
    TikerControlSAcel.interval(10);
  }

  if (estabilidadacel && TikerControlSAcel.counter() == 1) {
    sensorcaidafcv.StartSCaida();
    lecturaacel = true;
    estabilidadacel = false;
    TikerControlSAcel.interval(300);
  }
}
//*****************************************
//Funcion control publicacion estado de batería
//*****************************************
void ControlBat() {
  int Readbateria = analogRead(A5);
  float sbateria3 = (Readbateria * 3.3) / 1024; //Se realiza la lectura del voltaje en 10 bits (0-1024) en el puerto A5, luego con la regla de tres calculamos el voltaje actual.
  float sbateria2 = (sbateria3 * 100) / 3.3;
  sbateria = (sbateria2 * 4.1) / 100;
  mastersensorsfcv.setBateria(sbateria);
}
//*****************************************
//Funcion control Modulo BLE
//*****************************************
void ControlModuloBLE() {
  mastersensorsfcv.Inicializa_Enviar();
}

//Funcion que inicia mastersensors y asigna los puertos Vcc y Read de los sensores
void SetupSensoresHSB() {
  mastersensorsfcv.setupBLE();
  sensorproximidadfcv.SetupSProximidad();
  sensortemperaturafcv18B20.SetupSTemperatura1820();
  sensorcaidafcv.SetupSCaida();
  mastersensorsfcv.Inicializa_Enviar();
  attachInterrupt( 0, ServicioBateria, CHANGE);
}

//Funcion Loop de los sensores
void ControlSensoresHSB() {

  sensorproximidadfcv.LoopSProximidad();
  sensortemperaturafcv18B20.LoopSTemperatura1820();
  pulsocardiaco.LoopSensorPPM();
  sensorcaidafcv.LoopSCaida();
  mastersensorsfcv.loopBLE();

  if (!cargabateria) {

    //Define el tiempo que se ejecutara el modulo BLE
    if (millis() > 2000) {
      if (millis() > (intervaloble + TMble)) {
        TMble = millis();
        TikerControlModuloBLE.start();
      }
    }

    //Define el tiempo que se ejecutara el sensor de Proximidad
    if (millis() > (intervaloP + TSProx)) {
      TSProx = millis();
      TikerControlSProx.start();
    }

    //Define el tiempo que se ejecutara el sensor de temperatura
    if (sproximidad) {
      if (millis() > (intervaloT + TSTemp)) {
        TSTemp = millis();
        TikerControlSTemp.start();
      }
    }

    //Define el tiempo que se ejecutara el sensor de PPM
    if (sproximidad) {
      if (millis() > (intervaloPPM + TSppm)) {
        TSppm = millis();
        TikerControlSPPM.start();
      }
    }

    //Define el tiempo que se ejecutara el sensor de Caida-Acelerometro
    if (sproximidad) {
      if (!scaida) {
        if (millis() > (intervaloAcel + TSAcel)) {
          TSAcel = millis();
          TikerControlSAcel.start();
        }
      }
      if (scaida) {
        mastersensorsfcv.setCaida(scaida);
        if (millis() > (intervaloAcel2 + TSAcel)) {
          TSAcel = millis();
          scaida = false;
        }
      }
    }

    //Define el tiempo que se ejecutara la publicacion de la bateria
    if (millis() > (intervaloBAT + TSBat)) {
      TSBat = millis();
      TikerControlBat.start();
    }
  }
  TikerControlBat.update();
  TikerControlSProx.update();
  TikerControlSTemp.update();
  TikerControlSPPM.update();
  TikerControlSAcel.update();
  TikerControlModuloBLE.update();
}
