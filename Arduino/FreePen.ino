/*
  Blink
  Turns on an LED on for one second, then off for one second, repeatedly.

  Most Arduinos have an on-board LED you can control. On the Uno and
  Leonardo, it is attached to digital pin 13. If you're unsure what
  pin the on-board LED is connected to on your Arduino model, check
  the documentation at http://www.arduino.cc

  This example code is in the public domain.

  modified 8 May 2014
  by Scott Fitzgerald
 */

import processing.serial.*;


//Serial myPort;
int xPin = 0;
int yPin  = 1;
int zPin = 2;
float ax;
float ay;
float az;
float vx = 0.0;
float vy = 0.0;
float vz = 0.0;
float dx = 0.0;
float dy = 0.0;
float dz = 0.0;
float gx = 0.0;
float gy = 0.0;
float gz = 0.0;

void setup() {
  // initialize digital pin 13 as an output.
  Serial.begin(9600);
  pinMode(13, OUTPUT);

}

// the loop function runs over and over again forever
void loop() {

          ax = ((float)(analogRead(xPin) - 354) / 73.0);
          ay = ((float)(analogRead(yPin) - 354) / 73.0);
          az = ((float)(analogRead(zPin) - 354) / 73.0);

          ax *= 9.8;
          ay *= 9.8;
          az *= 9.8;

          gx = 0.9 * gx + 0.1 * ax;
          ax = ax - gx;

          gy = 0.9 * gy + 0.1 * ay;
          ay = ay - gy;

          gz = 0.9 * gz + 0.1 * az;
          az = az - gz;


          vx = vx + ax * 0.01;
          vy = vy + ay * 0.01;

          dx = dx + vx * 0.01;
          dy = dy + vy * 0.01;

          Serial.print(ax);
          Serial.print(",");
          Serial.print(ay);
          Serial.print(",");
          Serial.print(vx);
          Serial.print(",");
          Serial.println(vy);

          delay(10);
}
