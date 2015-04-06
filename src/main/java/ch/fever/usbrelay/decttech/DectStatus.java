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

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class DectStatus extends Structure implements Structure.ByReference {
    public byte id[] = new byte[5];
    public byte unused;
    public short state;


    public int size() {
        return id.length + 1 + 2;
    }

    @Override
    protected List getFieldOrder() {
        return Arrays.asList(
                "id", "unused", "state"
        );
    }

    public String getIdentifier() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < id.length; i++) {
            sb.append(Integer.toHexString(0x100 | (0xff & id[i])).substring(1).toUpperCase());

            if (i != id.length - 1)
                sb.append(":");
        }
        return sb.toString();
    }
}

