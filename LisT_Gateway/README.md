## Install instructions

### Setting up Raspberry Pi

1. Follow [these](https://www.raspberrypi.org/documentation/installation/installing-images/README.md) instructions to prepare a SD card with Raspbian Lite.
2. Create a file named ssh with no content on your preinstalled card. 
3. To connect your Pi to your Local Network also create a file called wpa_supplicant.conf.
Fill it with with your Wifi credentials like:
```
network={
  ssid="YourWifiName"
  psk="yourwifipassphrase"
  key_mgmt=WPA-PSK
}
```
4. Power up your Pi and scan your network to find the Pis IP address.
5. SSH into your Pi via `pi@yourPisIpAddress` with password `raspberry
6. Update Raspbian by the following commands
```
sudo apt-get update
sudo apt-get upgrade
```
7. And then expand it by
```
sudo rapsi-config
```
And search for "Expand Filesystem". Invoke it and Reboot.

### Install Nodejs and NPM

8. Add the latest nodejs package to your apt-get source by:
```
curl -sL https://deb.nodesource.com/setup_8.x | sudo -E bash -
```

9. install it by

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
10. To Enable the AT86RF233 transreciever module add/uncomment the following line in `/boot/config.txt`
```
dtoverlay=at86rf233

```
* Reboot and check via `ip link`, you should see a `wpan0` device if successful