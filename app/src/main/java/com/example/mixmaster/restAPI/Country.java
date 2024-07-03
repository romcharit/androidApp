package com.example.mixmaster.restAPI;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Country {
    @SerializedName("name")
    private Common name;

    public Common getName() {
        return name;
    }


}
