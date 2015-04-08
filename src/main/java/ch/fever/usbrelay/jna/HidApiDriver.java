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

package ch.fever.usbrelay.jna;

import ch.fever.usbrelay.decttech.DectStatus;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import java.util.LinkedList;
import java.util.List;

public class HidApiDriver {
    final private HidApiNative INSTANCE = (HidApiNative)
            Native.loadLibrary(System.getProperty("os.name").toLowerCase().contains("linux") ? "hidapi-libusb" : "hidapi", HidApiNative.class);

    private HidDeviceInfoStructure enumerate(short vendor_id, short product_id) {
        return INSTANCE.hid_enumerate(vendor_id, product_id);
    }

    private void freeEnumeration(HidDeviceInfoStructure devs) {
        INSTANCE.hid_free_enumeration(devs.getPointer());
    }

    synchronized public List<DeviceInfoStructure> getEnumeration(short vendor_id, short product_id) {
        List<DeviceInfoStructure> list = new LinkedList<>();
        HidDeviceInfoStructure penum = enumerate(vendor_id, product_id);
        HidDeviceInfoStructure p = penum;

        while (p != null) {
            list.add(DeviceInfoStructure.copy(p));
            p = p.next;
        }

        if (penum != null)
            freeEnumeration(penum);
        return list;
    }

    synchronized public Pointer openPath(String path) {
        if (path == null)
            throw new HidRuntimeException("hid_open_path path is null");
        Pointer ret = INSTANCE.hid_open_path(path);
        if (ret == null)
            throw new HidRuntimeException("hid_open_path returned null");
        return ret;
    }

    synchronized public void sendFeatureReport(Pointer device, Buffer data) {
        int i = INSTANCE.hid_send_feature_report(device, data, data.size());
        if (i < 0)
            throw new RuntimeException("hid_send_feature_report returned " + i);
    }

    private void getFeatureReport(Pointer device, DectStatus data) {
        int i = INSTANCE.hid_get_feature_report(device, data, data.size() );

        if (i < 0)
            throw new HidRuntimeException("hid_get_feature_report returned " + i);
    }

    static private String pad(String p) {
        if (p.length() >= 2)
            return p;
        else
            return pad("0" + p);
    }

    synchronized public DectStatus getFeatureReport(Pointer device) {
        DectStatus dectStatus = new DectStatus();
        INSTANCE.hid_get_feature_report(device, dectStatus, dectStatus.size());
        return dectStatus;
    }

    synchronized public void write(Pointer device, Buffer data) {
        int i = INSTANCE.hid_write(device, data, data.size());
        if (i < 0)
            throw new RuntimeException();
    }

    synchronized public void hidClose(Pointer device) {
        INSTANCE.hid_close(device);
    }

    synchronized public void hidExit() {
        INSTANCE.hid_exit();
    }
}
