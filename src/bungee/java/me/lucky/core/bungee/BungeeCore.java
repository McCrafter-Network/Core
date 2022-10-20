package me.lucky.core.bungee;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import me.lucky.core.api.ICore;
import me.lucky.core.api.database.DatabaseManager;
import me.lucky.core.api.enumerations.config.DatabaseConfigEntry;
import me.lucky.core.api.signaling.SignalAgent;
import me.lucky.core.api.signaling.SignalType;
import me.lucky.core.api.utils.*;
import me.lucky.core.bungee.commands.ReloadCommand;
import me.lucky.core.bungee.events.ConnectionEvents;
import me.lucky.core.bungee.signaling.VersionListener;
import me.lucky.core.bungee.utils.BungeeMessages;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@Plugin(id = "bungeecore", name = "Mc Crafter TV - Core", version = "0.7-SNAPSHOT", authors = {"LuckyDev (Kimon Meier)"})
public class BungeeCore implements ICore {
    private final ProxyServer server;
    private final Logger logger;
    private final BungeeMessages messages;
    private final Config config;
    private final SysLog sysLog;
    private final DatabaseManager dbManager;
    private final ExecutorService executorService;
    private final SignalAgent signalAgent;


    @Inject
    public BungeeCore(ProxyServer server, Logger logger) {
        CoreFactory.registerInstance(this);
        CoreFactory.registerServer(server);

        this.server = server;
        this.logger = logger;

        this.messages = new BungeeMessages();
        this.config = new Config();
        this.sysLog = new SysLog();
        this.dbManager = new DatabaseManager();

        this.executorService = Executors.newCachedThreadPool();
        this.signalAgent = new SignalAgent();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.sysLog.LogInformation("Starting to load the Core");

        this.sysLog.LogDebug("Loading the config");
        this.config.LoadData();
        this.sysLog.LogDebug("The config was loaded");

        this.sysLog.LogDebug("Loading the messages");
        this.messages.refreshMessages();
        this.sysLog.LogDebug("The messages we're loaded");

        if(this.config.getEntryAsBoolean(DatabaseConfigEntry.ENABLED)) {
            this.sysLog.LogInformation("Die Datenbankverbindung wird aufgebaut!");

            if(this.dbManager.testConnection()) {
                this.sysLog.LogInformation("Die Datenbankverbindung wurde erfolgreich aufgebaut");
            } else {
                this.sysLog.LogError("Beim Öffnen der Datenbankverbindung ist ein Fehler aufgetreten!");
            }
        } else {
            this.sysLog.LogInformation("Die Datenbankverbindung ist deaktiviert!");
        }

        this.signalAgent.initConnection();

        this.sysLog.LogDebug("Registering the Event listener");
        this.registerListener();
        this.sysLog.LogDebug("Registered the Event listener");

        this.sysLog.LogDebug("Registering the Commands");
        this.registerCommands();
        this.sysLog.LogDebug("Registered the Commands");

        this.sysLog.LogDebug("Die Signale zwischen Bungee und Spigot werden registriert");
        this.registerSignals();
        this.sysLog.LogDebug("Die Signale wurden registriert!");

        this.sysLog.LogInformation("Successfully loaded the Core");
    }

    private void registerListener() {
        this.server.getEventManager().register(this, new ConnectionEvents());
    }

    private void registerCommands() {
        new ReloadCommand().registerCommand(this.server);
    }

    private void registerSignals() {
        this.signalAgent.registerListener(SignalType.CHECK_VERSION, new VersionListener());
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
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return this.dbManager;
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
