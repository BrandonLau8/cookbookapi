package com.cookbook.api.exceptions;

import com.cookbook.api.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice //handles exceptions thrown by controller methods
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ErrorDto> handleLoginException(LoginException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage("Login error");
        errorDto.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<ErrorDto> handleRegisterException(LoginException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage("Register error");
        errorDto.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
//
//    @ExceptionHandler(FoodNotFoundException.class) //when a foodnotfoundexception occurs in controllers, this method will be invoked
//    //webrequests typically used in spring mvc controllers to handle incoming http requests
//    public ResponseEntity<ErrorObject> handleFoodNotFoundException(
//            FoodNotFoundException ex, WebRequest request) {
//        ErrorObject errorObject = new ErrorObject();
//        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value()); //404
//        errorObject.setMessage(ex.getMessage());
//        errorObject.setTimestamp(new Date());
//
//        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(IngredientNotFoundException.class)
//    public ResponseEntity<ErrorObject> handleIngredientNotFoundException(
//            IngredientNotFoundException ex, WebRequest request) {
//        ErrorObject errorObject = new ErrorObject();
//        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
//        errorObject.setMessage(ex.getMessage());
//        errorObject.setTimestamp(new Date());
//
//        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(ReviewNotFoundException.class)
//    public ResponseEntity<ErrorObject> handleReviewNotFoundException(
//            ReviewNotFoundException ex, WebRequest request) {
//        ErrorObject errorObject = new ErrorObject();
//        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
//        errorObject.setMessage(ex.getMessage());
//        errorObject.setTimestamp(new Date());
//
//        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
//    }
}
