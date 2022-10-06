package me.lucky.core.bungee.commands;

import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import me.lucky.core.api.ICore;
import me.lucky.core.api.utils.CoreFactory;

abstract class AbstractCommand {

    protected final ICore core;

    protected AbstractCommand() {
        this.core = CoreFactory.getRunningCore();
    }

    public void registerCommand(ProxyServer proxy) {
        proxy.getCommandManager().register(this.getBrigadierCommand());
    }

    protected abstract BrigadierCommand getBrigadierCommand();
}
