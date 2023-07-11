package com.example.poste.api.poste.exceptions;

public class MalformedResponseException extends APIException {

    public MalformedResponseException() { super("The API's response was malformed and could not be parsed"); }

}
