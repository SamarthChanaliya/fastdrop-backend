package com.feb_prject.open_share_application.enums;

import lombok.Getter;

@Getter
public enum Discoverability {
    HIDDEN("hidden"),
    NEARBY("nearby");

    private final String dbValue;

    Discoverability(String dbValue) {
        this.dbValue = dbValue;
    }

}
