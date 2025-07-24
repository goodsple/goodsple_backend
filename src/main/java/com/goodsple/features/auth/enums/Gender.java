package com.goodsple.features.auth.enums;

public enum Gender {
    MALE,
    FEMALE;

    public String toUpperCase() {
        return this.name().toUpperCase();
    }
}
