package me.lucky.core.api.utils;

import me.lucky.core.api.ICore;

public class CoreFactory {

    private static ICore instance;
    private static Object server;

    public static void registerInstance(ICore core) {
        CoreFactory.instance = core;
    }

    public static ICore getRunningCore() {
        return CoreFactory.instance;
    }

    public static void registerServer(Object server) {
        CoreFactory.server = server;
    }

    public static Object getRunningServer() {
        return CoreFactory.server;
    }
}
