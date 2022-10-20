package me.lucky.core.spigot;

import me.lucky.core.api.ICore;
import me.lucky.core.api.database.DatabaseManager;
import me.lucky.core.api.signaling.SignalAgent;
import me.lucky.core.api.signaling.SignalType;
import me.lucky.core.api.utils.*;
import me.lucky.core.api.utils.CoreVersion;
import me.lucky.core.api.signaling.signals.VersionSignal;
import me.lucky.core.spigot.utils.SpigotMessages;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.*;

@SuppressWarnings("unused")
public class SpigotCore extends JavaPlugin implements ICore {

    private final Config config;
    private final SpigotMessages messages;
    private final SysLog sysLog;
    private final DatabaseManager databaseManager;
    private final ExecutorService executorService;
    private final SignalAgent signalAgent;

    public SpigotCore() {
        CoreFactory.registerInstance(this);
        CoreFactory.registerServer(this.getServer());

        this.config = new Config();
        this.messages = new SpigotMessages();
        this.sysLog = new SysLog();
        this.databaseManager = new DatabaseManager();

        this.executorService = Executors.newCachedThreadPool();
        this.signalAgent = new SignalAgent();
    }

    @Override
    public void onEnable() {
        this.sysLog.LogInformation("Starting to load the Core");

        this.sysLog.LogDebug("Loading the config");
        this.config.LoadData();
        this.sysLog.LogDebug("The config was loaded");

        this.sysLog.LogDebug("Loading the messages");
        this.messages.refreshMessages();
        this.sysLog.LogDebug("The messages we're loaded");

        this.signalAgent.initConnection();

        this.sysLog.LogDebug("Die Nachrichten zwischen Bungee und Spigot werden registriert");
        this.registerSignal();
        this.sysLog.LogDebug("Die Nachrichten zwischen Bungee und Spigot wurden registriert");

        this.sysLog.LogInformation("Successfully loaded the Core");
    }

    @Override
    public void onDisable() {
        this.sysLog.LogInformation("Starting to unload the Core");

        this.signalAgent.disableAgent();

        this.sysLog.LogInformation("Successfully unloaded the Core");
    }

    private void registerSignal() {
        VersionSignal signal = new VersionSignal(SignalType.CHECK_VERSION, "");
        signal.setVersion(CoreVersion.VERSION);

        this.signalAgent.sendSignal(signal, (version) -> {
            this.sysLog.LogDebug("Current Version: " + CoreVersion.VERSION);
            this.sysLog.LogDebug("Bungee Version: " + version.getVersion());
            if(!version.getVersion().equals(CoreVersion.VERSION)) {
                this.sysLog.LogError("Die Version des Cores stimmt nicht mit der Version des Proxys überein." +
                        "Aus diesem Grund wird die Kommunikation mit dem Proxy deaktiviert!");
                this.signalAgent.disableAgent();
            }
        });
    }

    @Override
    public Messages getMessages() {
        return this.messages;
    }

    @Override
    public Config getCustomConfig() {
        return this.config;
    }

    @Override
    public SysLog getSysLog() {
        return this.sysLog;
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    @Override
    public SignalAgent getSignalAgent() {
        return this.signalAgent;
    }

    @Override
    public ExecutorService getExecutor() {
        return this.executorService;
    }
}
