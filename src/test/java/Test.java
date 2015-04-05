import ch.fever.domotic4j.usbrelay.UsbRelayNative;
import ch.fever.domotic4j.usbrelay.data.Buffer;
import ch.fever.domotic4j.usbrelay.data.HidDeviceInfoStructure;
import com.sun.jna.Pointer;

public class Test {
    UsbRelayNative usn = UsbRelayNative.INSTANCE;

    @org.junit.Test
    public void doTest() {

        short vendor_id = 0x16c0;
        short product_id = 0x05df;
        HidDeviceInfoStructure penum = usn.hid_enumerate(vendor_id, product_id);
        HidDeviceInfoStructure p = penum;

        while (p != null) {


            Pointer pp = usn.hid_open_path(p.path);

            usn.hid_get_feature_report(pp, new Buffer(9), 1);
            for (int i = 1; i <= 8; i++)
                operate_relay(pp, (byte) i, (byte) 0xff);

            for (int i = 1; i <= 8; i++)
                operate_relay(pp, (byte) i, (byte) 0xfd);
            usn.hid_close(pp);
            p = p.next;

        }


        usn.hid_free_enumeration(p);
        usn.hid_exit();
    }


    int operate_relay(Pointer handle, byte relay, byte state) {
        Buffer buf = new Buffer(9);
        buf.buffer[0] = 0x0;
        buf.buffer[1] = state;
        buf.buffer[2] = relay;
        for (int i = 3; i <= 8; i++)
            buf.buffer[i] = 0x0;
        return usn.hid_write(handle, buf, buf.size());

    }
}
