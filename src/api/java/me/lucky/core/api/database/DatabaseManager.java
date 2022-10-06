package me.lucky.core.api.database;

import me.lucky.core.api.ICore;
import me.lucky.core.api.enumerations.config.DatabaseConfigEntry;
import me.lucky.core.api.utils.Config;
import me.lucky.core.api.utils.CoreFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.StatementRunnable;

public class DatabaseManager {

    private final ICore core;

    public DatabaseManager() {
        this.core = CoreFactory.getRunningCore();
    }

    public boolean testConnection() {
        try (Connection connection = this.getConnection().open()) {
            return true;
        } catch (Exception e) {
            this.core.getSysLog().LogException("Beim Öffnen der Datenbankverbindung ist ein Fehler aufgetreten!", e);
            return false;
        }
    }

    public void runStatement(StatementRunnable runnable) {
        this.getConnection().withConnection(runnable);
    }

    public Connection getOpenConn() {
        return this.getConnection().open();
    }

    private Sql2o getConnection() {
        Config customConfig = this.core.getCustomConfig();

        String dbHost = customConfig.getEntryAsString(DatabaseConfigEntry.HOST);
        String dbPort = customConfig.getEntryAsString(DatabaseConfigEntry.PORT);
        String dbDatabase = customConfig.getEntryAsString(DatabaseConfigEntry.DATABASE);
        String dbUser = customConfig.getEntryAsString(DatabaseConfigEntry.USER);
        String dbPassword = customConfig.getEntryAsString(DatabaseConfigEntry.PASSWORD);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            this.core.getSysLog().LogException("Beim laden des Datenbankdrivers ist ein Fehler aufgetreten!", e);
        }

        return new Sql2o("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbDatabase, dbUser, dbPassword);
    }

}
