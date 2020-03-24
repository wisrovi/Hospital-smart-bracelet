#ifndef MasterSensorsFCV_h
#define MasterSensorsFCV_h

 class MasterSensorsFCV
 
{			
  public:
	void loopBLE();
	void setupBLE();
	void setBateria(float newData);
	void setTemperatura(float newData);
	void setPPM(int newData);
	void setProximidad(bool newData);
	void setCaida(bool newData);
	void Inicializa_Enviar();

  private:
};
  
#endif















