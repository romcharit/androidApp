package com.example.mixmaster.restAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CountryApiService {
    @GET("v3.1/all")
    Call<List<Country>> getAllCountries();
}
