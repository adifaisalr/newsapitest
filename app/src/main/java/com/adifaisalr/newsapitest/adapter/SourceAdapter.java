package com.adifaisalr.newsapitest.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adifaisalr.newsapitest.R;
import com.adifaisalr.newsapitest.model.Source;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Adi Faisal Rahman on 7/17/2017.
 */

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.ViewHolder> {
    private ArrayList<Source> sources;
    private LayoutInflater inflater;
    private Context context;
    private SourceClickListener clickListener;

    public SourceAdapter(Context context, ArrayList<Source> sources, SourceClickListener clickListener) {
        this.sources = sources;
        this.context = context;
        this.clickListener = clickListener;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public SourceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_source, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Source source = sources.get(position);

        holder.titleTV.setText(source.getName());
        holder.descriptionTV.setText(source.getDescription());

        // set listener on cardview click
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(source);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return sources.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.titleTV)
        TextView titleTV;

        @BindView(R.id.descriptionTV)
        TextView descriptionTV;

        @BindView(R.id.card_view)
        CardView cardView;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public interface SourceClickListener{
        void onClick(Source source);
    }
}
