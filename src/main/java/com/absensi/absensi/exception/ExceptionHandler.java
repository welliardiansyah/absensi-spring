package com.absensi.absensi.exception;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.*;

public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleCloudVendorNotFoundException
            (NotFoundException notFoundException)
    {
        Exception exception = new Exception(
                notFoundException.getMessage(),
                notFoundException.getCause(),
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }
}
