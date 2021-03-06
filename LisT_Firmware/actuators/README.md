LisT_Firmware/actuators
================
This firmware is used to flash our actuator nodes.

Usage
=====
See the tutorial for RIOT to be able to flash our boards:
* https://github.com/RIOT-OS/Tutorials#tutorials-for-riot

Flash the main.c on a samrx21 Board.
```
make all flash term
```
Should you have multiple boards connected to your machine use the following commands to ensure you are flashing the correct one:

```
make list-ttys
make all flash term SERIAL=<serialnumber the previous cmd returned>
```


## Connect your Board to the actuators as follwoing:

#### SAMR21 for the LED strip:

* RGB-LED-R: PA16
* RGB-LED-G: PA18
* RGB-LED-B: PA19

-> connect GND with the GND Board of the LED-Band

#### LED bar:

* VCC: EXT1 VCC
* GND: EXT1 GND oder EXT2 GND
* CLK: PA13
* DI: PA28

#### Heating element:

-> SwitchBox with RS232 cable
* GND: Ext1 GND oder EXT2 GND (Pin 8)
* VCC: PA23 (Pin 1)
