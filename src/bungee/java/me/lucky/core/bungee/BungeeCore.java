package me.lucky.core.bungee;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import me.lucky.core.api.ICore;
import me.lucky.core.api.database.DatabaseManager;
import me.lucky.core.api.enumerations.config.DatabaseConfigEntry;
import me.lucky.core.api.utils.Config;
import me.lucky.core.api.utils.CoreFactory;
import me.lucky.core.api.utils.Messages;
import me.lucky.core.api.utils.SysLog;
import me.lucky.core.bungee.commands.ReloadCommand;
import me.lucky.core.bungee.events.ConnectionEvents;
import me.lucky.core.bungee.utils.BungeeMessages;

import javax.inject.Inject;
import java.util.logging.Logger;

@Plugin(id = "bungeecore", name = "Mc Crafter TV - Core", version = "0.5-SNAPSHOT", authors = {"LuckyDev (Kimon Meier)"})
public class BungeeCore implements ICore {
    private final ProxyServer server;
    private final Logger logger;
    private final BungeeMessages messages;
    private final Config config;
    private final SysLog sysLog;
    private final DatabaseManager dbManager;


    @Inject
    public BungeeCore(ProxyServer server, Logger logger) {
        CoreFactory.registerInstance(this);

        this.server = server;
        this.logger = logger;

        this.messages = new BungeeMessages();
        this.config = new Config();
        this.sysLog = new SysLog();
        this.dbManager = new DatabaseManager();
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

        this.sysLog.LogDebug("Registering the Event listener");
        this.registerListener();
        this.sysLog.LogDebug("Registered the Event listener");

        this.sysLog.LogDebug("Registering the Commands");
        this.registerCommands();
        this.sysLog.LogDebug("Registered the Commands");

        this.sysLog.LogInformation("Successfully loaded the Core");
    }

    private void registerListener() {
        this.server.getEventManager().register(this, new ConnectionEvents());
    }

    private void registerCommands() {
        new ReloadCommand().registerCommand(this.server);
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
}