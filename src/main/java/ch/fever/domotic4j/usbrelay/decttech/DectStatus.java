package ch.fever.domotic4j.usbrelay.decttech;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class DectStatus extends Structure implements Structure.ByReference {
    public byte idx[] = new byte[5];
    public byte unused;
    public short state;

    public int size() {
        return idx.length + 1 + 2;
    }

    @Override
    protected List getFieldOrder() {
        return Arrays.asList(
                "idx", "unused", "state"
        );
    }

    public String getIdentifier() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < idx.length; i++) {
            sb.append(Integer.toHexString(0x100 | (0xff & idx[i])).substring(1).toUpperCase());

            if (i != idx.length - 1)
                sb.append(":");
        }
        return sb.toString();
    }
}

