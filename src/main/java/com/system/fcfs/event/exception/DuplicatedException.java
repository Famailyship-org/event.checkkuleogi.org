package com.system.fcfs.event.exception;

import com.system.fcfs.global.domain.exception.CustomRuntimeException;

public class DuplicatedException extends CustomRuntimeException {
    public DuplicatedException(String message, Object... args) {
        super(message, args);
    }
}
