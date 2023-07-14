package com.example.legacy.poste.api.models;

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

    public static FolderAccess valueOf(int value) {
        switch (value) {
            case 1: return VIEW;
            case 2: return MANAGE;
            case 3: return OWNER;
            default: return NONE;
        }
    }
}
