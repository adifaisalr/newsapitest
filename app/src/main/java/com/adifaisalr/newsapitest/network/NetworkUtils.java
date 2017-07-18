package com.adifaisalr.newsapitest.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Adi Faisal Rahman on 7/17/2017.
 */

public class NetworkUtils {
    private static final String API_BASE_URL = "https://newsapi.org/";
    public static final String API_KEY = "bcc181c600874cd5b7aca3990d509349";

    public static NewsClient getNewsClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        // add logging as last interceptor
        httpClient.addInterceptor(logging);

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        return retrofit.create(NewsClient.class);
    }
}
