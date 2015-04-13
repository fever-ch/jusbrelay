package ch.fever.usbrelay.decttech;

import ch.fever.jhidapi.jna.FeatureReport;

public class DectFeatureReport extends FeatureReport {

    public DectFeatureReport(){
        super(10);
    }

    public int getState(){
        return bytesArray[6];
    }

    String getIdentifier(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(Integer.toHexString(0x100 | (0xff & bytesArray[i])).substring(1).toUpperCase());

            if (i != 4)
                sb.append(":");
        }
        return sb.toString();
    }

}
