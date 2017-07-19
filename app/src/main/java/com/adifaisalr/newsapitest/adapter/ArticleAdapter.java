package com.adifaisalr.newsapitest.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adifaisalr.newsapitest.R;
import com.adifaisalr.newsapitest.model.Article;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Adi Faisal Rahman on 7/17/2017.
 */

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int BIG = 0, SMALL = 1;
    private ArrayList<Article> articles;
    private LayoutInflater inflater;
    private Context context;
    private ArticleClickListener clickListener;

    public ArticleAdapter(Context context, ArrayList<Article> articles, ArticleClickListener clickListener) {
        this.articles = articles;
        this.context = context;
        this.clickListener = clickListener;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View smallView = LayoutInflater.from(context).inflate(R.layout.item_article_small, parent, false);
        return new ViewHolder(smallView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Article article = articles.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.titleTV.setText(article.getTitle());
        viewHolder.descriptionTV.setText(article.getDescription());

        if (!article.getUrlToImage().isEmpty() && article.getUrlToImage() != null) {
            viewHolder.loadingIndicator.show();
            // load image asynchronously using picasso
            Picasso.with(context)
                    .load(article.getUrlToImage())
                    .fit()
                    .centerCrop()
                    .into(viewHolder.articleIV, new Callback() {
                        @Override
                        public void onSuccess() {
                            viewHolder.loadingIndicator.hide();
                        }

                        @Override
                        public void onError() {
                            viewHolder.loadingIndicator.hide();
                        }
                    });

            // set listener on cardview click
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(article);
                }
            });
        }else{
            Picasso.with(context)
                    .load(R.drawable.no_image)
                    .fit()
                    .centerCrop()
                    .into(viewHolder.articleIV);
            viewHolder.loadingIndicator.hide();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public interface ArticleClickListener {
        void onClick(Article article);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.titleTV)
        TextView titleTV;

        @BindView(R.id.descriptionTV)
        TextView descriptionTV;

        @BindView(R.id.articleIV)
        ImageView articleIV;

        @BindView(R.id.card_view)
        CardView cardView;

        @BindView(R.id.loadingIndicator)
        AVLoadingIndicatorView loadingIndicator;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
