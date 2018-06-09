# WaterMApp
 

### Abstract

WaterMApp is Internet of Things group-project for Pervasive Systems course - Spring 2018 at "Sapienza Università di Roma". 
The idea is to create an Android App that collects data about the positions of the fountains worldwide and a cheap monitoring system to get the quality of the water. 

# Presentation
[You can check the SlideShare presentation here](https://www.slideshare.net/mobile/GiuseppeAndreetti/watermapp-100552441)

# Architecture
![alt text](https://preview.ibb.co/nR4zCo/schema_Water_Mapp.png)

The B-L072Z-LRWAN1 - STM32L0 Board read PH and Turbidity from the fountain and send the read values through Lora wireless data communication to the other B-L072Z-LRWAN1 - STM32L0 Board. <br> The receiver board is connected to a Server that stores the values in a SQLite Database. The information are available to the app through Node.js.<br> The mobile phone connects to the server in order to get both the fountain positions and  water stats.

## Hardware

B-L072Z-LRWAN1 - STM32L0 Board
Analog Turbidity Sensor
Analog PH Sensor

## Technologies 

Lora
Mbed OS
Node.js
SQLite
Android App



# Android App
Android App that displays fountains markers on a Map using Maps SDK for Android.

![alt text](https://preview.ibb.co/mAVoV8/Screen2.png)

It is possible to get informations about the fountain just tapping on the marker; it will open a new screen in which we can leave comments about the quality of the water or check the status of the fountain, controlling the PH and the turbidity.       
In the case you find a new fountain, you can report it and add on the map just tapping on the add button.

![alt text](https://preview.ibb.co/cNpRHo/Screen1.png)
