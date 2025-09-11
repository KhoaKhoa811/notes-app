package com.example.notes_app.exception.impl;

import com.example.notes_app.enums.ErrorCode;
import com.example.notes_app.enums.ErrorCode;
import com.example.notes_app.exception.BaseException;

public class ResourceAlreadyExistsException extends BaseException {

    public ResourceAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
