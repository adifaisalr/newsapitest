package com.adifaisalr.newsapitest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.adifaisalr.newsapitest.R;
import com.adifaisalr.newsapitest.adapter.SourceAdapter;
import com.adifaisalr.newsapitest.model.Article;
import com.adifaisalr.newsapitest.model.GetSources;
import com.adifaisalr.newsapitest.model.Source;
import com.adifaisalr.newsapitest.network.NetworkUtils;
import com.adifaisalr.newsapitest.network.NewsClient;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView sourceRecyclerView;

    @BindView(R.id.loadingLayout)
    LinearLayout loadingLayout;

    NewsClient newsClient;
    ArrayList<Source> sources;
    SourceAdapter sourceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        sources = new ArrayList<>();

        // callback for item click listener
        SourceAdapter.SourceClickListener clickListener = new SourceAdapter.SourceClickListener() {
            @Override
            public void onClick(Source source) {
                Log.d("Source", source.getId());
                Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
                // send extra source id for get article
                intent.putExtra("sourceID",source.getId());
                startActivity(intent);
            }
        };
        sourceAdapter = new SourceAdapter(MainActivity.this, sources, clickListener);
        sourceRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        sourceRecyclerView.setAdapter(new ScaleInAnimationAdapter(sourceAdapter));

        // Fetch a list of the News Sources.
        showLoadingLayout();
        newsClient = NetworkUtils.getNewsClient();
        Call<GetSources> call = newsClient.getSources("en");
        call.enqueue(new Callback<GetSources>() {
            @Override
            public void onResponse(Call<GetSources> call, Response<GetSources> response) {
                if (response.isSuccessful()) {
                    GetSources getSources = response.body();
                    String status = getSources.getStatus();
                    // check status response api
                    if (status.equalsIgnoreCase("ok")) {
                        int countn = sources.size();
                        sources.clear();
                        sourceAdapter.notifyItemMoved(0, countn);
                        sources.addAll(getSources.getSources());

                        // notify adapter to update recycler view
                        sourceAdapter.notifyItemRangeInserted(0, sources.size());

                        // save source list to local database
                        FastStoreModelTransaction
                                .saveBuilder(FlowManager.getModelAdapter(Source.class))
                                .addAll(sources)
                                .build();
                    }
                }
                hideLoadingLayout();
            }

            @Override
            public void onFailure(Call<GetSources> call, Throwable t) {

            }
        });
    }

    void showLoadingLayout(){
        sourceRecyclerView.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    void hideLoadingLayout(){
        loadingLayout.setVisibility(View.GONE);
        sourceRecyclerView.setVisibility(View.VISIBLE);
    }
}
