package com.adifaisalr.newsapitest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adifaisalr.newsapitest.R;
import com.adifaisalr.newsapitest.model.Source;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Adi Faisal Rahman on 7/18/2017.
 */

public class SourceAdapter extends BaseAdapter {
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
    public int getCount() {
        return sources.size();
    }

    @Override
    public Object getItem(int position) {
        return sources.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Source source = sources.get(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_source, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleTV.setText(source.getName());
        holder.descriptionTV.setText(source.getDescription());

        // set listener on cardview click
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(source);
            }
        });
        return convertView;
    }

    public interface SourceClickListener {
        void onClick(Source source);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.titleTV)
        TextView titleTV;

        @BindView(R.id.descriptionTV)
        TextView descriptionTV;

        @BindView(R.id.root)
        LinearLayout root;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
