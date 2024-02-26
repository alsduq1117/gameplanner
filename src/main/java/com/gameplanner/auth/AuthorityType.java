package com.gameplanner.auth;

public enum AuthorityType {
    ROLE_ADMIN("관리자"),
    ROLE_USER("유저");

    private final String value;

    private AuthorityType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}