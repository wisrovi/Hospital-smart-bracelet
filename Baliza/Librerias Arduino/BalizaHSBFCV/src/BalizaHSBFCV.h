#ifndef BalizaHSBFCV_h
#define BalizaHSBFCV_h

#include "Arduino.h"
#include <ArduinoJson.h>

class BalizaHSBFCV
{

  public:
    void start_at();
    void loopScanear();
    String Totalbeacons();

	private:

};

#endif
