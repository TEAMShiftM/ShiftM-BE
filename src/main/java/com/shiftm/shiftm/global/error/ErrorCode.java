package com.shiftm.shiftm.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
    /* COMMON ERROR */
    INTERNAL_SERVER_ERROR(500, "COMMON001", "Internal Server Error"),
    INVALID_INPUT_VALUE(400, "COMMON002", "Invalid Input Value"),

    /* MEMBER ERROR */
    DUPLICATED_ID(400, "MEM001", "It Is Duplicated ID"),
    DUPLICATED_EMAIL(400, "MEM002", "It Is Duplicated Email");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
