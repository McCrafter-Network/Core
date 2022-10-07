package me.lucky.core.api.signaling;

import me.lucky.core.api.signaling.signals.BaseSignal;
import me.lucky.core.api.utils.CoreFactory;
import org.json.JSONObject;
import redis.clients.jedis.JedisPubSub;

public abstract class BaseSignalListener<T> extends JedisPubSub {

    private final SignalType signalType;

    public BaseSignalListener(SignalType signalType) {
        this.signalType = signalType;
    }

    @Override
    public void onMessage(String channel, String message) {
        if(channel != signalType.getChannel()) {
            return;
        }

        Object parsed = JSONObject.stringToValue(message);

        if(parsed == null) {
            CoreFactory.getRunningCore().getSysLog().LogError("Typemissmatch!");
            return;
        }

        this.receivedMessage((T)parsed);
    }

    protected abstract void receivedMessage(T signal);
}
