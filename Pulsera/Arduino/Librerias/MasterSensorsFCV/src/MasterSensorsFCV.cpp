#include <SoftwareSerial.h>
#include "MasterSensorsFCV.h"
#include <Ticker.h>
#include "Arduino.h"

#define powerHM10 A1
#define RXHM10 4
#define TXHM10 3
#define constanteComunicacion 270

float _bateria = 1;
int _ppm = 0;
boolean _proximidad = false;
boolean _caida = false;
float _temperatura = 0;

SoftwareSerial HM10(RXHM10, TXHM10); // RX, TX

boolean hayComunicacion = false;
boolean haveMac = false;
String ibe[4] = {"00007067", "", "", ""};
boolean configComplete[2] = {false, false};
boolean changeIBE = false;

bool first_start = false;
String packet_ibe0 = "";
String packet_ibe1 = "";
String packet_ibe2 = "";
String packet_ibe3 = "";
bool estadoComunicacion[5] = { false, false, false, false, false };
String mac_ibe1 = "";
String mac_ibe2 = "";
unsigned long time_process = 0;


void checkTimeLifeProcess();
Ticker bootFinishProcess(checkTimeLifeProcess, 50);

void cerrarProceso(){
	//Serial.print("Tiempo gastado en proceso: ");
	//Serial.println(millis() - time_process);
	
	digitalWrite(powerHM10, LOW);
	estadoComunicacion[0]=false;
	estadoComunicacion[1]=false;
	estadoComunicacion[2]=false;
	estadoComunicacion[3]=false;
	estadoComunicacion[4]=false;
	bootFinishProcess.stop();
}

void checkTimeLifeProcess(){
	if(estadoComunicacion[0]==true){
		unsigned long time_process_finish ;
		if(first_start){
			time_process_finish = 450;
		}else{			
			time_process_finish = 950;
		}
		time_process_finish = time_process_finish + constanteComunicacion;
		if((millis() - time_process) >= time_process_finish){
			cerrarProceso();
		}
	}			
}

void MasterSensorsFCV::Inicializa_Enviar(){
	digitalWrite(powerHM10, HIGH);
	time_process = millis();
	delay(constanteComunicacion);
	HM10.write("AT");
	//Serial.println("AT reset");	
	bootFinishProcess.start();
}

void MasterSensorsFCV::loopBLE(){
	bootFinishProcess.update();
	static byte conteoIntentoComunicacion = 0;
	HM10.listen();  // listen the HM10 port
	if (HM10.available()) {
		String inData = "";
		while (HM10.available()) {
		  char c = HM10.read();
		  inData.concat(String(c));// save the data in string format
		  delay(1);
		}
		//Serial.print("inData: ");
		//Serial.println(inData);
					
		if(first_start == true){			
			if(estadoComunicacion[0]==false){	
				if (inData.equals("OK")) {
					estadoComunicacion[0] = true;
					conteoIntentoComunicacion = 0;
				}else{
					if(conteoIntentoComunicacion<3){
						HM10.write("AT");
						conteoIntentoComunicacion++;
					}else{
						conteoIntentoComunicacion = 0;
					}
				}
			}
			
			if(estadoComunicacion[0]==true){
				if(estadoComunicacion[3]==false){
					packet_ibe2 = "AT+IBE2";				
					packet_ibe2.concat(mac_ibe2);
					
					String bateriaString = (String) ( (int) (_bateria * 10) );						
					packet_ibe2.concat(bateriaString);
					char packet_ibe2_char[18];
					packet_ibe2.toCharArray(packet_ibe2_char, 18);
					HM10.write(packet_ibe2_char);
					estadoComunicacion[3] = true;
					//Serial.print("estadoComunicacion: ");Serial.println("3");
					Serial.println(packet_ibe2_char);
				}else{
					if(estadoComunicacion[4]==false){
						packet_ibe3 = "AT+IBE3";						
						{
							String ppmString = (String) _ppm;
							if (ppmString.length() < 3) {
							  if (ppmString.length() < 2) {
								ppmString = "00";
							  } else {
								ppmString = "0";
							  }
							  ppmString.concat((String) _ppm);
							}
							packet_ibe3.concat(ppmString);
							
							if (_proximidad) {
							  packet_ibe3.concat("1");
							} else {
							  packet_ibe3.concat("0");
							}
							
							if (_caida) {
							  packet_ibe3.concat("1");
							} else {
							  packet_ibe3.concat("0");
							}
							
							String temperaturaString = (String) ( (int) (_temperatura * 10) );
							if (temperaturaString.length() < 3) {
							  if (temperaturaString.length() < 2) {
								temperaturaString = "00";
							  } else {
								temperaturaString = "0";
							  }
							  temperaturaString.concat(  (String) ( (int) (_temperatura * 10) )  );
							}
							packet_ibe3.concat(temperaturaString);
						}
																		
						char packet_ibe3_char[18];
						packet_ibe3.toCharArray(packet_ibe3_char, 18);
						HM10.write(packet_ibe3_char);
						estadoComunicacion[4]=true;
						//Serial.print("estadoComunicacion: ");Serial.println("4");
						Serial.println(packet_ibe3_char);
					}	else{					
						//Serial.print("estadoComunicacion: ");Serial.println("5");					
						cerrarProceso();						
					}			
				}
			}							
		}
		
		if(first_start == false){			
			if(estadoComunicacion[0]==false){
				if (inData.equals("OK")) {
					HM10.write("AT+ADDR?");
					estadoComunicacion[0] = true;
				}
			}else{
				if(estadoComunicacion[1]==false){
					inData = inData.substring(8);
					mac_ibe1 = inData.substring(0, 6);
					mac_ibe2 = inData.substring(6);
											
					packet_ibe0 = "AT+IBE000007067";
					
					char packet_ibe0_char[18];
					packet_ibe0.toCharArray(packet_ibe0_char, 18);
					HM10.write(packet_ibe0_char);	
					estadoComunicacion[1] = true;
				}else{
					if(estadoComunicacion[2]==false){
						packet_ibe1 = "AT+IBE1";
						packet_ibe1.concat("86");
						packet_ibe1.concat(mac_ibe1);
						
						char packet_ibe1_char[18];
						packet_ibe1.toCharArray(packet_ibe1_char, 18);
						HM10.write(packet_ibe1_char);
												
						estadoComunicacion[2] = true;
						
						first_start = true;
					}
				}					
			}
		}
				
	}	
}


void MasterSensorsFCV::setupBLE() {
  pinMode(powerHM10, OUTPUT);  
  HM10.begin(9600);
  delay(10);
}

void MasterSensorsFCV::setBateria(float newData) {
  _bateria = newData;
}

void MasterSensorsFCV::setTemperatura(float newData) {
  _temperatura = newData;
}

void MasterSensorsFCV::setPPM(int newData) {
  _ppm = newData;
}

void MasterSensorsFCV::setProximidad(boolean newData) {
	_proximidad = newData;
}

void MasterSensorsFCV::setCaida(boolean newData) {
	_caida = newData;
}