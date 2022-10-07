package me.lucky.core.api;

import me.lucky.core.api.database.DatabaseManager;
import me.lucky.core.api.signaling.SignalAgent;
import me.lucky.core.api.utils.Config;
import me.lucky.core.api.utils.Messages;
import me.lucky.core.api.utils.SysLog;

import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public interface ICore {

    Messages getMessages();

    Config getCustomConfig();

    SysLog getSysLog();

    Logger getLogger();

    DatabaseManager getDatabaseManager();

    SignalAgent getSignalAgent();

    ExecutorService getExecutor();
}