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

package ch.fever.usbrelay.decttech;

import ch.fever.jhidapi.api.DeviceInfoStructure;
import ch.fever.jhidapi.api.HidApiDriver;
import ch.fever.jhidapi.common.Buffer;
import ch.fever.jhidapi.common.FeatureReport;
import ch.fever.jhidapi.jna.HidDevice;
import ch.fever.jhidapi.jna.JHidApiException;
import ch.fever.usbrelay.Controller;
import ch.fever.usbrelay.Relay;
import ch.fever.usbrelay.State;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Driver implements ch.fever.usbrelay.Driver {
    final private HidApiDriver hidApi;

    private Driver(HidApiDriver hidApiDriver) {
        this.hidApi = hidApiDriver;
    }

    static public Driver newInstance() throws JHidApiException {
        return new Driver(HidApiDriver.newInstance());
    }

    private class DectController implements Controller {

        final private String identifier;
        final private Pattern pattern = Pattern.compile("^USBRelay(\\d+)$");
        final int nrRelays;

        final private String path;

        protected <T> T apply(Function<HidDevice, T> f) {
            HidDevice pointer = hidApi.openPath(path);

            try {
                return f.apply(pointer);
            } finally {
                hidApi.close(pointer);
            }
        }

        public DectController(DeviceInfoStructure infoStructure) {
            Matcher matcher = pattern.matcher(infoStructure.getProductString());

            nrRelays = matcher.matches() ? Integer.parseInt(matcher.group(1)) : 0;

            path = infoStructure.getPath();
            // DevicePointer pt = infoStructure.openDevice();

            identifier = apply(pp ->
            {
                FeatureReport fp = new FeatureReport(8);
                //pt.getFeatureReport(fp);
                hidApi.getFeatureReport(pp, fp);

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 5; i++) {
                    sb.append(Integer.toHexString(0x100 | (0xff & fp.bytesArray[i])).substring(1).toUpperCase());

                    if (i != 4)
                        sb.append(":");
                }
                return sb.toString();
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
                Buffer buf = new Buffer(9);
                buf.getBytesArray()[1] = (byte) (state == State.ACTIVE ? 0xff : 0xfd);
                buf.getBytesArray()[2] = (byte) (id + 1);

                apply(p -> {
                    hidApi.write(p, buf);
                    return 1;
                });
            }

            @Override
            public Optional<State> getState() {
                return apply(p ->
                {
                    FeatureReport fp = new FeatureReport(8);
                    hidApi.getFeatureReport(p, fp);

                    return Optional.of(((fp.bytesArray[7] >> id) & 1) == 1 ? State.ACTIVE : State.INACTIVE);
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
