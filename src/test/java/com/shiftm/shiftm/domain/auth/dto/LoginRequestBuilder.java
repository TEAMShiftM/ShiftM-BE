package com.shiftm.shiftm.domain.auth.dto;

import com.shiftm.shiftm.domain.auth.dto.request.LoginRequest;

public class LoginRequestBuilder {
    public static LoginRequest build() {
        final String id = "shiftm";
        final String password = "Password!0";

        return new LoginRequest(id, password);
    }
}
