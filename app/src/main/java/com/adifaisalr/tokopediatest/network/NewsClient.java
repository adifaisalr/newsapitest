package com.adifaisalr.tokopediatest.network;

import com.adifaisalr.tokopediatest.model.GetSources;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Adi Faisal Rahman on 7/17/2017.
 */

public interface NewsClient {
    @GET("v1/sources")
    Call<GetSources> getSources(@Query("language") String language);
}
