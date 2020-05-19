#include <Internet.h>
#include "DHT.h"

#define DHTPIN 33
#define DHTTYPE DHT22
DHT dht(DHTPIN, DHTTYPE);

struct SensorDHT22
{
  float temperatura;
  float humedad;
};

struct SensorDs18b20
{
  float temperatura;
};

struct Termocupla
{
  float temperatura = 0;
};
struct PlanillaDatos
{
  SensorDHT22 senDht22;
  SensorDs18b20 senDs18b20;
  Termocupla senTermocupla;
};

//*************************
//BlinkSendGet
//*************************

TaskHandle_t nucleo0Handle = NULL;
void Nucleo0( void *pvParameters );

void SepararProcesamientoNucleos() {
  xTaskCreatePinnedToCore(
    Nucleo0
    ,  "Nucleo0"   // A name just for humans
    ,  1024  // This stack size can be checked & adjusted by reading the Stack Highwater
    ,  NULL
    ,  3  // Priority, with 3 (configMAX_PRIORITIES - 1) being the highest, and 0 being the lowest.
    ,  &nucleo0Handle
    ,  0);

  bool usarWifi = true;
  bool usarEth = true;
//  ConfigInternet(usarWifi, usarEth);
}

PlanillaDatos planillaDatosSensores;

#include <OneWire.h>
#include <DallasTemperature.h>

const int pinDatosDQ = 34;
OneWire oneWireObjeto(pinDatosDQ);
DallasTemperature sensorDS18B20(&oneWireObjeto);

void SENSORES(AsyncWebServerRequest *request) {   //empleado 1 el cual toma los datos de los 3 sensores en conjunto

  DynamicJsonDocument datosSensores(256);

  DynamicJsonDocument datosDht22(128);
  datosDht22["temperatura"] = planillaDatosSensores.senDht22.temperatura;
  datosDht22["humedad"] = planillaDatosSensores.senDht22.humedad;

  DynamicJsonDocument datosDs18b20(128);
  datosDs18b20["Temperatura"] = planillaDatosSensores.senDs18b20.temperatura;

  DynamicJsonDocument datostermocupla(128);
  datostermocupla["temperatura"] = planillaDatosSensores.senTermocupla.temperatura;

  datosSensores["Sensor 0"] = datosDht22;
  datosSensores["Sensor 1"] = datosDs18b20;
  datosSensores["Sensor 2"]= datostermocupla;
  String output;  
  serializeJson(datosSensores, output);

  request->send(200, "text/plain", output);
}

void SENSORES1(AsyncWebServerRequest *request) {   //empleado 2 toma dato de forma individual del sensor DHT22

  DynamicJsonDocument datosDht22_1(128);
  datosDht22_1["temperatura"] = planillaDatosSensores.senDht22.temperatura;
  datosDht22_1["humedad"] = planillaDatosSensores.senDht22.humedad;
  String output;
  serializeJson(datosDht22_1, output);
  request->send(200, "text/plain", output);
}

void SENSORES2(AsyncWebServerRequest *request) {   //empleado 3 toma dato de forma individual del sensor DS18B20

  DynamicJsonDocument datosDs18b20_1(128);
  datosDs18b20_1["temperatura"] = planillaDatosSensores.senDs18b20.temperatura;  //bien
  String output;
  serializeJson(datosDs18b20_1, output);
  request->send(200, "text/plain", output);
}

void SENSORES3(AsyncWebServerRequest *request) {   //empleado 4 toma dato de forma individual del sensor Termocupla

  DynamicJsonDocument datosTermocupla(128);
  datosTermocupla["temperatura"] = planillaDatosSensores.senTermocupla.temperatura;
  String output;
  serializeJson(datosTermocupla, output);
  request->send(200, "text/plain", output);
}
 
void GUARDIAN(AsyncWebServerRequest *request) {   //empleado 5 toma datos de los sensores

  String output = "";
  request->send(200, "text/plain", output);
} 

TaskHandle_t Sensores_Nucleo0_Handle = NULL;  // contrato empleado 1
void Sensores_Nucleo0( void *pvParameters );

TaskHandle_t Guardian_Nucleo0_Handle = NULL;  // contrato empleado 2
void Guardian_Nucleo0( void *pvParameters );

