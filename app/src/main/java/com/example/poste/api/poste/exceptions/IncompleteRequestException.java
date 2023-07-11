package com.example.poste.api.poste.exceptions;

public class IncompleteRequestException extends APIException {

    public IncompleteRequestException() { super("Request could not be executed due to cancellation, a connectivity problem or timeout"); }

}
