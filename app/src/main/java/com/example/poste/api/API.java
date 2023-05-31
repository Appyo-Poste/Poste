package com.example.poste.api;

import java.net.MalformedURLException;
import java.net.URL;

public class API {

    public static final String HOST_URL = "https://poste-388415.uc.r.appspot.com/";

    public static URL URL(String endpoint) {
        try {
            return new URL(String.join(HOST_URL, endpoint));
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

}
