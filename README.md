# LisT

Living in a smart Tank is the smart solution for every aquarium owner!  
Observe and regulate your aquarium. Light, temperature and even the pH-value.

## The Idea in detail

Based on the RIOT-OS (https://github.com/RIOT-OS), we use sensors to monitor chosen values such as
Temperature, light and pH-value.  
These values will be processed on a Raspberry Pie. The processed data will be saved in a database system.  
This data will then be used to show aquarium statistics on a web application.  
Additionally you'll be able to regulate the aquriums temperature and light from all over the world using RIOT-OS ready actuators. The pH-value on the other side is something special. You'll need to regulate it manually, so LisT will give you an alert, if the pH-Value differs from the default range.
By default the LED actuator will try to simulate lighting conditions based on the actual day and night cycle of the current date. An LED-Bar will visually indicate the current water temperatur of the aquarium and turn red once the water temperature exceeds a certain threshold.

## Features

* self-regulating light, that will adapt to the light outside
* self-regulating temperature, that will adapt to your default temperature
* monitored pH-value, that will alarm you, if it differs from the default range
* online statistics to your values and the possibility to change the defaults
* visual indicator for the water temperature

## Getting started

### Setting up the hardware

To use our system, you'll need the following hardware:

* 1x Raspberry Pi
* 1x Actuator board: Atmel SAM R21 Xplained Pro
* 1x Sensor board: Phytec phyNODE KW22

The sensor board comes with a variety of sensor already mounted on the pcb and will provide an easy setup. If instead you want to use external sensors or different boards you can find a list of RIOT compatible boards and sensors using the following links:

* https://github.com/RIOT-OS/RIOT/wiki/RIOT-Platforms
* https://riot-os.org/api/group__drivers__sensors.html

Further external hardware:

* 1x pH-sensor
* 1x heating element (desc tba)
* 1x Antrax SwitchBox-Relais
* 1x LED strip
* 1x VMA01 RGB Shield for Arduino
* 1x 12V Power supply for the Arduino Shield
* 1x Grove - LED Bar 104030002

Information on which pins to connect to what external hardware please see ..... KATRIN !!!!!!!!!!!!!!!!!!!!

### Flashing the hardware 

* To find details on flashing the sensor and actuator nodes please see the folder called "firmware".
* To find information on setting up the raspberry pi please view the contents of folder "LisT_Gateway"
* -> https://github.com/smartuni/LisT/tree/master/LisT_Gateway

### Setting up the backend and frontend

* To find details on setting up the back- and frontend. Please see the folder "LisT_Backend"
* -> https://github.com/smartuni/LisT/tree/master/LisT_Backend
