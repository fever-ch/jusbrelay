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
import com.sun.jna.Library;
import com.sun.jna.WString;

/**
 * HidApiNative: the JNA interface to HIDAPI.
 * <p>
 * note: The documentation of this interface is a port of the original the one of HIDAPI.
 */
public interface HidApiNative extends Library {
    /**
     * Initialize the HIDAPI library.
     *
     * This function initializes the HIDAPI library. Calling it is not strictly necessary, as it will be called
     * automatically by {@link #hid_enumerate}, {@link #hid_open} and
     * {@link #hid_open_path} if it is needed. This function should be called at the beginning of execution
     * however, if there is a chance of HIDAPI handles being opened by different threads simultaneously.
     *
     * @return This function returns 0 on success and -1 on error.
     */
    int hid_init();

    /**
     * Finalize the HIDAPI library.
     *
     * This function frees all of the static data associated with HIDAPI. It should be called at the end of execution to
     * avoid memory leaks.
     *
     * @return This function returns 0 on success and -1 on error.
     */
    int hid_exit();

    /**
     * Enumerate the HID Devices.
     *
     * This function returns a linked list of all the HID devices attached to the system which match vendor_id and
     * product_id. If vendor_id is set to 0 then any vendor matches. If product_id is set to 0 then any product matches.
     * If vendor_id and product_id are both set to 0, then all HID devices will be returned.
     *
     * @param vendor_id  The Vendor ID (VID) of the types of device to open.
     * @param product_id The Product ID (PID) of the types of device to open.
     * @return This function returns a pointer to a linked list of type struct hid_device, containing information about
     * the HID devices attached to the system, or NULL in the case of failure. Free this linked list by calling
     * {@link #hid_free_enumeration}.
     */
    HidDeviceInfoStructure.ByReference hid_enumerate(short vendor_id, short product_id);

    /**
     * Free an enumeration Linked List.
     *
     * This function frees a linked list created by {@link #hid_enumerate}.
     *
     * @param devs Pointer to a list of struct_device returned from {@link #hid_enumerate}
     */
    void hid_free_enumeration(HidDeviceInfoStructure.ByReference devs);

    /**
     * Open a HID device using a Vendor ID (VID), Product ID (PID) and optionally a serial number.
     *
     * If serial_number is NULL, the first device with the specified VID and PID is opened.
     *
     * @param vendor_id     The Vendor ID (VID) of the device to open.
     * @param product_id    The Product ID (PID) of the device to open.
     * @param serial_number The Serial Number of the device to open (Optionally NULL).
     * @return This function returns a pointer to a hid_device object on success or NULL on failure.
     */
    HidDevice hid_open(short vendor_id, short product_id, WString serial_number);

    /**
     * Open a HID device by its path name.
     *
     * The path name be determined by calling {@link #hid_enumerate}, or a platform-specific path name can be used
     * (eg: /dev/hidraw0 on Linux).
     *
     * @param path The path name of the device to open
     * @return This function returns a pointer to a hid_device object on success or NULL on failure.
     */
    HidDevice hid_open_path(String path);

    /**
     * Write an Output report to a HID device.
     *
     * The first byte of data[] must contain the Report ID. For devices which only support a single report, this must be
     * set to 0x0. The remaining bytes contain the report data. Since the Report ID is mandatory, calls to
     * {@link #hid_write} will always contain one more byte than the report contains. For example, if a hid report is 16
     * bytes long, 17 bytes must be passed to {@link #hid_write}, the Report ID (or 0x0, for devices with a single
     * report), followed by the report data (16 bytes). In this example, the length passed in would be 17.
     *
     * {@link #hid_write} will send the data on the first OUT endpoint, if one exists. If it does not, it will send the
     * data through the Control Endpoint (Endpoint 0).
     *
     * @param device A device handle returned from  {@link #hid_open}.
     * @param data   The data to send, including the report number as the first byte.
     * @param length The length in bytes of the data to send.
     * @return This function returns the actual number of bytes written and -1 on error.
     */
    int hid_write(HidDevice device, Buffer data, int length);

    /**
     * Read an Input report from a HID device with timeout.
     *
     * Input reports are returned to the host through the INTERRUPT IN endpoint. The first byte will contain the Report
     * number if the device uses numbered reports.
     *
     * @param device       A device handle returned from {@link #hid_open}.
     * @param data         A buffer to put the read data into.
     * @param length       The number of bytes to read. For devices with multiple reports, make sure to read an extra
     *                     byte for the report number.
     * @param milliseconds timeout in milliseconds or -1 for blocking wait.
     * @return This function returns the actual number of bytes read and -1 on error. If no packet was available to be
     * read within the timeout period, this function returns 0.
     */
    int hid_read_timeout(HidDevice device, Buffer data, int length, int milliseconds);

