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

package ch.fever.jhidapi.jna;

import com.sun.jna.*;

public interface HidApiNative extends Library {
    int hid_init();

    void hid_exit();

    HidDeviceInfoStructure hid_enumerate(short vendor_id, short product_id);

    void hid_free_enumeration(Pointer devs);

    Pointer hid_open(short vendorId, short productId, WString serial_number);

    Pointer hid_open_path(String path);

    int hid_write(Pointer device, Buffer.ByReference data, int len);

    int hid_read_timeout(Pointer device, Buffer.ByReference buff, int size, int milliseconds);

    int hid_read(Pointer device, Buffer.ByReference buff, int size);

    int hid_set_nonblocking(Pointer device, int nonblock);

    int hid_send_feature_report(Pointer device, FeatureReport.ByReference data, int length);

    int hid_get_feature_report(Pointer device, FeatureReport.ByReference data, int length);

    void hid_close(Pointer device);

    int hid_get_manufacturer_string(Pointer device, WString wchar_t, SizeT maxlen);

    int hid_get_product_string(Pointer device, WString wchar_t, SizeT maxlen);

    int hid_get_serial_number_string(Pointer device, WString wchar_t, SizeT maxlen);

    int hid_get_indexed_string(Pointer device, int StringIndex, WString wchar_t, SizeT maxlen);

    WString hid_error(Pointer device);
}


class SizeT extends IntegerType {
    public SizeT() {
        this(0);
    }

    public SizeT(long value) {
        super(Native.SIZE_T_SIZE, value, true);
    }
}