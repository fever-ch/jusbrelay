package ch.fever.domotic4j.usbrelay.data;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Buffer extends Structure implements Structure.ByReference {

     public byte[] bytesArray;

    public Buffer(int len) {
        bytesArray = new byte[len];
    }

    public String toString() {
        return new String(bytesArray);
    }

    @Override
    protected List getFieldOrder() {
        return Arrays.asList("bytesArray");
    }
}
