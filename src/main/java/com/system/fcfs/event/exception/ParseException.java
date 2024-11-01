package com.system.fcfs.event.exception;

import com.system.fcfs.global.domain.exception.CustomRuntimeException;

public class ParseException extends CustomRuntimeException {
    public ParseException(String message, Object... args) {
        super(message, args);
    }
}
