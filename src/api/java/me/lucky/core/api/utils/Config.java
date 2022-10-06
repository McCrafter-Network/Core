package me.lucky.core.api.utils;

import de.mlessmann.confort.LoaderFactory;
import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.api.except.ParseException;
import de.mlessmann.confort.api.lang.IConfigLoader;
import de.mlessmann.confort.config.FileConfig;
import de.mlessmann.confort.lang.RegisterLoaders;
import me.lucky.core.api.ICore;
import me.lucky.core.api.exceptions.ConfigNotLoadedException;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

public class Config {
    private boolean dataLoaded = false;
    private FileConfig config = null;

    private final ICore core;

    public Config() {
        this.core = CoreFactory.getRunningCore();
    }

    public void LoadData() {
        this.core.getSysLog().LogDebug("Die Daten werden nun aus der Konfiguration geladen!");
        RegisterLoaders.registerLoaders();
        IConfigLoader jsonLoader = LoaderFactory.getLoader((String)"application/json");
        config = new FileConfig(jsonLoader, new File("plugins/config/config.json").getAbsoluteFile().toPath().normalize().toFile());
        try {
            config.load();
        }
        catch (ParseException | IOException e) {
            this.core.getSysLog().LogException("Beim Laden der Config ist ein Fehler aufgeterten!", (Exception)e);
            e.printStackTrace();
            return;
        }
        this.core.getSysLog().LogDebug("Das Laden der Daten wurde abgeschlossen!");
        this.dataLoaded = true;
    }

    public boolean RefreshData(boolean isEvent) {
        this.core.getSysLog().LogDebug("Die Konfiguration wird aktualisiert!");
        try {
            config.load();
        }
        catch (Exception e) {
            this.core.getSysLog().LogException("Beim aktualisieren der Konfiguration ist ein schwerwiegender Fehler aufgerteten!", e);
            return false;
        }
        if (this.checkForLoadedConfig()) {
            return false;
        }
        this.core.getMessages().refreshMessages();
        this.core.getSysLog().LogDebug("Das Aktualisieren der Konfiguration wurde abgeschlossen!");
        return true;
    }

    public String getEntryAsString(IEnumNested node) {
        if (this.checkForLoadedConfig()) {
            return "";
        }

        Object object = config.getRoot().getNode(this.getNodeByEnum(node)).getValue();
        if (object instanceof String) {
            return (String)object;
        }
        if (object instanceof Integer || object instanceof Double || object instanceof Float) {
            return object.toString();
        }
        return "";
    }

    public int getEntryAsInteger(IEnumNested node) {
        if (this.checkForLoadedConfig()) {
            return 0;
        }

        Object object = config.getRoot().getNode(this.getNodeByEnum(node)).getValue();
        if (object instanceof Integer) {
            return (Integer)object;
        }
        if (object instanceof String) {
            return Integer.parseInt((String)object);
        }
        if (object instanceof Double || object instanceof Float) {
            return (int)Math.round((Double)object);
        }
        return 0;
    }

    public boolean getEntryAsBoolean(IEnumNested node) {
        if (this.checkForLoadedConfig()) {
            return false;
        }

        Object object = config.getRoot().getNode(this.getNodeByEnum(node)).getValue();
        if (object instanceof Boolean) {
            return config.getRoot().getNode(this.getNodeByEnum(node)).asBoolean();
        }
        return false;
    }

    public Map<String, IConfigNode> getEntryAsMap(IEnumNested node) {
        return config.getRoot().getNode(this.getNodeByEnum(node)).asMap();
    }

    public <T> void addEntryToConfig(IEnumNested node, T entry) {
        if (this.checkForLoadedConfig()) {
            return;
        }

        IConfigNode configNode = config.getRoot().getNode(this.getNodeByEnum(node));

        if(entry instanceof Boolean) {
            configNode.setBoolean((Boolean) entry);
        } else if (entry instanceof String) {
            configNode.setString((String) entry);
        } else if (entry instanceof Integer) {
            configNode.setInteger((Integer) entry);
        } else if (entry instanceof Double) {
            configNode.setDouble((Double) entry);
        } else if (entry instanceof Float) {
            configNode.setFloat((Float) entry);
        } else {
            this.core.getSysLog().LogError("Beim setzten von " + entry.getClass().getName() + " ist ein Fehler aufgetreten!");
        }
    }

    public void saveConfig() {
        if (this.checkForLoadedConfig()) {
            return;
        }

        try {
            config.save();
        }
        catch (IOException e) {
            this.core.getSysLog().LogException("Die Config konnte nicht gespeichert werden!", e);
        }
    }

    private boolean checkForLoadedConfig() {
        if (!dataLoaded) {
            //this.core.getSysLog().LogException("Die Config wurde nicht geladen!", new ConfigNotLoadedException());
            return true;
        }

        return false;
    }

    private String[] getNodeByEnum(IEnumNested enumNested) {
        String key = enumNested.getCombinedKey();
        return key.split(Pattern.quote("."));
    }
}
