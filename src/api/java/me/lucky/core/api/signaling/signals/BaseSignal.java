package me.lucky.core.api.signaling.signals;

import me.lucky.core.api.signaling.SignalType;
import redis.clients.jedis.JedisPubSub;

import java.io.Serializable;

public abstract class BaseSignal<T> extends JedisPubSub {

    private final SignalType signalType;
    private final String target;
    private final SignalTargetType targetType;

    public BaseSignal(SignalType signalType, SignalTargetType targetType, String target) {
        this.signalType = signalType;
        this.targetType = targetType;
        this.target = target;
    }

    public SignalType getSignalType() {
        return signalType;
    }

    public String getTarget() {
        return target;
    }

    public SignalTargetType getTargetType() {
        return targetType;
    }
}
