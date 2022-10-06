package me.lucky.core.api.database;

import java.util.function.BiConsumer;

public class DBParameter<T> {
    public String Name;
    public T Value;

    public DBParameter(String name, T value) {
        this.Name = name;
        this.Value = value;
    }
}