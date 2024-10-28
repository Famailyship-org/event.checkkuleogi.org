package com.system.fcfs.global.domain;

import com.system.fcfs.global.domain.exception.BadRequestException;
import com.system.fcfs.global.domain.exception.IllegalParameterException;
import com.system.fcfs.global.domain.exception.NotFoundException;
import com.system.fcfs.global.domain.response.CommonResponseEntity;
import com.system.fcfs.global.domain.response.CustomError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {
    private ResponseEntity<CommonResponseEntity<Object>> response(Throwable throwable, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(
                new CommonResponseEntity<>(false, null, new CustomError(throwable.getMessage(), status))
                , headers, status
        );
    }

    @ExceptionHandler({
            IllegalParameterException.class,
            BadRequestException.class})
    public ResponseEntity<?> handleBadRequestException(Exception e){
        return response(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleExceptionForBadRequest(Exception e){
        return response(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception e) {
        return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
