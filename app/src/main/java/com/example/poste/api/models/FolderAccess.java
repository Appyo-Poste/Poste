package com.example.poste.api.models;

public enum FolderAccess {
    NONE(0),
    VIEW(1),
    MANAGE(2),
    OWNER(3);

    private final int value;

    FolderAccess(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}
