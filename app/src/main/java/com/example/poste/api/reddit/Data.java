package com.example.poste.api.reddit;

public class Data {
    private String authorization_code;
    private String code;
    private String redirect_uri;


    public Data(String authorization_code, String code, String redirect_uri)
    {
        this.authorization_code = authorization_code;
        this.code = code;
        this.redirect_uri = redirect_uri;
    }

    public String getAuthorization_code() {
        return authorization_code;
    }

    public void setAuthorization_code(String authorization_code) {
        this.authorization_code = authorization_code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }
}
