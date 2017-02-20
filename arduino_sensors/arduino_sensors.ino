#include <Adafruit_Sensor.h>
#include <Adafruit_LSM303_U.h>
#include <Adafruit_9DOF.h>
#include <Adafruit_L3GD20_U.h>
#include <SPI.h>
#include <Pixy.h>
#include <LIDARLite.h>
#include <Wire.h>
#include <Timer.h>

#define X_CENTER ((PIXY_MAX_X-PIXY_MIN_X)/2)
Pixy pixy;
LIDARLite lidar;
uint16_t blocks;
boolean debug = false;
Timer t;
String gyroPrint;
Adafruit_9DOF                dof   = Adafruit_9DOF();
Adafruit_LSM303_Mag_Unified   mag   = Adafruit_LSM303_Mag_Unified(30302);
int i;

void printData() {
  sensors_vec_t orientation;
  sensors_event_t mag_event;
  int dif;
  blocks = -1;
  char buffer[100];
  blocks = pixy.getBlocks();
  // PIXY STUFF
  String dir;
  static int i = 0;
  int CenterOfBlocks;
  char buf[7];

  if (blocks) {
    //Serial.println("Block Count: " + String(blocks));
    if (blocks == 1) {
      CenterOfBlocks = pixy.blocks[0].x;
    } else if (blocks == 2) {
      CenterOfBlocks = (pixy.blocks[0].x + pixy.blocks[1].x) / 2;
    } else {
      CenterOfBlocks = 0;
    }
    //if (blocks == 1 || blocks == 2) {
    dif = X_CENTER - CenterOfBlocks;
    if (dif < -5) {
      sprintf(buffer, "^%s~%s~%s~", "PIXY", "direction", "right");
      Serial.print(buffer);
    } else if (dif > 5) {
      sprintf(buffer, "^%s~%s~%s~", "PIXY", "direction", "left");
      Serial.print(buffer);
    } else {
      sprintf(buffer, "^%s~%s~%s~", "PIXY", "direction", "straight");
      Serial.print(buffer);
    }
    if (debug) {
      Serial.println("");
    }

  }


  //LIDAR STUFF
  sprintf(buffer, "^%s~%s~%d~", "LIDAR", "cm", lidar.distance());
  Serial.print(buffer);
  if (debug) {
    Serial.println("");
  }

  mag.getEvent(&mag_event);
  if (dof.magGetOrientation(SENSOR_AXIS_Z, &mag_event, &orientation))
  {
    dtostrf(orientation.heading, 5, 2 , buf);
    sprintf(buffer, "^%s~%s~%s~", "GYRO", "heading", buf);
    Serial.print(buffer);
  }
  if (debug) {
    Serial.println("");
  }
}

void setup() {
  Serial.begin(115200);
  pixy.init();
  if (debug) {
    Serial.println("PIXY ONLINE");
  }
  lidar.begin(0, true);
  if (debug) {
    Serial.println("LIDAR ONLINE");
  }
  mag.begin();
  if (debug)
  {
    Serial.println("GYRO ONLINE");
  }

}

void loop() {

  t.update();

}




