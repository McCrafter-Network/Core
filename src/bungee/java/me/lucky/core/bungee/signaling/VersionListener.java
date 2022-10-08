package me.lucky.core.bungee.signaling;

import me.lucky.core.api.signaling.BaseSignalListener;
import me.lucky.core.api.signaling.SignalType;
import me.lucky.core.api.utils.CoreVersion;
import me.lucky.core.api.signaling.signals.VersionSignal;

public class VersionListener extends BaseSignalListener<VersionSignal> {
    public VersionListener() {
        super(SignalType.CHECK_VERSION, VersionSignal.class);
    }

    @Override
    protected void receivedMessage(VersionSignal signal) {
        this.sendAnswer(new VersionSignal(SignalType.CHECK_VERSION, "", CoreVersion.VERSION));
    }
}
