package ch.fever.usbrelay;

public interface Controller {
    String getIdentifier();

    Relay[] getRelays();

}
