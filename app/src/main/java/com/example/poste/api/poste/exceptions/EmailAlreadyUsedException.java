package com.example.poste.api.poste.exceptions;

public class EmailAlreadyUsedException extends APIException {

    public EmailAlreadyUsedException() { super("Email in use"); }

}
