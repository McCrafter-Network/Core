package me.lucky.core.bungee.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;

public class ReloadCommand extends AbstractCommand {

    @Override
    protected BrigadierCommand getBrigadierCommand() {
        LiteralArgumentBuilder<CommandSource> baseNode = LiteralArgumentBuilder.
                <CommandSource>literal("reload")
                .requires(source -> source.hasPermission("core.reload"))
                .executes(source -> {
                    this.core.getSysLog().LogDebug("Der Reload Command wurde getriggert!");

                    this.reloadConfig();
                    this.reloadMessages();

                    return Command.SINGLE_SUCCESS;
                });

        return new BrigadierCommand(baseNode);
    }


    private void reloadConfig() {
        this.core.getSysLog().LogDebug("Die Config wird aktualisiert");

        this.core.getCustomConfig().LoadData();

        this.core.getSysLog().LogDebug("Die Config wurde aktualisiert!");
    }

    private void reloadMessages() {
        this.core.getSysLog().LogDebug("Die Nachrichten werden aktualisiert");

        this.core.getMessages().refreshMessages();

        this.core.getSysLog().LogDebug("Die Nachrichten wurden aktualisiert!");
    }
}
