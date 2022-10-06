package me.lucky.core.api.enumerations.config;

import me.lucky.core.api.utils.IEnumNested;

public enum CoreConfigEntry implements IEnumNested {
    DATABASE("DATABASE"),
    MESSAGES("MSG");

    private final String key;

    CoreConfigEntry(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getCombinedKey() {
        return this.key;
    }

    @Override
    public boolean isParent() {
        return true;
    }

    @Override
    public IEnumNested getParent() {
        return null;
    }
}
