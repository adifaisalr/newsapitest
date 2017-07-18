package com.adifaisalr.newsapitest.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adifaisalr.newsapitest.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by adifaisalr on 7/18/17.
 */

public class ArticleDetailActivity extends AppCompatActivity {
    @BindView(R.id.articleWebView)
    WebView articleWebView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.searchIV)
    ImageView searchIV;

    @BindView(R.id.titleTV)
    TextView titleTV;

    String url = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);

        // get url from extras
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
        }

        // set action bar title
        titleTV.setText(R.string.article_detail);

        // hide search button since it doesn't needed here
        searchIV.setVisibility(View.GONE);

        articleWebView.setWebViewClient(new WebViewClient());
        articleWebView.setWebChromeClient(new WebChromeClient(){


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // add progress bar listener
                progressBar.setProgress(newProgress);
                if(newProgress == 100){
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        // open url
        articleWebView.loadUrl(url);
    }

    @OnClick(R.id.backBtn)
    void backClick(){
        finish();
    }
}
