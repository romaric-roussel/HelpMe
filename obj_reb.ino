/**
   BasicHTTPClient.ino

    Created on: 24.05.2015

*/

#include <Arduino.h>

#include <ESP8266WiFi.h>
#include <ESP8266WiFiMulti.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>
#include <LiquidCrystal.h>
#include <Wire.h>
#include <Adafruit_GFX.h>
#include "Adafruit_LEDBackpack.h"

Adafruit_8x16matrix matrix = Adafruit_8x16matrix();

int pinBouton;
ESP8266WiFiMulti WiFiMulti;

String MESSAGE[50][3];
int sizeMessage;
//const int rs = 1, en = 14, d4 = 16, d5 = 2, d6 = 13, d7 = 15;
//LiquidCrystal lcd(rs, en, d4, d5, d6, d7);

void setup() {

  Serial.begin(115200);


  
 // WiFiMulti.addAP("H20", "qqqq4444");
  
  
  pinMode(0,INPUT_PULLUP);
  matrix.begin(0x70);
  matrix.setRotation(1);
  // Print a message to the LCD.
}

void loop() {
  // set the cursor to column 0, line 1
  // (note: line 1 is the second row, since counting begins with 0):

  //parseData(getData());
  
  matrix.setCursor(0,0);

  Serial.print(sizeMessage);
  
  matrix.print(sizeMessage);
  matrix.writeDisplay();
  delay(1000);
  if(!digitalRead(0))
  {
    deleteData(MESSAGE[0][0]);

    delay(1000);
  }
  matrix.clear();
  

}


void parseData(String s)
{
 
  if(s.indexOf("{") != -1)
  {
    sizeMessage = 0;
    MESSAGE[0][0] = "-1";
    MESSAGE[0][1] = "Infos";
    MESSAGE[0][2] = "Aucun message";
  }
  else
  {
    int indexTab = 0;
    int startPosition = 0;
    int endPosition = s.indexOf("&");
    int startIntoStringPosition = 0;
    int endIntoStringPosition = 0;
    String id;
    String author;
    String message;
    String temp;
    sizeMessage = 0;
    while(1)
    {
     sizeMessage ++; 
     temp = s.substring(startPosition,endPosition);
     startIntoStringPosition = 0;
     endIntoStringPosition = temp.indexOf(":");
     MESSAGE[indexTab][0] =  temp.substring(startIntoStringPosition,endIntoStringPosition);
     startIntoStringPosition = endIntoStringPosition + 1;
     endIntoStringPosition = temp.indexOf(":",startIntoStringPosition);
     MESSAGE[indexTab][1] = temp.substring(startIntoStringPosition,endIntoStringPosition);
     startIntoStringPosition = endIntoStringPosition + 1;
     endIntoStringPosition = temp.indexOf(":",startIntoStringPosition);
     MESSAGE[indexTab][2] = temp.substring(startIntoStringPosition,endIntoStringPosition);
     indexTab++;
     startPosition = endPosition + 1;
     endPosition = s.indexOf("&",startPosition);
     if(endPosition == -1)
     break;   
    }
  }
}

String getData() 
{
  WiFiClient client;
  HTTPClient http;
  if ((WiFiMulti.run() == WL_CONNECTED)) 
  {
    if (http.begin(client, "http://help.api.coopuniverse.fr/message")) {
      int httpCode = http.GET();
      if (httpCode > 0) 
      {
        if (httpCode == HTTP_CODE_OK) 
        {
          return http.getString();
        }
      } 
      else 
      {
        Serial.printf("ERROR: Request result no 200");
      }
      http.end();
    } 
    else 
    {
      Serial.printf("ERROR: Is not connected");
    }
  }
}


bool deleteData(String id) 
{
  WiFiClient client;
  HTTPClient http;
  if ((WiFiMulti.run() == WL_CONNECTED)) 
  {
    if (http.begin(client, "http://help.api.coopuniverse.fr/message/delete?id=" + id)) {
      int httpCode = http.GET();
      if (httpCode > 0) 
      {
        if (httpCode == HTTP_CODE_OK) 
        {
          http.end();
          return true;
        }
      } 
      else 
      {
        Serial.printf("ERROR: Request result no 200");
      }
      
    } 
    else 
    {
      Serial.printf("ERROR: Is not connected");
    }
  }
  http.end();
  return false;
}
