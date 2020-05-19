#include "DHT.h"

#define DHTPIN 22
#define DHTTYPE DHT22
DHT dht(DHTPIN, DHTTYPE);

#include <OneWire.h>
#include <DallasTemperature.h>

// Pin donde se conecta el bus 1-Wire
const int pinDatosDQ = 23;
// Instancia a las clases OneWire y DallasTemperature
OneWire oneWireObjeto(pinDatosDQ);
DallasTemperature sensorDS18B20(&oneWireObjeto);

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  Serial.println(F("DHTxx test!"));
  dht.begin();
  sensorDS18B20.begin();
}

void loop() {
  // put your main code here, to run repeatedly:

  delay(2000);

  float h = dht.readHumidity();
  // Read temperature as Celsius (the default)
  float t = dht.readTemperature();
  // Read temperature as Fahrenheit (isFahrenheit = true)
  float f = dht.readTemperature(true);

  // Check if any reads failed and exit early (to try again).
  if (isnan(h) || isnan(t) || isnan(f)) {
    Serial.println(F("Failed to read from DHT sensor!"));
    return;
  }

  // Compute heat index in Fahrenheit (the default)
  float hif = dht.computeHeatIndex(f, h);
  // Compute heat index in Celsius (isFahreheit = false)
  float hic = dht.computeHeatIndex(t, h, false);

  Serial.print(F("Humidity: "));
  Serial.print(h);
  Serial.print(F("%  Temperature: "));
  Serial.print(t);
  Serial.print(F("°C "));
  Serial.print(f);
  Serial.print(F("°F  Heat index: "));
  Serial.print(hic);
  Serial.print(F("°C "));
  Serial.print(hif);
  Serial.println(F("°F"));

  Serial.println("Mandando comandos a los sensores");
  sensorDS18B20.requestTemperatures();

  // Leemos y mostramos los datos de los sensores DS18B20
  Serial.print("Temperatura sensor 0: ");
  Serial.print(sensorDS18B20.getTempCByIndex(0));
  Serial.println(" C");
  // Serial.print("Temperatura sensor 1: ");
  // Serial.print(sensorDS18B20.getTempCByIndex(1));
  // Serial.println(" C");

  delay(1000);









}
