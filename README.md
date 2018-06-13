# WaterMApp
 

### Abstract 

WaterMApp is Internet of Things group-project for Pervasive Systems course - Spring 2018 at "Sapienza Università di Roma". 
The idea is to create an Android App that collects data about the positions of the fountains worldwide and a cheap monitoring system to get the quality of the water.

# Project 

## Hardware

+ **STMicroelectronics B-L072Z-LRWAN1 - STM32L0 Board** / 2pcs.
+ **Analog Turbidity Sensor SKU:SEN0189**
+ **Analog PH Sensor SKU:SEN0161**

## Software & Technologies 

+ **Lora** used to exchange data between the two boards
+ **Mbed OS**  used in the Nucleo boards
+ **Node.js** used for the server
+ **SQLite** used for the database
+ **Python** used to get values from the receiver board
+ **Android App**

## Architecture
![alt text](https://image.ibb.co/bHxYVJ/Senza_titolo_2.png)

The **B-L072Z-LRWAN1 - STM32L0 Board** read PH and Turbidity from the fountain and send the read values through **Lora wireless data communication** to the other **B-L072Z-LRWAN1 - STM32L0 Board**. <br> The receiver board is connected to a **Server** that stores the values in a **SQLite Database**. The information are available to the app through **Node.js** as Json Objects.<br> The **Android phone** connects to the **Server** in order to get both the fountain positions and  water stats.

## Sensors connection schema
![alt text](https://image.ibb.co/i27HqJ/1e160303_a738_423d_83d5_c48d77253475.jpg)

## Code
The code is composed by:

+ [B-L072Z-LRWAN1 - STM32L0 Board](https://github.com/WaterMApp/STMCode)
+ [Middleware](https://github.com/WaterMApp/Middleware)
+ [Backend](https://github.com/WaterMApp/backend)
+ [Android App](https://github.com/WaterMApp/AndroidApp)

## B-L072Z-LRWAN1 - STM32L0 Board / Sender - Receiver

### MBed OS

We have used Mbed OS in order to manage the lora transreceiver and the read of analog sensors in a more proficient way.
(Anyway for educational purposes we also make it work with low level HAL libraries).

### MBED OS Repositories

Mbed OS provides its own version control service, we will point to it for the trasmitter and receiver code.
 - [Transmitter](https://os.mbed.com/users/gabrio/code/TRANSMITTER/): Reads analog values from analog A0 and A2 and sends them through LoRa<br>
 - [Receiver](https://os.mbed.com/users/gabrio/code/RECEIVER/): Receives LoRa messages and forwards them to a gateway
 
 ## Middleware 

This component read data from the LoRa receiver and send it to the server. It consists in a **Python script** that read the data from the Com4 serial port. 

## Backend
For the backend we used  both **Node.js** and **Sqlite**. Sqlite takes care about the informations storage of the fountain positions, the fountains stats and the comments. 
Instead, Node.js provides this information to the App as Json Object.

## Prototype 
Here the image of the prototype.
### Sender

### Receiver

## Android App
Android App that displays fountains markers on a Map using Maps SDK for Android.<br>
The **main** functionality of this app are:
+ get fountains positions
+ get water stats and quality
+ add a new discovered fountain on the map
+ leave comments about the fountain

![alt text](https://image.ibb.co/hbEYxy/Screen2.png)

It is possible to get informations about the fountain just tapping on the marker; it will open a new screen in which we can leave comments about the quality of the water or check the status of the fountain, controlling the PH and the turbidity.       
In the case you find a new fountain, you can report it and add on the map just tapping on the add button.

![alt text](https://image.ibb.co/etR2qJ/Screen1.png)

# Useful links
## Initial Concept & User Evaluation Presentation
[You can check the SlideShare presentation here](https://www.slideshare.net/GiuseppeAndreetti/watermapp-idea)

## Midterm Presentation
[You can check the SlideShare presentation here](https://www.slideshare.net/GiuseppeAndreetti/watermapp-midterm)

## Final Presentation
[You can check the SlideShare presentation here](https://www.slideshare.net/mobile/GiuseppeAndreetti/watermapp-100552441)

## Final Presentation Demo
[You can check the Demo here](https://youtu.be/W4Bmlybk8DI)

# Project Members - Contacts
+ Linkedin: [Giuseppe Andreetti](https://www.linkedin.com/in/giuseppe-andreetti/)  
+ Linkedin: [Adriano Pimpini](https://www.linkedin.com/in/adriano-pimpini/)  
+ Linkedin: [Lucia Rodinò](https://www.linkedin.com/in/lucia-rodinò-b5019815b/) 
+ WebSite:  [Gabrio Tognozzi](https://gabrio.ovh/) 


##
![Logo](https://preview.ibb.co/ebyZCo/logo_rosso_sapienza.png)

