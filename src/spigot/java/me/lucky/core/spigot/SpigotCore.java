package me.lucky.core.spigot;

import me.lucky.core.api.ICore;
import me.lucky.core.api.database.DatabaseManager;
import me.lucky.core.api.utils.Config;
import me.lucky.core.api.utils.CoreFactory;
import me.lucky.core.api.utils.Messages;
import me.lucky.core.api.utils.SysLog;
import me.lucky.core.spigot.utils.SpigotMessages;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class SpigotCore extends JavaPlugin implements ICore {

    private final Config config;
    private final SpigotMessages messages;
    private final SysLog sysLog;
    private final DatabaseManager databaseManager;

    public SpigotCore() {
        CoreFactory.registerInstance(this);

        this.config = new Config();
        this.messages = new SpigotMessages();
        this.sysLog = new SysLog();
        this.databaseManager = new DatabaseManager();
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

        this.sysLog.LogInformation("Successfully loaded the Core");
    }

    @Override
    public void onDisable() {
        this.sysLog.LogInformation("Starting to unload the Core");

        this.sysLog.LogInformation("Successfully unloaded the Core");
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
}
