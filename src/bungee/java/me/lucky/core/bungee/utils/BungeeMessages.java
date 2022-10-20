package me.lucky.core.bungee.utils;

import com.velocitypowered.api.proxy.Player;
import me.lucky.core.api.utils.Messages;
import me.lucky.core.api.enumerations.MessageTypes;
import net.kyori.adventure.text.Component;

public class BungeeMessages extends Messages {
    @Override
    public void sendNoPermissionMessage(Object player) {
        ((Player) player).sendMessage(Component.text(this.getIngameMessage(MessageTypes.NO_PERMISSIONS)));
    }
}
