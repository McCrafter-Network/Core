package me.lucky.core.bungee.events;

import com.velocitypowered.api.event.Subscribe;
import me.lucky.core.api.ICore;
import me.lucky.core.api.database.Operations;
import me.lucky.core.api.database.WhereParameter;
import me.lucky.core.api.database.entities.DBPlayer;
import me.lucky.core.api.utils.CoreFactory;
import org.sql2o.Connection;

import java.time.LocalDateTime;

public class ConnectionEvents {

    private final ICore core;

    public ConnectionEvents() {
        this.core = CoreFactory.getRunningCore();
    }

    @Subscribe
    public void onPlayerJoin(com.velocitypowered.api.event.connection.LoginEvent loginEvent) {
        try (Connection connection = this.core.getDatabaseManager().getOpenConn()) {
            com.velocitypowered.api.proxy.Player eventPlayer = loginEvent.getPlayer();
            DBPlayer player = Operations.ExecuteScalarQuery(connection, DBPlayer.class, new WhereParameter<>(DBPlayer.class, DBPlayer::getUuid, eventPlayer.getUniqueId().toString()));;

            boolean playerWasEmpty = false;
            if(player == null) {
                playerWasEmpty = true;
                this.core.getSysLog().LogDebug("Players empty");
                player = new DBPlayer();
                player.setUuid(eventPlayer.getUniqueId().toString());
                player.setFirstJoinedDate(LocalDateTime.now());
            }

            player.setName(eventPlayer.getUsername());
            player.setLastJoinedDate(LocalDateTime.now());

            if(playerWasEmpty) {
                Operations.Insert(connection, player);
            } else {
                Operations.Update(connection, player, player.getId());
            }
        } catch (Exception e) {
            this.core.getSysLog().LogException("Beim aktualisieren des Spielers ist ein Fehler aufgetreten!", e);
        }
    }
}
