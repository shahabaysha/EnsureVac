#include <TinyGPS++.h>
#include <HardwareSerial.h>
TinyGPSPlus gps;
HardwareSerial SerialGPS(1);
 
void setup()
{
// put your setup code here, to run once:
Serial.begin(115200); //baud rate of 9600
SerialGPS.begin(9600, SERIAL_8N1, 16, 17); //(baud rate, protocol, Rx pin, Tx pin)
} 
void loop(){
  gps.encode(SerialGPS.read());
  Serial.print("Speed: ");
  Serial.println(gps.speed.kmph());
  Serial.print("\n");
  Serial.print("Longitude: ");
  Serial.print(gps.location.lng());
  Serial.print("Latitude: ");
  Serial.println(gps.location.lat());
  Serial.print("Satellites: ");
  Serial.println(gps.satellites.value());

  delay(2000);
  }
