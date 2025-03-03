package com.shiftm.shiftm.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
    /* COMMON ERROR */
    INTERNAL_SERVER_ERROR(500, "COMMON001", "Internal Server Error"),
    INVALID_INPUT_VALUE(400, "COMMON002", "Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "COMMON003", "Entity Not Found"),

    /* MEMBER ERROR */
    DUPLICATED_ID(400, "MEM001", "It Is Duplicated ID"),
    DUPLICATED_EMAIL(400, "MEM002", "It Is Duplicated Email"),
    MEMBER_NOT_FOUND(400, "MEM003", "Member Not Found"),

    /* AUTH ERROR */
    INVALID_PASSWORD(400, "AUTH001", "It Is Invalid Password"),
    INVALID_TOKEN(400, "AUTH002", "It Is Invalid Token"),
    INVALID_BEARER_PREFIX(400, "AUTH003", "It Is Invalid Bearer Prefix"),
    UNAUTHORIZED(401, "AUTH004", "Unauthorized, Please Login"),
    FORBIDDEN(403, "AUTH005", "Forbidden, You Don't Have Any Authority"),

    /* LEAVE TYPE ERROR */
    DUPLICATED_NAME(400, "LVT001", "It Is Duplicated Name"),
    NOT_FOUND_LEAVE_TYPE(404, "LVT002", "Leave type not found"),

    /* COMPANY ERROR */
    COMPANY_ALREADY_EXISTS(404, "COMPANY001", "Company Is Already Exists"),
    COMPANY_NOT_FOUND(404, "COMPANY002", "Company Not Found"),
  
    /* EMAIL ERROR */
    UNABLE_TO_SEND_EMAIL(500, "EMAIL001", "Unable To Send Email");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
