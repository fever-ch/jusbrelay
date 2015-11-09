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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class NativeTest {
    final Logger logger = LoggerFactory.getLogger(NativeTest.class);

    private HidApiNative han;

    @Before
    public void prepare() throws Exception {
        han = HidApiNativeDriver.newInstance();
        han.hid_init();
    }

    @After
    public void dispose() {
        han.hid_exit();
    }

    protected void crawlOverDevices(Function<HidDeviceInfoStructure, Void> f) {

        HidDeviceInfoStructure enu = han.hid_enumerate((short) 0, (short) 0);

        HidDeviceInfoStructure current = enu;
        boolean empty = true;
        while (current != null) {
            // loop on available devices
            empty = false;
            f.apply(current);
            current = current.next;
        }
        if (empty) logger.warn("No USB HID device found. Test still pass");

        han.hid_free_enumeration(enu.getPointer());
    }

    @Test
    public void deviceEnumerationTest() {
        crawlOverDevices((c) -> null);
    }


    @Test
    public void deviceOpeningTest() {
        crawlOverDevices((c) -> {
            HidDevice device = han.hid_open_path(c.path);
            han.hid_close(device);
            return null;
        });
    }

}
