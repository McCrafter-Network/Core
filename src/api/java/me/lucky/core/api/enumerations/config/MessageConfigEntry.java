package me.lucky.core.api.enumerations.config;

import me.lucky.core.api.utils.IEnumNested;

public enum MessageConfigEntry implements IEnumNested{
    INGAME_PREFIX("Ingame_Prefix", CoreConfigEntry.MESSAGES),
    LOGGER_PREFIX("Logger_Prefix", CoreConfigEntry.MESSAGES),
    LOG_DEBUG("Log_Debug", CoreConfigEntry.MESSAGES);

    private final String key;
    private final IEnumNested parent;

    MessageConfigEntry(String key, IEnumNested parent) {
        this.key = key;
        this.parent = parent;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getCombinedKey() {
        return this.parent.getCombinedKey() + "." + this.key;
    }

    @Override
    public boolean isParent() {
        return false;
    }

    @Override
    public IEnumNested getParent() {
        return this.parent;
    }
}
