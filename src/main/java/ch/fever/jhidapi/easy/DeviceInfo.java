/**
 * Copyright (C) 2015 Raphael P. Barazzutti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.fever.jhidapi.easy;

import ch.fever.jhidapi.jna.HidDeviceInfoStructure;

public class DeviceInfo {

    public int getInterfaceNumber() {
        return interfaceNumber;
    }


    public String getManufacturerString() {
        return manufacturerString;
    }

    public String getPath() {
        return path;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductString() {
        return productString;
    }

    public int getReleaseNumber() {
        return releaseNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public int getUsage() {
        return usage;
    }

    public int getUsagePage() {
        return usagePage;
    }

    public int getVendorId() {
        return vendorId;
    }

    public DeviceInfo(HidDeviceInfoStructure struct) {
        path = struct.path;
        vendorId = 0xffff & struct.vendorId;
        productId = 0xffff & struct.productId;
        serialNumber = struct.serialNumber.toString();

        releaseNumber = 0xffff & struct.releaseNumber;
        manufacturerString = struct.manufacturerString.toString();

        productString = struct.productString.toString();
        usagePage = 0xffff & struct.usagePage;

        usage = 0xffff & struct.usage;
        interfaceNumber = struct.interfaceNumber;
    }

    final private String path;

    final private int vendorId;
    final private int productId;
    final private String serialNumber;

    final private int releaseNumber;
    final private String manufacturerString;

    final private String productString;
    final private int usagePage;

    final private int usage;
    final private int interfaceNumber;


}