void setup() {
  Serial.begin(115200);
  bool usarWifi = true;
  bool usarEth = false;
  bool isAP = false;
   SepararProcesamientoNucleos();

  //deshabilitarWifimanager(); //se usa esta linea para evitar que el sistema en caso de que las credenciales de red no sean correctas, el mismo sistema active el wifi manager
  if (ConfigRedWifiConection("KITARA86", "TOLOSA86") == false) {
    //se utiliza antes de configurar el internet para que la wifi se conecte a estas credenciales
    //esta instrucción no funciona si el modo AP esta activo
    Serial.println("No se puede configurar una conexion a una red wifi mientras este activo un modo AP.");
  }
  ConfigInternet(usarWifi, usarEth, isAP);

  if (isActiveUseWifi()) {
    while (!getStatusConectionwifi()) {
      //esperando a que se conecte a una red wifi
      delay(100);
      Serial.print(".");
    }
  }

  if (getStatusConectionwifi() && isActiveUseWifi()) {
    RegistrarNuevoServicioGet("/SENSORES", SENSORES);
    RegistrarNuevoServicioGet("/SENSORES1", SENSORES1);
    RegistrarNuevoServicioGet("/SENSORES2", SENSORES2);
    RegistrarNuevoServicioGet("/SENSORES3", SENSORES3);
    RegistrarNuevoServicioGet("/GUARDIAN", GUARDIAN);

    setIniciarServidorWifi();
  }

  {
    xTaskCreatePinnedToCore(
      Sensores_Nucleo0
      ,  "Sensores_Nucleo0"   // A name just for humans
      ,1024  //Esta es la RAM a utilizar (max:15000), pero tener en cuenta que es sumatorio con todos los procesos
      ,  NULL
      ,  3  // Priority, with 3 (configMAX_PRIORITIES - 1) being the highest, and 0 being the lowest.
      ,  &Sensores_Nucleo0_Handle
      ,  0);

//aca se colocan las tareas de los otros empleados?

    xTaskCreatePinnedToCore(
      Guardian_Nucleo0
      ,  "Guardian_Nucleo0"   // A name just for humans
      , 512   //Esta es la RAM a utilizar (max:15000), pero tener en cuenta que es sumatorio con todos los procesos
      ,  NULL
      ,  2  // Priority, with 3 (configMAX_PRIORITIES - 1) being the highest, and 0 being the lowest.
      ,  &Guardian_Nucleo0_Handle
      ,  0);
  }
}

void loop() {
}

//***************************************
//BlinkSendGet
//***************************************

void Nucleo0(void *pvParameters) { // This is a task.
  (void) pvParameters;
  Serial.print("Nucleo: ");
  Serial.println(xPortGetCoreID());

  pinMode(2, OUTPUT);

  unsigned long startAttemptTime = millis();
  unsigned long timeGetResponse = millis();
  bool esperarRespuesta = false;
  for (;;) { // A Task shall never return or exit.
    if (millis() - startAttemptTime > 5000) {
      startAttemptTime = millis();
      Serial.print("[Blink]: [Core 0]: "); Serial.println(xPortGetCoreID());
      SendGet("192.168.1.6", 2020, "/esp32?Datos=HolaHouston");
      timeGetResponse = millis();
      esperarRespuesta = true;
    }

    if (esperarRespuesta) {
      if (millis() - timeGetResponse > 2000) {
        esperarRespuesta = false;
        Serial.print("RTA Internet: ");
        Serial.println(getResponse());
      }
    }

    digitalWrite(2, !digitalRead(2));   // turn the LED on (HIGH is the voltage level)
    vTaskDelay(500 / portTICK_PERIOD_MS); // one tick delay (15ms) in between reads for stability
  }
  //vTaskDelete(NULL); //borrar esta tarea
}
//*********************************
//hasta aca
//********************************

void Sensores_Nucleo0(void *pvParameters) { // This is a task.
  (void) pvParameters;
  Serial.print("Sensores_Nucleo0: ");
  Serial.println(xPortGetCoreID());
  dht.begin();
  sensorDS18B20.begin();

  pinMode(2, OUTPUT);

  unsigned long startAttemptTime = millis();
  for (;;) { // A Task shall never return or exit.
    if (millis() - startAttemptTime > 15000) { //15000 = 15 segundos
      startAttemptTime = millis();
      digitalWrite(2, HIGH);

      Serial.print("[Sensores_Nucleo0]: [Core 0]: ");
      Serial.println(xPortGetCoreID());

      sensorDS18B20.requestTemperatures();

      planillaDatosSensores.senDht22.temperatura =  dht.readTemperature();
      planillaDatosSensores.senDht22.humedad =  dht.readHumidity();
      planillaDatosSensores.senDs18b20.temperatura = sensorDS18B20.getTempCByIndex(0);
     //planillaDatosSensores.Termocupla.temperatura = senTermocupla. aca va el termocupla tipo T

      digitalWrite(2, LOW);
    }
    vTaskDelay(100 / portTICK_PERIOD_MS); // one tick delay (15ms) in between reads for stability
  }
  //vTaskDelete(NULL); //borrar esta tarea
}

void Guardian_Nucleo0(void *pvParameters) { // This is a task.
  (void) pvParameters;
  Serial.print("Guardian_Nucleo0: ");
  Serial.println(xPortGetCoreID());

  //setup
  // temperatura1;
  // humedad1;
  // temperatura2;

  unsigned long startAttemptTime = millis();
  for (;;) { // A Task shall never return or exit.
    if (millis() - startAttemptTime > 16000) { //16000 = 16 segundos
      startAttemptTime = millis();
      //loop
    }
    vTaskDelay(100 / portTICK_PERIOD_MS); // one tick delay (15ms) in between reads for stability
  }
  //vTaskDelete(NULL); //borrar esta tarea
}
