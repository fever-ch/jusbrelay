package ch.fever.jusbrelay;/*
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

import ch.fever.jhidapi.api.HidApiDriver;
import ch.fever.jhidapi.api.HidLibException;
import ch.fever.jhidapi.jna.HidApiNative;
import ch.fever.jhidapi.jna.HidDeviceInfoStructure;
import ch.fever.usbrelay.Controller;
import ch.fever.usbrelay.Relay;
import ch.fever.usbrelay.decttech.Driver;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class JUSBRelayTest {


    @org.junit.Test
    public void testDriver() throws InterruptedException, HidLibException {
        List<Relay> list = new LinkedList<>();

        for (Controller c : (Driver.newInstance()).listControllers()) {
            System.out.println(c.getIdentifier());
            list.addAll(Arrays.asList(c.getRelays()));
        }

        HidApiNative han = HidApiDriver.newInstance().hidApiNative;
        short vendorId = 0x16c0;
        short productId = 0x05df;
        HidDeviceInfoStructure his = han.hid_enumerate(vendorId, productId);


        for (int i = 0; i < 2; i++) {
            list.forEach(Relay::swapState);
        }
    }

}

