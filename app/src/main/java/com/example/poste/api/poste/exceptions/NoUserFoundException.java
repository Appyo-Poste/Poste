package com.example.poste.api.poste.exceptions;

public class NoUserFoundException extends APIException {

    public NoUserFoundException() { super("No user was found with the provided details"); }

    public NoUserFoundException(String message) { super(message); }

}
