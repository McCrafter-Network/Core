package me.lucky.core.api.enumerations.config;

import me.lucky.core.api.utils.IEnumNested;

public enum RedisConfigEntry implements IEnumNested {
    HOST("Host", CoreConfigEntry.REDIS),
    PORT("Port", CoreConfigEntry.REDIS),
    USER("Username", CoreConfigEntry.REDIS),
    PASSWORD("Password", CoreConfigEntry.REDIS),
    ENABLED("Enabled", CoreConfigEntry.REDIS);

    private final String key;
    private final IEnumNested parent;

    RedisConfigEntry(String key, IEnumNested parent) {
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
