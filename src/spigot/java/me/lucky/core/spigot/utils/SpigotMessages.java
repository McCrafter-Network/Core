package me.lucky.core.spigot.utils;

import me.lucky.core.api.utils.Messages;
import me.lucky.core.api.enumerations.MessageTypes;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class SpigotMessages extends Messages {
    @Override
    public void sendNoPermissionMessage(Object player) {
        ((Player) player).sendMessage(TextComponent.fromLegacyText(this.getIngameMessage(MessageTypes.NO_PERMISSIONS)));
    }
}
