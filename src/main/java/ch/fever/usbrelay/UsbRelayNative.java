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

package ch.fever.usbrelay;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import ch.fever.usbrelay.data.*;

public interface UsbRelayNative extends Library {
    UsbRelayNative INSTANCE = (UsbRelayNative) Native.loadLibrary("hidapi", UsbRelayNative.class);


    HidDeviceInfoStructure hid_enumerate(short vendor_id, short product_id);

    void hid_free_enumeration(Pointer devs);

    int hid_write(Pointer device, Buffer.ByReference data, int len);

    Pointer hid_open_path(String path);

    int hid_get_feature_report(Pointer device, Buffer.ByReference data, int length);

    int hid_send_feature_report(Pointer device, Buffer.ByReference data, int length);

    void hid_close(Pointer device);

    void hid_exit();
}
