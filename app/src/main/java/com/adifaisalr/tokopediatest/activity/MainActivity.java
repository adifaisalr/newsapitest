package com.adifaisalr.tokopediatest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.adifaisalr.tokopediatest.R;
import com.adifaisalr.tokopediatest.model.GetSources;
import com.adifaisalr.tokopediatest.model.Source;
import com.adifaisalr.tokopediatest.network.NetworkUtils;
import com.adifaisalr.tokopediatest.network.NewsClient;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    NewsClient newsClient;
    ArrayList<Source> sources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsClient = NetworkUtils.getNewsClient();

        // Fetch a list of the News Sources.
        Call<GetSources> call = newsClient.getSources("en");
        call.enqueue(new Callback<GetSources>() {
            @Override
            public void onResponse(Call<GetSources> call, Response<GetSources> response) {
                if(response.isSuccessful()) {
                    GetSources getSources = response.body();
                    String status = getSources.getStatus();
                    if(status.equalsIgnoreCase("ok")){
                        sources = (ArrayList<Source>) getSources.getSources();

                        // save source list to local database
                        FastStoreModelTransaction
                                .saveBuilder(FlowManager.getModelAdapter(Source.class))
                                .addAll(sources)
                                .build();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetSources> call, Throwable t) {

            }
        });
    }
}
