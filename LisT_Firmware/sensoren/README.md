LisT_Firmware/sensoren/
================
This firmware is used to flash our sensor nodes.

Usage
=====
See the tutorial for RIOT to be able to flash our boards:
* https://github.com/RIOT-OS/Tutorials#tutorials-for-riot

Flash the main.c on a Phytec phyNODE KW22 Board.

Once you are all set up simply build and flash the application for your target board:

```
make all flash term
```

Should you have multiple boards connected to your machine use the following commands to ensure you are flashing the correct one:

```
make list-ttys
make all flash term SERIAL=<serialnumber the previous cmd returned>
```

Now you should have access to the RIOT shell on your board. You will see minor debug information being printed.
