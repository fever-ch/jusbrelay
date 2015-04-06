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

Copyright (C) 2015 Raphael P. Barazzutti

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.