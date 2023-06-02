package com.myproject.carrental.controller;

import com.myproject.carrental.exception.CarNotFoundException;
import com.myproject.carrental.exception.RentalNotFoundException;
import com.myproject.carrental.exception.RentalOverlappingException;
import com.myproject.carrental.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalHttpErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception) {
        return new ResponseEntity<>("User with given ID does not exist!", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<Object> handleCarNotFoundException(CarNotFoundException exception) {
        return new ResponseEntity<>("Car with given ID does not exist!", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RentalOverlappingException.class)
    public ResponseEntity<Object> handleRentalOverlappingException(RentalOverlappingException exception) {
        return new ResponseEntity<>("Rental is overlapping the other one!", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RentalNotFoundException.class)
    public ResponseEntity<Object> handleRentalNotFoundException(RentalNotFoundException exception) {
        return new ResponseEntity<>("Rental with given ID not found!", HttpStatus.BAD_REQUEST);
    }
}
