package com.example.notes_app.exception.impl;

import com.example.notes_app.enums.ErrorCode;
import com.example.notes_app.exception.BaseException;

public class InvalidTokenSignatureException extends BaseException {
    public InvalidTokenSignatureException(ErrorCode errorCode) {
        super(errorCode);
    }
}
