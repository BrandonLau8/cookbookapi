package com.cookbook.api.exceptions;

//RuntimeException makes it an unchecked exception
public class FoodNotFoundException extends RuntimeException {

    //ensures class can be serialized and deserialized correctly
    //final means variable cannot be changed after it has first been set
    private static final long serialVersionUID = 1;

    public FoodNotFoundException(String message) {
        super(message); //passed to RuntimeException
    }
}
