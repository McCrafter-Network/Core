package me.lucky.core.api.utils;


import de.mlessmann.confort.LoaderFactory;
import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.api.except.ParseException;
import de.mlessmann.confort.api.lang.IConfigLoader;
import de.mlessmann.confort.config.FileConfig;
import de.mlessmann.confort.lang.RegisterLoaders;
import me.lucky.core.api.ICore;
import me.lucky.core.api.enumerations.MessageTypes;
import me.lucky.core.api.enumerations.config.MessageConfigEntry;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class Messages {
    private Map<String, String> messages = new HashMap<String, String>();

    protected final ICore core;

    protected Messages() {
        this.core = CoreFactory.getRunningCore();
    }

    public String getMessage(IEnumKeyType path, String ... arguments) {
        String message = this.messages.getOrDefault(path.getKey(), "");
        if (!message.equals("") && arguments.length > 0) {
            for (int i = 0; i <= arguments.length - 1; ++i) {
                message = message.replace("{" + i + "}", arguments[i]);
            }
        }
        return message;
    }

    public String getIngameMessage(IEnumKeyType path, String ... arguments) {
        return this.core.getCustomConfig().getEntryAsString(MessageConfigEntry.INGAME_PREFIX) + this.getMessage(path, arguments);
    }

    public String getLoggerMessage(IEnumKeyType path, String ... arguments){
        return this.core.getCustomConfig().getEntryAsString(MessageConfigEntry.LOGGER_PREFIX) + this.getMessage(path, arguments);
    }

    public void refreshMessages() {
        this.messages = new HashMap<String, String>();
        this.loadMessages();

        this.core.getSysLog().LogDebug("Die Nachrichten wurden neugeladen!");
    }

    private void loadMessages() {
        SysLog sysLog = this.core.getSysLog();

        sysLog.LogDebug("Die Daten werden nun aus der Konfiguration geladen!");
        RegisterLoaders.registerLoaders();
        IConfigLoader jsonLoader = LoaderFactory.getLoader((String)"application/json");
        FileConfig config = new FileConfig(jsonLoader, new File("plugins/config/messages.json").getAbsoluteFile().toPath().normalize().toFile());
        try {
            config.load();
        }
        catch (ParseException | IOException e) {
            sysLog.LogException("Beim Laden der Config ist ein Fehler aufgetreten", (Exception)e);
        }
        for (IConfigNode node : config.getRoot().asList()) {
            sysLog.LogDebug("Die Nachricht: " + node.getNode("Key").asString() + " wird geladen!");
            this.messages.put(node.getNode("Key").asString(), node.getNode("Message").asString());
            sysLog.LogDebug("Die Nachricht: " + node.getNode("Key").asString() + " wurde erfolgreich geladen!");
        }
        sysLog.LogDebug("Die Daten wurden neugeladen!");
    }

    public abstract void sendNoPermissionMessage(Object player);
}