package ch.fever.domotic4j.usbrelay;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import org.hid4java.jna.HidDeviceInfoStructure;
import org.hid4java.jna.WideStringBuffer;

public interface UsbRelayNative extends Library {
    UsbRelayNative INSTANCE = (UsbRelayNative) Native.loadLibrary("hidapi", UsbRelayNative.class);


    HidDeviceInfoStructure hid_enumerate(short vendor_id, short product_id);
    void hid_free_enumeration(Pointer devs);
    void hid_free_enumeration(HidDeviceInfoStructure devs);

    int hid_write(Pointer device, WideStringBuffer.ByReference data, int len);

    Pointer hid_open_path(String path);

    int hid_get_feature_report(Pointer device, WideStringBuffer.ByReference data, int length);
    void hid_close(Pointer device);

    void hid_exit();


}
