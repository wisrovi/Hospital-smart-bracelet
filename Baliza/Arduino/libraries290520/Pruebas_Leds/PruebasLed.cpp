#include "PruebasLed.h"
#include "Arduino.h"

void PruebaLed::inicializa(){

pinMode(LED_BUILTIN, OUTPUT);

}

void PruebaLed::funcionleds(){

digitalWrite(LED_BUILTIN, HIGH);  
  delay(200);                       
  digitalWrite(LED_BUILTIN, LOW);   
  delay(200);

}