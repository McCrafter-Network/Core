package me.lucky.core.api.utils;

import java.util.UUID;

public interface IPlayer {

    void saveToDB();

    boolean hasPermission(String permKey);

    Object getPlayer();

    UUID getUUID();

    long getID();

    boolean hasChanged();
}
