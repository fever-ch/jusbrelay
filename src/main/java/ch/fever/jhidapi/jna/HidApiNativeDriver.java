package ch.fever.jhidapi.jna;


import com.sun.jna.Native;

/**
 * Created by rbarazzutti on 09/11/15.
 */
public class HidApiNativeDriver {
    static public HidApiNative newInstance() throws Exception {
        String path = System.getProperty("os.name").toLowerCase().contains("linux") ? "hidapi-libusb" : "hidapi";
        try {
            HidApiNative han = (HidApiNative)
                    Native.loadLibrary(path, HidApiNative.class);
            return han;
        } catch (UnsatisfiedLinkError e) {
            throw new Exception("Unable to load the HidApi library from the library search paths");
        }
    }

}
