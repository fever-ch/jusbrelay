package ch.fever.usbrelay;

public interface Relay {
    void setState(State state);

    default void swapState() {
        setState(getState() == State.ACTIVE ? State.INACTIVE : State.ACTIVE);
    }

    State getState();
}
