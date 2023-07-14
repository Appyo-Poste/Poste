package com.example.legacy.poste.api.exceptions;

public class EmailAlreadyUsedException extends APIException {

    public EmailAlreadyUsedException() { super("Email in use"); }

}
