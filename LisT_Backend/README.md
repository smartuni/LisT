# LisT Backend and Frontend

Living in a smart Tank is the smart solution for every aquarium owner!  
Observe and regulate your aquarium. Light, temperature and even the pH-value.

## The Idea in detail

Based on the RIOT-OS (https://github.com/RIOT-OS), we use sensors to monitor the three chosen values.  
Temperature, light and the pH-value.  
These values will be processed on a Raspberry Pie. The processed data will be saved in a database system.  
These data will be used to show your aquarium statistics in a Web application.  
Even further you'll be able to regulate your temperature and your light from all over the world. The pH-value on the other side is something special. You'll need to regulate it manually, so LisT will give you an alert, if the pH-Value differs from the default range.

## Features

* self-regulating light, that will adapt to the light outside
* self-regulating temperature, that will adapt to your default temperature
* monitored pH-value, that will alarm you, if it differs from the default range
* online statistics to your values and the possibility to change the defaults

## Setup the Backend

* install MySQL (configuration please see application.properties)
* install MQTT broker with 'sudo apt-get install mosquitto'
* install Angular on your Backend by following the given order (https://github.com/smartuni/LisT/blob/master/LisT_Doku/Angular%20install%20Order.txt)

## Getting started

* Use './gradlew bootRun' via terminal, in the LisT/LisT_Mqtt_Listener folder
* Use './gradlew bootRun' via terminal, in the LisT/LisT_Backend folder
* The Website runs on Port 8080 at /index.html.

