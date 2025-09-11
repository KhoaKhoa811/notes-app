package com.example.notes_app.exception.impl;

import com.example.notes_app.enums.ErrorCode;
import com.example.notes_app.exception.BaseException;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
