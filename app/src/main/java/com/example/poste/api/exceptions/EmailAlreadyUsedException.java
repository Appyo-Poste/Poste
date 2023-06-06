package com.example.poste.api.exceptions;

public class EmailAlreadyUsedException extends APIException {

    public EmailAlreadyUsedException() { super("Email in use"); }

}
