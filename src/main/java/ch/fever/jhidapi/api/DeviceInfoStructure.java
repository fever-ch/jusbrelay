/*
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

package ch.fever.jhidapi.api;


import ch.fever.jhidapi.jna.HidApiNative;
import ch.fever.jhidapi.jna.HidDeviceInfoStructure;
import com.sun.jna.Pointer;

public class DeviceInfoStructure {
    private DeviceInfoStructure(int interfaceNumber, String path, short vendorId, short productId, String serialNumber,
                                short releaseNumber, String manufacturerString, String productString, short usagePage,
                                short usage, HidApiNative hidApiNative) {
        this.interfaceNumber = interfaceNumber;
        this.path = path;
        this.vendorId = vendorId;
        this.productId = productId;
        this.serialNumber = serialNumber;
        this.releaseNumber = releaseNumber;
        this.manufacturerString = manufacturerString;
        this.productString = productString;
        this.usagePage = usagePage;
        this.usage = usage;
        this.hidApiNative = hidApiNative;
    }

    static String npeb(Object o) {
        return o != null ? o.toString() : "";
    }

    static public DeviceInfoStructure copy(HidDeviceInfoStructure hidDis, HidApiNative hidApiNative) {
        return new DeviceInfoStructure(hidDis.interfaceNumber,
                hidDis.path,
                hidDis.vendorId,
                hidDis.productId,
                npeb(hidDis.serialNumber),
                hidDis.releaseNumber,
                npeb(hidDis.manufacturerString),
                npeb(hidDis.productString),
                hidDis.usagePage,
                hidDis.usage,
                hidApiNative);
    }

    public int getInterfaceNumber() {
        return interfaceNumber;
    }

    public String getManufacturerString() {
        return manufacturerString;
    }

    public String getPath() {
        return path;
    }

    public short getProductId() {
        return productId;
    }

    public String getProductString() {
        return productString;
    }

    public short getReleaseNumber() {
        return releaseNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public short getUsage() {
        return usage;
    }

    public short getUsagePage() {
        return usagePage;
    }

    public short getVendorId() {
        return vendorId;
    }

    final private String path;

    final private short vendorId;
    final private short productId;
    final private String serialNumber;

    final private short releaseNumber;
    final private String manufacturerString;

    final private String productString;
    final private short usagePage;

    final private short usage;
    final private int interfaceNumber;

    final private HidApiNative hidApiNative;

    public DevicePointer openDevice() {
        Pointer ret = hidApiNative.hid_open_path(path);
        if (ret == null)
            throw new HidException("hid_open_path returned null while opening " + path);
        return new DevicePointer(hidApiNative, ret);
    }
}