package com.example.poste.api.reddit;

import com.google.gson.annotations.SerializedName;

public class Post{ //Here is what we want from the json file from reddit

    @SerializedName("main")
    Main main;

    public Main getMain() { return main; }

    public void setMain(Main main) {
        this.main = main;
    }
}