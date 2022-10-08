package me.lucky.core.api.utils;

import me.lucky.core.api.ICore;
import me.lucky.core.api.enumerations.config.MessageConfigEntry;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;

public class SysLog {
    private final ICore core;
    private final Config config;

    public SysLog() {
        this.core = CoreFactory.getRunningCore();
        this.config = this.core.getCustomConfig();
    }

    public void LogDebug(String message) {
        if (!this.config.getEntryAsBoolean(MessageConfigEntry.LOG_DEBUG)) {
            return;
        }
        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        this.core.getLogger().log(Level.INFO, this.config.getEntryAsString(MessageConfigEntry.LOGGER_PREFIX) + className + " || " + message);
    }

    public void LogInformation(String message) {
        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        this.core.getLogger().log(Level.INFO, this.config.getEntryAsString(MessageConfigEntry.LOGGER_PREFIX) + className + " || " + message);
    }

    public void LogWarning(String message) {
        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        this.core.getLogger().log(Level.WARNING, this.config.getEntryAsString(MessageConfigEntry.LOGGER_PREFIX) + className + " || " + message);
    }

    public void LogError(String message) {
        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        this.core.getLogger().log(Level.SEVERE, this.config.getEntryAsString(MessageConfigEntry.LOGGER_PREFIX) + className + " || " + message);
    }

    public void LogException(String message, Exception ex) {
        StringWriter strWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(strWriter);
        ex.printStackTrace(writer);
        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        String exceptionInfo = strWriter.toString();
        this.core.getLogger().log(Level.SEVERE, this.config.getEntryAsString(MessageConfigEntry.LOGGER_PREFIX) + className + " || " + message + "\n" + exceptionInfo);
    }
}
