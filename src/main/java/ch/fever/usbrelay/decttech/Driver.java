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

package ch.fever.usbrelay.decttech;


import ch.fever.usbrelay.Controller;
import ch.fever.usbrelay.Relay;
import ch.fever.usbrelay.State;
import ch.fever.usbrelay.jna.Buffer;
import ch.fever.usbrelay.jna.DeviceInfoStructure;
import ch.fever.usbrelay.jna.HidApiDriver;
import com.sun.jna.Pointer;

import java.nio.ByteOrder;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Driver implements ch.fever.usbrelay.Driver {
    final private HidApiDriver hidApi = new HidApiDriver();

    private class DectController implements Controller {

        final private String identifier;
        final private Pattern pattern = Pattern.compile("^USBRelay(\\d+)$");
        final int nrRelays;

        final private String path;

        protected <T> T apply(Function<Pointer, T> f) {
            Pointer pointer = hidApi.openPath(path);

            try {
                return f.apply(pointer);
            } finally {
                hidApi.hidClose(pointer);
            }
        }

        public DectController(DeviceInfoStructure infoStructure) {
            Matcher matcher = pattern.matcher(infoStructure.getProductString());

            nrRelays = matcher.matches() ? Integer.parseInt(matcher.group(1)) : 0;

            path = infoStructure.getPath();

            identifier = apply(pp -> hidApi.getFeatureReport(pp).getIdentifier());
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

                apply(p -> {
                    hidApi.write(p, buf);
                    return 1;
                });
            }

            @Override
            public State getState() {
                return apply(p ->
                {
                    DectStatus dectStatus = hidApi.getFeatureReport(p);
                    short state = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN) ? dectStatus.state : Short.reverseBytes(dectStatus.state);
                    return ((state >> id) & 1) == 1 ? State.ACTIVE : State.INACTIVE;
                });

            }
        }
    }


    @Override
    public List<Controller> listControllers() {
        short vendorId = 0x16c0;
        short productId = 0x05df;

        return hidApi.getEnumeration(vendorId, productId).stream().map(DectController::new).collect(Collectors.toList());
    }
}
