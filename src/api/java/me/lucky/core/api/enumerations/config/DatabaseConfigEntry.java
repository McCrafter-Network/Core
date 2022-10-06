package me.lucky.core.api.enumerations.config;

import me.lucky.core.api.utils.IEnumNested;

public enum DatabaseConfigEntry implements IEnumNested {
    USER("User", CoreConfigEntry.DATABASE),
    PASSWORD("Password", CoreConfigEntry.DATABASE),
    HOST("Host", CoreConfigEntry.DATABASE),
    PORT("Port", CoreConfigEntry.DATABASE),
    DATABASE("Database", CoreConfigEntry.DATABASE),
    ENABLED("Enabled", CoreConfigEntry.DATABASE);

    private final String key;
    private final IEnumNested parent;

    DatabaseConfigEntry(String key, IEnumNested parent) {
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
