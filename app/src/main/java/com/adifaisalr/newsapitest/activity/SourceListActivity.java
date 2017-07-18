package com.adifaisalr.newsapitest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.adifaisalr.newsapitest.R;
import com.adifaisalr.newsapitest.adapter.SourceAdapter;
import com.adifaisalr.newsapitest.database.AppDatabase;
import com.adifaisalr.newsapitest.model.GetSources;
import com.adifaisalr.newsapitest.model.Source;
import com.adifaisalr.newsapitest.network.NetworkUtils;
import com.adifaisalr.newsapitest.network.NewsClient;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SourceListActivity extends AppCompatActivity {
    @BindView(R.id.root)
    RelativeLayout root;

    @BindView(R.id.recyclerView)
    ListView sourceRecyclerView;

    @BindView(R.id.loadingLayout)
    LinearLayout loadingLayout;

    NewsClient newsClient;
    ArrayList<Source> sources = new ArrayList<>();
    SourceAdapter sourceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // callback for item click listener
        SourceAdapter.SourceClickListener clickListener = new SourceAdapter.SourceClickListener() {
            @Override
            public void onClick(Source source) {
                Log.d("Source", source.getId());
                Intent intent = new Intent(SourceListActivity.this, ArticleListActivity.class);
                // send extra source id for get article
                intent.putExtra("sourceID", source.getId());
                startActivity(intent);
            }
        };
        sourceAdapter = new SourceAdapter(SourceListActivity.this, sources, clickListener);
        sourceRecyclerView.setAdapter(sourceAdapter);

        if (NetworkUtils.isOnline(this)) {
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
                            // save source list to local database
                            FlowManager.getDatabase(AppDatabase.class).executeTransaction(
                                    FastStoreModelTransaction
                                            .saveBuilder(FlowManager.getModelAdapter(Source.class))
                                            .addAll(getSources.getSources())
                                            .build());

                            loadFromDB();
                        }
                    }
                    hideLoadingLayout();
                }

                @Override
                public void onFailure(Call<GetSources> call, Throwable t) {

                }
            });
        } else {
            hideLoadingLayout();
            loadFromDB();
            showNoInternetSnackBar();
        }
    }

    void loadFromDB() {
        sources.clear();
        sources.addAll(Source.getAll());
        // notify adapter to update recycler view
        sourceAdapter.notifyDataSetChanged();
    }

    void showLoadingLayout() {
        sourceRecyclerView.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    void hideLoadingLayout() {
        loadingLayout.setVisibility(View.GONE);
        sourceRecyclerView.setVisibility(View.VISIBLE);
    }

    void showNoInternetSnackBar() {
        final Snackbar mySnackbar = Snackbar.make(root,
                R.string.no_internet, Snackbar.LENGTH_INDEFINITE);
        mySnackbar.setAction(R.string.dismiss, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySnackbar.dismiss();
            }
        });
        mySnackbar.show();
    }
}
