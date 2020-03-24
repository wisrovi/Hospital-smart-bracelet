#include "SensorCaidaFCV.h" //PortRead EjeX A2/EjeY A3/EjeZ A4/ PortVcc 10
#include "Ticker.h"

#define portEjeX A2
#define portEjeY A3
#define portEjeZ A4
#define portVccSCaida 10

float promX, promY, promZ; // PROMEDIO DE  LECTURAS
int ejeX, ejeY, ejeZ; // Valores instantaneos ejes acelerómetro
int muestras = 20;
int posicionTemporal = 0;
int serieTemporal[5] = {1, 2, 3, 4, 5};
boolean seParecenLosDatos = false;
float G_resultante_z;
void loopSensorCaida();
boolean ValorSCaida = false;


Ticker TLoopSensorCaida(loopSensorCaida, 150);

//*************************************************
//LOOP
//*************************************************
void SensorCaidaFCV::LoopSCaida() {
  TLoopSensorCaida.update();
}
//*************************************************
//LECTURA INICIAL DE LOS EJES
//*************************************************
void SensorCaidaFCV::SetupSCaida(){
	pinMode(portVccSCaida, OUTPUT);	
}


void SensorCaidaFCV::StartSCaida() {
  digitalWrite(portVccSCaida, HIGH);
  delay(10);

  for (int i = 0; i < muestras; i++) {
    ejeX = analogRead(portEjeX);
    promX += ejeX;
    ejeY = analogRead(portEjeY);
    promY += ejeY;
    ejeZ = analogRead(portEjeZ);
    promZ += ejeZ;
  }

  promX /= muestras;
  promY /= muestras;
  promZ /= muestras;

  TLoopSensorCaida.start();
}

//*****************************************************
//CONFIRMACION PRIMERA ALERTA
//*************************************************

boolean alertaZ = false;

//Funcion que verifica la primera estapa de caida
void ConfirmacionAlerta1() {
  float G_z = (analogRead(portEjeZ) - promZ);
  float Gz = G_z / 76;
  float G2z = pow(Gz, 2);
  G_resultante_z = (sqrt(G2z));
  //Serial.print("Resultante");
  //Serial.println(G_resultante_z);

  if (G_resultante_z > 2) {
    alertaZ = true;
    //delay(100);
  }

  //delay(100);
}

//*************************************************
//PROMEDIO DE LOS EJES X y Y
//*************************************************

//Funcion que promedia y totaliza muestras del eje x y y.
int sensadox_y() {
  int acumulador_x = 0;
  for (int contador = 0; contador < 5; contador++) {
    float G_x = (analogRead(portEjeX) - promX);
    G_x = pow(G_x, 2);

    acumulador_x =  G_x  + acumulador_x;

  }
  int promedio_x = acumulador_x / 5;


  int acumulador_y = 0;
  for (int contador = 0; contador < 5; contador++) {
    float G_y = (analogRead(portEjeY) - promY);
    G_y = pow(G_y, 2);

    acumulador_y =  G_y  + acumulador_y;

  }
  int promedio_y = acumulador_y / 5;

  float total = (sqrt( promedio_y + promedio_x));


  return round (total);
}

//*************************************************
//CONFIRMACION SEGUNDA ALERTA
//*************************************************
//Funcion para relaizar la segunda verificacion y confirmar la caida
int contar = 0;

void loopSensorCaida() {
  if (!alertaZ) {
    ConfirmacionAlerta1();  //para leer el eje z
  }

  if (alertaZ) {
    float total = sensadox_y();


    serieTemporal[posicionTemporal] = total;

    posicionTemporal++;
    if (posicionTemporal >= 5) {
      posicionTemporal = 0;

      for (int i = 0; i < 3; i++) {
        if ((serieTemporal[i] - serieTemporal[i + 1]) <= 30 && (serieTemporal[i] - serieTemporal[i + 1]) >= -30) {
		  contar++;
          seParecenLosDatos = true;
        }
        else {
          seParecenLosDatos = false;
          break;
        }
		//Serial.print("Total comparaciones: ");
		//Serial.println(contar);
		contar = 0;
      }

      if (seParecenLosDatos) {
		ValorSCaida = true;
        alertaZ = false;
        seParecenLosDatos = false;
      }
      else {
        alertaZ = false;
      }
    }
    //delay(100);
  }
}

boolean SensorCaidaFCV::ValorCaida() {
  return ValorSCaida;
}

void SensorCaidaFCV::ResetCaida() {
  ValorSCaida = false;
}

void SensorCaidaFCV::StopSCaida() {
  TLoopSensorCaida.stop();
  digitalWrite(portVccSCaida, LOW);
}