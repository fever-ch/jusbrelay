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

import com.sun.jna.Native;

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
