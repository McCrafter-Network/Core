package me.lucky.core.api.utils;

import me.lucky.core.api.ICore;

public class CoreFactory {

    private static ICore instance;

    public static void registerInstance(ICore core) {
        CoreFactory.instance = core;
    }

    public static ICore getRunningCore() {
        return CoreFactory.instance;
    }
}
