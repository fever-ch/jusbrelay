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

package ch.fever.jhidapi.common;

import com.sun.jna.Structure;

import java.util.Collections;
import java.util.List;


public class Buffer extends Structure implements Structure.ByReference {

    public byte bytesArray[];

    public Buffer(byte array[]) {
        super(ALIGN_NONE);
        bytesArray = array;
    }

    public byte[] getBytesArray() {
        return bytesArray;
    }


    public Buffer(int len) {
        this(new byte[len]);
    }

    @Override
    protected List getFieldOrder() {
        return Collections.singletonList(
                "bytesArray"
        );
    }
}
