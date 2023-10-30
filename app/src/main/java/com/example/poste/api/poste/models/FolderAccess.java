package com.example.poste.api.poste.models;

public enum FolderAccess {
    NONE(0),
    VIEWER(1),
    EDITOR(2),
    FULL_ACCESS(3);

    private final int value;

    FolderAccess(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }

    public static FolderAccess valueOf(int value) {
        switch (value) {
            case 1: return VIEWER;
            case 2: return EDITOR;
            case 3: return FULL_ACCESS;
            default: return NONE;
        }
    }
}
