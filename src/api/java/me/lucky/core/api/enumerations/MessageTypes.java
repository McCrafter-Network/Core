package me.lucky.core.api.enumerations;

import me.lucky.core.api.utils.IEnumKeyType;

public enum MessageTypes implements IEnumKeyType {
    NO_PERMISSIONS("core.no_permissions"),
    BACKGROUND_ERROR("core.background_error"),
    PLAYER_DOESNT_EXIST("core.player_doesnt_exist"),
    NO_CONSOLE("core.no_console");

    private final String key;

    private MessageTypes(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
