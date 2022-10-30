package me.lucky.core.api.database.entities;

import lombok.Getter;
import lombok.Setter;
import me.lucky.core.api.database.annotations.DataColumn;
import me.lucky.core.api.database.annotations.DataTable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@DataTable(Name = "player")
public class DBPlayer {

    @DataColumn(Key = "ID", IsAuto = true)
    @Getter
    @Setter
    private int id;

    @DataColumn(Key = "UUID")
    @Getter
    @Setter
    private String uuid;

    @DataColumn(Key = "Username")
    @Getter
    @Setter
    private String name;

    @DataColumn(Key = "FirstJoinTime")
    @Getter
    @Setter
    private long firstJoined;

    @DataColumn(Key =  "LastJoinTime")
    @Getter
    @Setter
    private long lastJoined;

    public LocalDateTime getFirstJoinedDate() {
        return LocalDateTime.ofEpochSecond(this.getFirstJoined(), 0, ZoneOffset.UTC);
    }

    public void setFirstJoinedDate(LocalDateTime date) {
        this.setFirstJoined(date.toEpochSecond(ZoneOffset.UTC));
    }

    public LocalDateTime getLastJoinedDate() {
        return LocalDateTime.ofEpochSecond(this.getLastJoined(), 0, ZoneOffset.UTC);
    }

    public void setLastJoinedDate(LocalDateTime date) {
        this.setLastJoined(date.toEpochSecond(ZoneOffset.UTC));
    }
}

