package ch.fever.domotic4j.usbrelay;

public interface Controller {
    String getIdentifier();

    Relay[] getRelays();

}
