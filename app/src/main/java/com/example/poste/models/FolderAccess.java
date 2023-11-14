package com.example.poste.models;

public enum FolderAccess {
    NONE(0, "none"),
    VIEWER(1, "viewer"),
    EDITOR(2, "editor"),
    FULL_ACCESS(3, "full_access");

    private final int value;
    private final String stringValue;

    FolderAccess(final int newValue, final String stringValue) {
        value = newValue;
        this.stringValue = stringValue;
    }

    public int getValue() { return value; }

    public String getStringValue() { return stringValue; }

    public static FolderAccess valueOf(int value) {
        switch (value) {
            case 1: return VIEWER;
            case 2: return EDITOR;
            case 3: return FULL_ACCESS;
            default: return NONE;
        }
    }
}
