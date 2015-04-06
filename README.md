# jUSBRelay

## Java driver for USB Relays

This driver aims to provide to the Java ecosystem access to low cost USB relays (such as DctTech devices or the ones available on eBay).

## Dependencies
* This driver relies on [JNA](https://github.com/twall/jna) and it this driver brings it transitively we used with Maven.   
* [HID API](http://www.signal11.us/oss/hidapi) has to be properly installed in order to use this driver. This is ot provided through Maven transitive dependencies.

## Compatibility
### This library has been successfully tested on the following platforms:
* Linux desktop machine (arch: amd64) 
* Linux Raspberry (arch: armv6l)
* Mac OS X (with hidapi provided by Homebrew)
 

---

2015 - Raphaël P. Barazzutti