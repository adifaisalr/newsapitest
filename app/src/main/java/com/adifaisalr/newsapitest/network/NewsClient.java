package com.adifaisalr.newsapitest.network;

import com.adifaisalr.newsapitest.model.GetArticles;
import com.adifaisalr.newsapitest.model.GetSources;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Adi Faisal Rahman on 7/17/2017.
 */

public interface NewsClient {
    @GET("v1/sources")
    Call<GetSources> getSources(@Query("language") String language);

    @GET("v1/articles")
    Call<GetArticles> getArticles(@Query("source") String source, @Query("apiKey") String apiKey);
}
