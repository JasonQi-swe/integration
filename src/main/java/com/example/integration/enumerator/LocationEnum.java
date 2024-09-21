package com.example.integration.enumerator;

public enum LocationEnum {
    EU("91000000"),
    ITALY("103350119"),
    US("103644278"),
    CANADA("101174742"),
    STOCKHOLM("90010409"),
    LATIN_AMERICA("91000011");

    private final String id;

    LocationEnum(String id) {
        this.id = id;
    }

    public String getLocationId() {
        return this.id;
    }
}

