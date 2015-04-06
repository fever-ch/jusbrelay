import ch.fever.domotic4j.usbrelay.Controller;
import ch.fever.domotic4j.usbrelay.Relay;
import ch.fever.domotic4j.usbrelay.State;
import ch.fever.domotic4j.usbrelay.UsbRelayNative;
import ch.fever.domotic4j.usbrelay.decttech.Driver;

import java.util.LinkedList;
import java.util.List;

public class Test {
    UsbRelayNative usn = UsbRelayNative.INSTANCE;

    @org.junit.Test
    public void testDriver() throws InterruptedException {

        List<Relay> list = new LinkedList<>();


        for (Controller c : (new Driver()).listControllers()) {
            System.out.println(c.getIdentifier());
            for (Relay r : c.getRelays()) {
                list.add(r);
            }
        }

        for (Controller c : (new Driver()).listControllers()) {
            System.out.println(c.getIdentifier());
            for (Relay r : c.getRelays()) {
                list.add(r);
            }
        }


        for (Relay r : list)
            r.setState(State.ACTIVE);


        Thread.sleep(1000);

        for (Relay r : list)
            r.setState(State.INACTIVE);

        while(true){
            for (Relay r : list) {
                r.swapState();
                Thread.sleep(100);
            }
        }


    }

}
