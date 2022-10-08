package me.lucky.core.api.signaling;

import me.lucky.core.api.signaling.signals.BaseSignal;
import me.lucky.core.api.utils.CoreFactory;
import redis.clients.jedis.BinaryJedisPubSub;

import java.nio.charset.StandardCharsets;

public abstract class BaseSignalListener<T extends BaseSignal> extends BinaryJedisPubSub {

    private final SignalType signalType;
    private final Class<?> tClass;

    protected BaseSignalListener(SignalType signalType, Class<?> tClass) {
        this.signalType = signalType;
        this.tClass = tClass;
    }

    @Override
    public void onMessage(byte[] channelByte, byte[] message) {
        CoreFactory.getRunningCore().getSysLog().LogDebug("Received a Message on Channel " +  new String(channelByte, StandardCharsets.UTF_8));

        CoreFactory.getRunningCore().getExecutor().execute(() -> {
            this.receivedMessage((T)SignalAgent.deserialize(message));
        });
    }

    protected void sendAnswer(T signal) {
        CoreFactory.getRunningCore().getSignalAgent().sendAnswerSignal(signal);
    }

    protected abstract void receivedMessage(T signal);
}