    /**
     * Read an Input report from a HID device.
     *
     * Input reports are returned to the host through the INTERRUPT IN endpoint. The first byte will contain the Report
     * number if the device uses numbered reports.
     *
     * @param device A device handle returned from {@link #hid_open}.
     * @param data   A buffer to put the read data into.
     * @param length The number of bytes to read. For devices with multiple reports, make sure to read an extra byte for
     *               the report number.
     * @return This function returns the actual number of bytes read and -1 on error. If no packet was available to be
     * read and the handle is in non-blocking mode, this function returns 0.
     */
    int hid_read(HidDevice device, Buffer data, int length);

    /**
     * Set the device handle to be non-blocking.
     *
     * In non-blocking mode calls to {@link #hid_read} will return immediately with a value of 0 if there is no data to
     * be read. In blocking mode, {@link #hid_read} will wait (block) until there is data to read before returning.
     *
     * Nonblocking can be turned on and off at any time.
     *
     * @param device   A device handle returned from {@link #hid_open}.
     * @param nonblock enable or not the nonblocking reads
     *                 <ul>
     *                 <li>1 to enable nonblocking.</li>
     *                 <li>0 to disable nonblocking.</li>
     *                 </ul>
     * @return This function returns 0 on success and -1 on error.
     */
    int hid_set_nonblocking(HidDevice device, int nonblock);

    /**
     * Send a Feature report to the device.
     *
     * Feature reports are sent over the Control endpoint as a Set_Report transfer. The first byte of data[] must
     * contain the Report ID. For devices which only support a single report, this must be set to 0x0. The remaining
     * bytes contain the report data. Since the Report ID is mandatory, calls to {@link #hid_send_feature_report} will
     * always contain one more byte than the report contains. For example, if a hid report is 16 bytes long, 17 bytes
     * must be passed to {@link #hid_send_feature_report}: the Report ID (or 0x0, for devices which do not use numbered
     * reports), followed by the report data (16 bytes). In this example, the length passed in would be 17.
     *
     * @param device A device handle returned from {@link #hid_open}.
     * @param data   The data to send, including the report number as the first byte.
     * @param length The length in bytes of the data to send, including the report number.
     * @return This function returns the actual number of bytes written and -1 on error.
     */
    int hid_send_feature_report(HidDevice device, FeatureReport data, int length);

    /**
     * Get a feature report from a HID device.
     *
     * Set the first byte of data[] to the Report ID of the report to be read. Make sure to allow space for this extra
     * byte in data[]. Upon return, the first byte will still contain the Report ID, and the report data will start in
     * data[1].
     *
     * @param device A device handle returned from {@link #hid_open}.
     * @param data   A buffer to put the read data into, including the Report ID. Set the first byte of data[] to the
     *               Report ID of the report to be read, or set it to zero if your device does not use numbered reports.
     * @param length The number of bytes to read, including an extra byte for the report ID. The buffer can be longer
     *               than the actual report.
     * @return This function returns the number of bytes read plus one for the report ID (which is still in the first
     * byte), or -1 on error.
     */
    int hid_get_feature_report(HidDevice device, FeatureReport data, int length);

    /**
     * Close a HID device.
     *
     * @param device A device handle returned from {@link #hid_open}.
     */
    void hid_close(HidDevice device);

    /**
     * Get The Manufacturer String from a HID device.
     *
     * @param device A device handle returned from {@link #hid_open}.
     * @param string A wide string buffer to put the data into.
     * @param maxlen The length of the buffer in multiples of wchar_t.
     * @return This function returns 0 on success and -1 on error.
     */
    int hid_get_manufacturer_string(HidDevice device, WString string, SizeT maxlen);

    /**
     * Get The Product String from a HID device.
     *
     * @param device A device handle returned from {@link #hid_open}.
     * @param string A wide string buffer to put the data into.
     * @param maxlen The length of the buffer in multiples of wchar_t.
     * @return This function returns 0 on success and -1 on error.
     */
    int hid_get_product_string(HidDevice device, WString string, SizeT maxlen);

    /**
     * Get The Serial Number String from a HID device.
     *
     * @param device A device handle returned from {@link #hid_open}.
     * @param string A wide string buffer to put the data into.
     * @param maxlen The length of the buffer in multiples of wchar_t.
     * @return This function returns 0 on success and -1 on error.
     */
    int hid_get_serial_number_string(HidDevice device, WString string, SizeT maxlen);

    /**
     * Get a string from a HID device, based on its string index.
     *
     * @param device       A device handle returned from {@link #hid_open}.
     * @param string_index The index of the string to get.
     * @param string       A wide string buffer to put the data into.
     * @param maxlen       The length of the buffer in multiples of wchar_t.
     * @return This function returns 0 on success and -1 on error.
     */
    int hid_get_indexed_string(HidDevice device, int string_index, WString string, SizeT maxlen);

    /**
     * Get a string describing the last error which occurred.
     *
     * @param device A device handle returned from {@link #hid_open}.
     * @return This function returns a string containing the last error which occurred or NULL if none has occurred.
     */
    WString hid_error(HidDevice device);
}
