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

import ch.fever.jhidapi.common.Buffer;
import ch.fever.jhidapi.common.FeatureReport;
import ch.fever.jhidapi.jna.HidApiNative;
import ch.fever.jhidapi.jna.HidDevice;
import ch.fever.jhidapi.jna.HidDeviceInfoStructure;
import com.sun.jna.Native;

import java.util.LinkedList;
import java.util.List;

public class HidApiDriver {
    final public HidApiNative hidApiNative;

    static public HidApiDriver newInstance() throws HidLibException {
        String path = System.getProperty("os.name").toLowerCase().contains("linux") ? "hidapi-libusb" : "hidapi";
        try {
            HidApiNative han = (HidApiNative)
                    Native.loadLibrary(path, HidApiNative.class);
            return new HidApiDriver(han);
        } catch (UnsatisfiedLinkError e) {
            throw new HidLibException("Unable to load the HidApi library from the library search paths");
        }
    }

    public void exit() {
        hidApiNative.hid_exit();
    }

    private HidApiDriver(HidApiNative hidApiNative) {
        this.hidApiNative = hidApiNative;
    }

    private HidDeviceInfoStructure enumerate(short vendor_id, short product_id) {
        return hidApiNative.hid_enumerate(vendor_id, product_id);
    }

    private void freeEnumeration(HidDeviceInfoStructure devs) {
        hidApiNative.hid_free_enumeration(devs.getPointer());
    }

    synchronized public List<DeviceInfoStructure> getEnumeration(short vendor_id, short product_id) {
        List<DeviceInfoStructure> list = new LinkedList<>();
        HidDeviceInfoStructure penum = enumerate(vendor_id, product_id);
        HidDeviceInfoStructure p = penum;

        while (p != null) {
            list.add(DeviceInfoStructure.copy(p, hidApiNative));
            p = p.next;
        }

        if (penum != null)
            freeEnumeration(penum);
        return list;
    }

    synchronized public HidDevice openPath(String path) {
        if (path == null)
            throw new NullPointerException();
        HidDevice ret = hidApiNative.hid_open_path(path);
        if (ret == null)
            throw new HidException("hid_open_path returned null while opening " + path);
        return ret;
    }


    synchronized public void sendFeatureReport(HidDevice device, FeatureReport featureReport) {
        if (device == null || featureReport == null)
            throw new NullPointerException();
        int i = hidApiNative.hid_send_feature_report(device, featureReport, featureReport.size());
        if (i < 0)
            throw new RuntimeException("hid_send_feature_report returned " + i);
    }


    synchronized public void getFeatureReport(HidDevice device, FeatureReport featureReport) {
        if (device == null || featureReport == null)
            throw new NullPointerException();
        int i = hidApiNative.hid_get_feature_report(device, featureReport, featureReport.size());
        if (i < 0)
            throw new HidException("hid_get_feature_report returned " + i);
    }


    synchronized public void write(HidDevice device, Buffer data) {
        if (device == null || data == null)
            throw new NullPointerException();
        int i = hidApiNative.hid_write(device, data, data.size());
        if (i < 0)
            throw new RuntimeException();
    }

    public void write(DevicePointer device, byte[] data) {
        write(device, new Buffer(data));
    }

    public void write(DevicePointer device, Buffer data) {
        write(device.getPointer(), data);
    }


    synchronized public void close(HidDevice device) {
        hidApiNative.hid_close(device);
    }

    public void close(DevicePointer device) {
        close(device.getPointer());
    }

    synchronized public void hidExit() {
        hidApiNative.hid_exit();
    }
}
