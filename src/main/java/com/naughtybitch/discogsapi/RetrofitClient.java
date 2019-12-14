package com.naughtybitch.discogsapi;

import android.util.Log;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String BASE_URL = "https://api.discogs.com";

    public static Retrofit retrofit;

    /*
    This public static method will return Retrofit client
    anywhere in the application
     */

    public static synchronized Retrofit getRetrofitClient(OkHttpClient client) {
        // If condition to ensure we don't create multiple retrofit instances in a single application
        if (retrofit == null) {
            synchronized (RetrofitClient.class) {
                if (retrofit == null) {
                    Log.i("instance", "Creating new instance");

                    // Defining the Retrofit using Builder
                    retrofit = new Retrofit.Builder().baseUrl(BASE_URL) // This is the only mandatory call on Builder object.
                            .addConverterFactory(GsonConverterFactory.create()) // Converter library used to convert response into POJO
                            .client(client).build();
                }
            }
        }
        return retrofit;
    }

    public static synchronized Retrofit getRetrofitClient() {
        // If condition to ensure we don't create multiple retrofit instances in a single application
        if (retrofit == null) {
            synchronized (RetrofitClient.class) {
                if (retrofit == null) {
                    Log.i("instance", "Creating new instance");

                    // Defining the Retrofit using Builder
                    retrofit = new Retrofit.Builder().baseUrl(BASE_URL) // This is the only mandatory call on Builder object.
                            .addConverterFactory(GsonConverterFactory.create()) // Converter library used to convert response into POJO
                            .build();
                }
            }
        }
        return retrofit;
    }
}