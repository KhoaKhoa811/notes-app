package com.example.notes_app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // account
    ACCOUNT_EMAIL_NOT_FOUND(100, "email not found"),
    ACCOUNT_NOT_FOUND(101, "account not found"),
    ACCOUNT_GET_ALL(102, "account get all"),
    ACCOUNT_DELETED(103, "account deleted"),
    ACCOUNT_UPDATED(104, "account updated"),
    // role
    ROLE_ALREADY_EXIST(202, "role already exists"),
    ROLE_NOT_FOUND(201, "role not found"),
    ROLE_CREATED(200, "role created"),
    ROLE_UPDATED(203, "role updated"),
    ROLE_DELETED(204, "role deleted"),
    ROLE_GET_ALL(205, "role get all"),
    // permission
    PERMISSION_ALREADY_EXIST(202, "permission already exists" ),
    PERMISSION_NOT_FOUND(201, "permission not found"),
    PERMISSION_GET_ALL(203, "permission get all"),
    PERMISSION_DELETED(204, "permission deleted"),
    PERMISSION_UPDATED(205, "permission updated"),
    PERMISSION_CREATED(200, "permission created"),
    // jwt
    JWT_INVALID(301, "jwt invalid or expired" ),
    // register
    ACCOUNT_EMAIL_ALREADY_EXIST(400, "account email already exist" ),
    REGISTER_SUCCESS(401, "register success"),
    //login
    LOGIN_SUCCESS(501, "login success"),
    REFRESH_TOKEN(502, "refresh token success"),
    // note
    NOTE_CREATED(503, "note created"),
    NOTE_DELETED(504, "note deleted"),
    NOTE_UPDATED(505, "note updated"),
    NOTE_GET_BY_ID(510, "note get by id"),
    NOTE_GET_ALL(511, "note get all"),
    NOTE_GET_BY_ACCOUNT(512,  "note get by account"),

    ;

    private final int code;
    private final String message;


}
