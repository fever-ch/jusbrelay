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

package ch.fever.jhidapi.jna;

import ch.fever.jhidapi.common.Buffer;
import ch.fever.jhidapi.common.FeatureReport;
import com.sun.jna.*;

/**
 * HidApiNative: the JNA interface to HIDAPI.
 * <p>
 * note: The documentation of this interface is a port of the original the one of HIDAPI.
 */
public interface HidApiNative extends Library {
    int hid_init();

    void hid_exit();

    /**
     * Enumerate the HID Devices.
     *
     * This function returns a linked list of all the HID devices attached to the system which match vendor_id and product_id. If vendor_id is set to 0 then any vendor matches. If product_id is set to 0 then any product matches. If vendor_id and product_id are both set to 0, then all HID devices will be returned.
     *
     * @param vendor_id  The Vendor ID (VID) of the types of device to open.
     * @param product_id The Product ID (PID) of the types of device to open.
     * @return This function returns a pointer to a linked list of type struct hid_device, containing information about the HID devices attached to the system, or NULL in the case of failure. Free this linked list by calling {@link #hid_free_enumeration}.
     */
    HidDeviceInfoStructure hid_enumerate(short vendor_id, short product_id);

    /**
     * Free an enumeration Linked List.
     *
     * This function frees a linked list created by {@link #hid_enumerate}.
     *
     * @param devs Pointer to a list of struct_device returned from {@link #hid_enumerate}
     */
    void hid_free_enumeration(Pointer devs);

    Pointer hid_open(short vendorId, short productId, WString serial_number);

    Pointer hid_open_path(String path);

    int hid_write(Pointer device, Buffer data, int len);

    int hid_read_timeout(Pointer device, Buffer buff, int size, int milliseconds);

    int hid_read(Pointer device, Buffer buff, int size);

    int hid_set_nonblocking(Pointer device, int nonblock);

    int hid_send_feature_report(Pointer device, FeatureReport data, int length);

    /**
     * Get a feature report from a HID device.
     *
     * Set the first byte of data[] to the Report ID of the report to be read. Make sure to allow space for this extra byte in data[]. Upon return, the first byte will still contain the Report ID, and the report data will start in data[1].
     *
     * @param device A device handle returned from hid_open().
     * @param data   A buffer to put the read data into, including the Report ID. Set the first byte of data[] to the Report ID of the report to be read, or set it to zero if your device does not use numbered reports.
     * @param length The number of bytes to read, including an extra byte for the report ID. The buffer can be longer than the actual report.
     * @return This function returns the number of bytes read plus one for the report ID (which is still in the first byte), or -1 on error.
     */
    int hid_get_feature_report(Pointer device, FeatureReport data, int length);

    /**
     * Close a HID device.
     *
     * @param device A device handle returned from {@link #hid_open}.
     */
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