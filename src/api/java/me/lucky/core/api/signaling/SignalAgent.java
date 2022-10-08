package me.lucky.core.api.signaling;

import me.lucky.core.api.enumerations.config.RedisConfigEntry;
import me.lucky.core.api.signaling.signals.BaseSignal;
import me.lucky.core.api.utils.Config;
import me.lucky.core.api.utils.CoreFactory;
import me.lucky.core.api.utils.SysLog;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class SignalAgent {
    private final ExecutorService executorService;
    private JedisPool jedisPool;
    private boolean isEnabled = true;

    private final List<BaseSignalListener> listeners;

    public SignalAgent() {
        this.listeners = new ArrayList<>();
        this.executorService = CoreFactory.getRunningCore().getExecutor();
    }

    public void initConnection() {
        SysLog sysLog = CoreFactory.getRunningCore().getSysLog();
        Config config = CoreFactory.getRunningCore().getCustomConfig();

        String redisHost = config.getEntryAsString(RedisConfigEntry.HOST);
        int redisPort = config.getEntryAsInteger(RedisConfigEntry.PORT);

        this.jedisPool = new JedisPool(this.buildPoolConfig(), redisHost, redisPort);
        try {
            this.jedisPool.preparePool();
        } catch (Exception e) {
            sysLog.LogException("Beim Öffnen des Pools ist ein Fehler aufgetreten!", e);
            this.disableAgent();
        }
    }

    public void sendSignal(BaseSignal signal) {
        this.sendSignal(signal.getSignalType().getChannel(), signal);
    }

    public void sendAnswerSignal(BaseSignal signal) {
        this.sendSignal(signal.getSignalType().getAnswerChannel(), signal);
    }

    private void sendSignal(String channel, BaseSignal signal) {
        if(this.checkIsDisabled()) {
            return;
        }

        CoreFactory.getRunningCore().getSysLog().LogDebug("Sending a Message on Channel " + channel);
        this.executorService.execute(() -> {
            CoreFactory.getRunningCore().getSysLog().LogDebug("Nachricht wird auf dem Channel " + channel + " gesendet!");
            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.publish(channel.getBytes(StandardCharsets.UTF_8), SignalAgent.serialize(signal));
            }
            CoreFactory.getRunningCore().getSysLog().LogDebug("Nachricht wurde auf dem Channel " + channel + " gesendet!");
        });
    }

    public <T extends BaseSignal> void sendSignal(T signal, Consumer<T> runnable) {
        if(this.checkIsDisabled()) {
            return;
        }

        BaseSignalListener listener = new BaseSignalListener<T>(signal.getSignalType(), signal.getClass()) {
            @Override
            protected void receivedMessage(T msg) {
                runnable.accept(msg);

                CoreFactory.getRunningCore().getSignalAgent().unregisterListener(this);
            }
        };

        this.registerListener(signal.getSignalType().getAnswerChannel(), listener);

        this.sendSignal(signal);
    }

    public void registerListener(SignalType signalType, BaseSignalListener listener) {
        this.registerListener(signalType.getChannel(), listener);
    }

    public void registerAnswerListener(SignalType signalType, BaseSignalListener listener) {
        this.registerListener(signalType.getAnswerChannel(), listener);
    }

    public void unregisterListener(BaseSignalListener listener) {
        if(this.checkIsDisabled()) {
            return;
        }

        listener.unsubscribe();
    }

    public void unregisterListeners() {
        for (BaseSignalListener listener :
                this.listeners) {
            listener.unsubscribe();
        }
    }

    public void disableAgent() {
        this.unregisterListeners();

        this.isEnabled = false;
        this.listeners.clear();
        this.jedisPool.close();
    }

    private void registerListener(String channel, BaseSignalListener listener) {
        this.executorService.execute(() -> {
            CoreFactory.getRunningCore().getSysLog().LogDebug("Channel " + channel + " wurde registriert!");
            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.subscribe(listener, channel.getBytes(StandardCharsets.UTF_8));
            }
        });

        this.listeners.add(listener);
    }

    private boolean checkIsDisabled() {
        if(!this.isEnabled) {
            CoreFactory.getRunningCore().getSysLog().LogError("Redis wurde deaktiviert und die Aktion kann nicht ausgeführt werden!");
        }

        return !this.isEnabled;
    }

    private JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }

    public static byte[] serialize(Object obj) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(obj);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Object deserialize(byte[] data) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            return is.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
