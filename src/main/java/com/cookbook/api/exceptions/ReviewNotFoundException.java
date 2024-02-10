package com.cookbook.api.exceptions;

public class ReviewNotFoundException extends RuntimeException {
    //ensures class can be serialized and deserialized correctly
    //final means variable cannot be changed after it has first been set
    private static final long serialVersionUID = 3;

    public ReviewNotFoundException(String message) {
        super(message); //passed to RuntimeException
    }
}
