package com.adifaisalr.newsapitest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adifaisalr.newsapitest.R;
import com.adifaisalr.newsapitest.adapter.ArticleAdapter;
import com.adifaisalr.newsapitest.database.AppDatabase;
import com.adifaisalr.newsapitest.model.Article;
import com.adifaisalr.newsapitest.model.GetArticles;
import com.adifaisalr.newsapitest.network.NetworkUtils;
import com.adifaisalr.newsapitest.network.NewsClient;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleListActivity extends AppCompatActivity {
    @BindView(R.id.root)
    LinearLayout root;

    @BindView(R.id.recyclerView)
    RecyclerView articleRecyclerView;

    @BindView(R.id.searchLayout)
    RelativeLayout searchLayout;

    @BindView(R.id.backBtn)
    ImageView backBtn;

    @BindView(R.id.titleTV)
    TextView titleTV;

    @BindView(R.id.searchET)
    TextView searchET;

    @BindView(R.id.loadingLayout)
    LinearLayout loadingLayout;

    @BindView(R.id.noArticleLayout)
    LinearLayout noArticleLayout;

    NewsClient newsClient;
    ArrayList<Article> articles = new ArrayList<>();
    ArticleAdapter articleAdapter;
    String sourceID;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        ButterKnife.bind(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // set actionbar title
        titleTV.setText(R.string.article_list);

        // get article source from extras
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sourceID = bundle.getString("sourceID");
        }

        // callback for item click listener
        ArticleAdapter.ArticleClickListener clickListener = new ArticleAdapter.ArticleClickListener() {
            @Override
            public void onClick(Article article) {
                Log.d("ArticleURL", article.getUrl());
                Intent intent = new Intent(ArticleListActivity.this, ArticleDetailActivity.class);
                // send extra source article url
                intent.putExtra("url", article.getUrl());
                startActivity(intent);
            }
        };
        articleAdapter = new ArticleAdapter(ArticleListActivity.this, articles, clickListener);
        articleRecyclerView.setLayoutManager(new LinearLayoutManager(ArticleListActivity.this));
        articleRecyclerView.setAdapter(new ScaleInAnimationAdapter(articleAdapter));

        // set edittext action search listener
        searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // hide soft keyboard
                    imm.hideSoftInputFromWindow(searchLayout.getWindowToken(), 0);
                    searchArticle(searchET.getText().toString());
                    return true;
                }
                return false;
            }
        });

        if (NetworkUtils.isOnline(this)) {
            getArticleFromAPI();
        } else {
            loadFromDB();
            showNoInternetSnackBar();
        }
    }

    void getArticleFromAPI(){
        // Fetch a list of the News Sources.
        showLoadingLayout();
        newsClient = NetworkUtils.getNewsClient();
        Call<GetArticles> call = newsClient.getArticles(sourceID, NetworkUtils.API_KEY);
        call.enqueue(new Callback<GetArticles>() {
            @Override
            public void onResponse(Call<GetArticles> call, Response<GetArticles> response) {
                if (response.isSuccessful()) {
                    GetArticles getArticles = response.body();
                    String status = getArticles.getStatus();
                    // check status response api
                    if (status.equalsIgnoreCase("ok")) {

                        List<Article> articleArrayList = getArticles.getArticles();
                        // set article source id
                        for (Article article : articleArrayList) {
                            article.setSourceId(sourceID);
                        }

                        Article.deleteBySource(sourceID);
                        // save source list to local database
                        FlowManager.getDatabase(AppDatabase.class).executeTransaction(
                                FastStoreModelTransaction
                                        .saveBuilder(FlowManager.getModelAdapter(Article.class))
                                        .addAll(articleArrayList)
                                        .build());

                        loadFromDB();
                    }
                }
                hideLoadingLayout();
            }

            @Override
            public void onFailure(Call<GetArticles> call, Throwable t) {
                hideLoadingLayout();
                showConnectionErrorSnackBar();
            }
        });
    }

    void loadFromDB() {
        int count = articles.size();
        articles.clear();
        articleAdapter.notifyItemRangeRemoved(0, count);
        articles.addAll(Article.getAllBySource(sourceID));
        // notify adapter to update recycler view
        articleAdapter.notifyItemRangeInserted(0, articles.size());
        checkEmptyResult();
    }

    @OnClick(R.id.searchIV)
    void searchBtnClick() {
        // animate layout, show search bar and hide back button & title
        backBtn.setVisibility(View.GONE);
        titleTV.setVisibility(View.GONE);
        searchLayout.setVisibility(View.VISIBLE);
        searchET.requestFocus();
        imm.showSoftInput(searchET, InputMethodManager.SHOW_IMPLICIT);
    }

    @OnClick(R.id.backBtn)
    void backBtnClick() {
        finish();
    }

    @OnClick(R.id.backBtn2)
    void backBtnInsideClick() {
        hideSearchLayout();
    }

    // animate layout, hide soft keyboard, search bar and show back button & title
    void hideSearchLayout() {
        backBtn.setVisibility(View.VISIBLE);
        titleTV.setVisibility(View.VISIBLE);
        imm.hideSoftInputFromWindow(searchLayout.getWindowToken(), 0);
        searchLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.closeIV)
    void clearClick() {
        // clear input edittext
        searchET.setText("");
        searchArticle("");
        hideSearchLayout();
    }

    void searchArticle(String keyword) {
        // clear the article list
        int count = articles.size();
        articles.clear();
        articleAdapter.notifyItemRangeRemoved(0, count);

        if (searchET.getText().length() > 0) {
            // search article title by keyword
            articles.addAll(Article.getByTitleKeyword(sourceID, keyword));
        } else {
            // no query, get all article by source
            articles.addAll(Article.getAllBySource(sourceID));
        }
        // notify adapter to update recycler view
        articleAdapter.notifyItemRangeInserted(0, articles.size());
        checkEmptyResult();
    }

    void checkEmptyResult(){
        hideLoadingLayout();
        if (articles.isEmpty()) {
            showNoArticleLayout();
        }
    }

    // show progress bar
    void showLoadingLayout() {
        articleRecyclerView.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        noArticleLayout.setVisibility(View.GONE);
    }

    // hide progress bar
    void hideLoadingLayout() {
        loadingLayout.setVisibility(View.GONE);
        articleRecyclerView.setVisibility(View.VISIBLE);
        noArticleLayout.setVisibility(View.GONE);
    }

    // show no article layour
    void showNoArticleLayout() {
        articleRecyclerView.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        noArticleLayout.setVisibility(View.VISIBLE);
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

    void showConnectionErrorSnackBar() {
        final Snackbar mySnackbar = Snackbar.make(root,
                R.string.connection_error, Snackbar.LENGTH_INDEFINITE);
        mySnackbar.setAction(R.string.retry, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getArticleFromAPI();
                mySnackbar.dismiss();
            }
        });
        mySnackbar.show();
    }
}
