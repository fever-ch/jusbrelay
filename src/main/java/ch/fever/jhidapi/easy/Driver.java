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

import ch.fever.jhidapi.jna.HidApiNative;
import ch.fever.jhidapi.jna.HidDeviceInfoStructure;
import com.sun.jna.Native;

import java.util.LinkedList;
import java.util.List;


public class Driver {
    final private HidApiNative INSTANCE = (HidApiNative)
            Native.loadLibrary(System.getProperty("os.name").toLowerCase().contains("linux") ? "hidapi-libusb" : "hidapi", HidApiNative.class);

    public List<DeviceInfo> getDevices(int vendorId, int productId) {


        List<DeviceInfo> list = new LinkedList<>();
        HidDeviceInfoStructure penum = INSTANCE.hid_enumerate((short) vendorId, (short) productId);

        HidDeviceInfoStructure p = penum;

        while (p != null) {
            System.out.println("enum");
            list.add(new DeviceInfo(p));

            p = p.next;
        }

        if (penum != null)
            INSTANCE.hid_free_enumeration(penum.getPointer());
        return list;

    }

    public Driver() {
        INSTANCE.hid_init();
    }

    public void finalize() {
        INSTANCE.hid_exit();
    }
}
