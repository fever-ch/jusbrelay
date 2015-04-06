package ch.fever.domotic4j.usbrelay;

public interface Relay {
    void setState(State state);

    State getState();
}
