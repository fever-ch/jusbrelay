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

import ch.fever.jhidapi.api.HidLibException;
import ch.fever.usbrelay.Controller;
import ch.fever.usbrelay.Relay;
import ch.fever.usbrelay.State;
import ch.fever.usbrelay.decttech.Driver;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class Test {


    @org.junit.Test
    public void testDriver() throws InterruptedException, HidLibException {


        List<Relay> list = new LinkedList<>();


        for (Controller c : (Driver.newInstance()).listControllers()) {
            list.addAll(Arrays.asList(c.getRelays()));
        }

        for (int i = 0; i < 3; i++) {
            for (Relay r : list) {
                Thread.sleep(10);
                r.swapState();
            }
        }
        list.get(1).setState(true);
        Thread.sleep(100);

        for (Relay r : list)
            r.setState(State.INACTIVE);


    }

}

