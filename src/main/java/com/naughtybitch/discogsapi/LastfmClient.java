package com.naughtybitch.discogsapi;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LastfmClient {

    public static final String BASE_URL = "https://ws.audioscrobbler.com";

    public static Retrofit retrofit;

    public static Retrofit getRetrofitClient() {
        // If condition to ensure we don't create multiple retrofit instances in a single application
        if (retrofit == null) {
            Log.i("instance", "Creating new instance");

            // Defining the Retrofit using Builder
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL) // This is the only mandatory call on Builder object.
                    .addConverterFactory(GsonConverterFactory.create()) // Converter library used to convert response into POJO
                    .build();
        }
        return retrofit;
    }
}
