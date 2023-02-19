package io.bbahaida.bshort.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    ADMIN ("ADMIN"),
    USER("USER");

    final String value;

    Role(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public String toString() {
        return value;
    }
}
