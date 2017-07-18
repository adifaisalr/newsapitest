package com.adifaisalr.newsapitest.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.adifaisalr.newsapitest.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by adifaisalr on 7/18/17.
 */

public class ArticleDetailActivity extends AppCompatActivity {
    @BindView(R.id.articleWebView)
    WebView articleWebView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    String url = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
        }

        articleWebView.setWebViewClient(new WebViewClient());
        articleWebView.setWebChromeClient(new WebChromeClient(){


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);

                if(newProgress == 100){
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        articleWebView.loadUrl(url);
    }
}
