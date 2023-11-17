package com.example.poste.utils;

import android.util.Log;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class NullHostNameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String hostname, SSLSession session) {
        Log.e("restUtilImpl", "Approving cerificate for " + hostname);
        return true;
    }
}
