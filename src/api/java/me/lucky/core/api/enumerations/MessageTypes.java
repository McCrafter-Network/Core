package me.lucky.core.api.enumerations;

import me.lucky.core.api.utils.IEnumKeyType;

public enum MessageTypes implements IEnumKeyType {
    NO_PERMISSIONS("core.no_permissions")

    ;

    private final String key;

    private MessageTypes(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
