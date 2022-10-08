package me.lucky.core.api.signaling.signals;

import me.lucky.core.api.signaling.SignalType;
import me.lucky.core.api.signaling.signals.BaseSignal;

public class VersionSignal extends BaseSignal {

    private String version;

    public VersionSignal(SignalType signalType, String target) {
        super(signalType, target);
    }

    public VersionSignal(SignalType signalType, String target, String version) {
        super(signalType, target);
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
