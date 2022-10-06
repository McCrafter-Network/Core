package me.lucky.core.api.utils;

public interface IEnumNested {
    String getKey();

    String getCombinedKey();

    boolean isParent();

    IEnumNested getParent();
}
