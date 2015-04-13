package ch.fever.jhidapi.jna;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * Created by rbarazzutti on 13/04/15.
 */
public class Device extends Structure implements Structure.ByReference {

    @Override
    protected List getFieldOrder() {
        return Arrays.asList(

        );
    }
}
