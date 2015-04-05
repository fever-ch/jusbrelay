package ch.fever.domotic4j.usbrelay.data;

import com.sun.jna.Structure;

import java.util.Collections;
import java.util.List;

public class Buffer extends Structure implements Structure.ByReference {

    public byte[] buffer = null;

    public Buffer(int len) {
        buffer = new byte[len];
    }

    @Override
    protected List getFieldOrder() {
        return Collections.singletonList("buffer");
    }
}
