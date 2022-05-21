#include <Arduino.h>
#include <Wire.h>
#include <SPI.h>
#include <DHT.h>
#include <Adafruit_Sensor.h>


DHT my_sensor(15, DHT22);

float temperature, humidity;

void setup() {
  Serial.begin(9600);
  my_sensor.begin();
 
  
}

void loop() {
  humidity= my_sensor.readHumidity();
  temperature = my_sensor.readTemperature();


  Serial.print("Temperature: ");
  Serial.print(temperature);
  Serial.print("*C");
  Serial.print(" Humidity: ");
  Serial.print(humidity);
  Serial.print("%");
  Serial.println();
  delay(2000);
  
}
