package me.lucky.core.api.signaling.signals;

import me.lucky.core.api.signaling.SignalType;

import java.io.Serializable;

public abstract class BaseSignal<T> implements Serializable {

    private SignalType signalType;
    private String target;

    @Deprecated
    public BaseSignal() {

    }

    public BaseSignal(SignalType signalType, String target) {
        this.signalType = signalType;
        this.target = target;
    }

    public SignalType getSignalType() {
        return signalType;
    }

    public String getTarget() {
        return target;
    }

    public void setSignalType(SignalType signalType) {
        this.signalType = signalType;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
