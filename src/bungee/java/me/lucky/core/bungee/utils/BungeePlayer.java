package me.lucky.core.bungee.utils;

import com.velocitypowered.api.proxy.Player;
import me.lucky.core.api.utils.IPlayer;

import java.util.UUID;

public class BungeePlayer implements IPlayer {

    private final Player player;

    public BungeePlayer(Player player) {
        this.player = player;
    }

    @Override
    public void saveToDB() {

    }

    @Override
    public boolean hasPermission(String permKey) {
        return this.player.hasPermission(permKey);
    }

    @Override
    public Object getPlayer() {
        return this.player;
    }

    @Override
    public UUID getUUID() {
        return this.player.getUniqueId();
    }

    @Override
    public long getID() {
        return 0;
    }

    @Override
    public boolean hasChanged() {
        return false;
    }
}
