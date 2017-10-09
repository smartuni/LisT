## Install instructions

### Setting up Raspberry Pi

* Follow [these](https://www.raspberrypi.org/documentation/installation/installing-images/README.md) instructions to prepare a SD card with Raspbian Lite.
* Create a file named ssh with no content on your preinstalled card. 
* To connect your Pi to your Local Network also create a file called wpa_supplicant.conf.
Fill it with with your Wifi credentials like:
```
network={
  ssid="YourWifiName"
  psk="yourwifipassphrase"
  key_mgmt=WPA-PSK
}
```
* Power up your Pi and scan your network to find the Pis IP address.
* SSH into your Pi via pi@yourPisIpAddress with password "raspberry"
* Update Raspbian by the following commands
```
sudo apt-get update
sudo apt-get upgrade
```
* And then expand it by
```
sudo rapsi-config
```
And search for "Expand Filesystem". Invoke it and Reboot.

### Install Nodejs and NPM

* Add the latest nodejs package to your apt-get source by:
```
curl -sL https://deb.nodesource.com/setup_8.x | sudo -E bash -
```

* install it by

```
sudo apt install nodejs
```
* And check your version by 
```
node -v
```
You should see something like: 
```
v8.6.0  
```