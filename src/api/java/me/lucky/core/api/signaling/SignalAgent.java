package me.lucky.core.api.signaling;

import de.mlessmann.confort.antlr.JSONParser;
import me.lucky.core.api.enumerations.config.RedisConfigEntry;
import me.lucky.core.api.signaling.signals.BaseSignal;
import me.lucky.core.api.utils.Config;
import me.lucky.core.api.utils.CoreFactory;
import me.lucky.core.api.utils.SysLog;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class SignalAgent {
    private final ExecutorService executorService;
    private final JedisPool jedisPool;

    public SignalAgent() {
        SysLog sysLog = CoreFactory.getRunningCore().getSysLog();
        Config config = CoreFactory.getRunningCore().getCustomConfig();
        this.executorService = CoreFactory.getRunningCore().getExecutor();

        String redisHost = config.getEntryAsString(RedisConfigEntry.HOST);
        int redisPort = config.getEntryAsInteger(RedisConfigEntry.PORT);
        String redisUser = config.getEntryAsString(RedisConfigEntry.USER);
        String redisPassword = config.getEntryAsString(RedisConfigEntry.PASSWORD);

        this.jedisPool = new JedisPool(redisHost, redisPort, redisUser, redisPassword);
    }

    public void sendSignal(BaseSignal signal) {
        this.executorService.submit(() -> {
           try (Jedis jedis = this.jedisPool.getResource()) {
               jedis.publish(signal.getSignalType().getChannel(), JSONObject.valueToString(signal));
           }
        });
    }

    public <T> void sendSignal(BaseSignal signal, Consumer<T> runnable) {
        BaseSignalListener listener = new BaseSignalListener<T>(signal.getSignalType()) {
            @Override
            protected void receivedMessage(T msg) {
                runnable.accept(msg);

                this.unsubscribe(signal.getSignalType().getAnswerChannel());
            }
        };

        this.registerListener(signal.getSignalType().getAnswerChannel(), listener);

        this.sendSignal(signal);
    }

    public void registerListener(SignalType signalType, BaseSignalListener listener) {
        this.registerListener(signalType.getChannel(), listener);
    }

    private void registerListener(String channel, BaseSignalListener listener) {
        this.executorService.submit(() -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.subscribe(listener, channel);
            }
        });
    }
}
