package ch.fever.domotic4j.usbrelay.decttech;


import ch.fever.domotic4j.usbrelay.Controller;
import ch.fever.domotic4j.usbrelay.Relay;
import ch.fever.domotic4j.usbrelay.State;
import ch.fever.domotic4j.usbrelay.UsbRelayNative;
import ch.fever.domotic4j.usbrelay.data.Buffer;
import ch.fever.domotic4j.usbrelay.data.HidDeviceInfoStructure;
import com.sun.jna.Pointer;

import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Driver implements ch.fever.domotic4j.usbrelay.Driver {
    final private UsbRelayNative usn = UsbRelayNative.INSTANCE;


    private class DectController implements Controller {

        final private String identifier;
        final private Pattern pattern = Pattern.compile("^USBRelay(\\d+)$");
        final int nrRelays;

        final private String path;

        protected <T> T apply(Function<Pointer, T> f) {
            Pointer pointer = usn.hid_open_path(path);
            T ret = f.apply(pointer);
            usn.hid_close(pointer);
            return ret;
        }

        public DectController(HidDeviceInfoStructure infoStructure) {
            Matcher matcher = pattern.matcher(infoStructure.productString.toString());

            nrRelays = matcher.matches() ? Integer.parseInt(matcher.group(1)) : 0;

            path = infoStructure.path;

            identifier = apply(pp ->
            {
                DectStatus dectStatus = new DectStatus();
                usn.hid_get_feature_report(pp, dectStatus, dectStatus.size());
                return dectStatus.getIdentifier();
            });
        }


        public String getIdentifier() {
            return identifier;
        }


        public Relay[] getRelays() {
            DectRelay relays[] = new DectRelay[nrRelays];
            for (byte i = 0; i < nrRelays; i++)
                relays[i] = new DectRelay(i);
            return relays;
        }

        private class DectRelay implements Relay {
            final private byte id;

            private DectRelay(byte id) {
                this.id = id;
            }

            @Override
            public void setState(State state) {
                byte st = (byte) (state == State.ACTIVE ? 0xff : 0xfd);
                Buffer buf = new Buffer(9);
                buf.bytesArray[1] = st;
                buf.bytesArray[2] = (byte) (id + 1);

                apply(p -> usn.hid_write(p, buf, buf.size()));
            }

            @Override
            public State getState() {
                return apply(p ->
                {
                    DectStatus dectStatus = new DectStatus();
                    usn.hid_get_feature_report(p, dectStatus, dectStatus.size());
                    short state = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN) ? dectStatus.state : Short.reverseBytes(dectStatus.state);
                    return ((state >> id) & 1) == 1 ? State.ACTIVE : State.INACTIVE;
                });

            }
        }
    }


    @Override
    public List<Controller> listControllers() {
        List<Controller> list = new LinkedList<>();
        short vendor_id = 0x16c0;
        short product_id = 0x05df;

        HidDeviceInfoStructure penum = usn.hid_enumerate(vendor_id, product_id);
        HidDeviceInfoStructure p = penum;

        while (p != null) {
            list.add(new DectController(p));
            p = p.next;
        }

        usn.hid_free_enumeration(penum.getPointer());

        usn.hid_exit();
        return list;
    }
}
