package com.ecomway.touristo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecomway.touristo.R;

import java.util.ArrayList;
import java.util.List;

public class SpotSearchAdapter extends RecyclerView.Adapter<SpotSearchAdapter.MyViewHolder> {
    Context c;
    List<String> dataList;

    public SpotSearchAdapter(Context c) {
        this.c = c;
        spotSearchList();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spotsearch_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
      String name=dataList.get(position);
      holder.txtSpotSearch.setText(name);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public com.ecomway.touristo.customfonts.MyTextView txtSpotSearch;

        public MyViewHolder(View view) {
            super(view);
            txtSpotSearch = view.findViewById(R.id.txtSpotSearch);

        }
    }

    private void spotSearchList() {
        dataList = new ArrayList<String>();
        dataList.add("AIRPORTS");
        dataList.add("ATM'S");
        dataList.add("EMBASSY OFFICES");
        dataList.add("HOSPITALS");
        dataList.add("HOTELS");
        dataList.add("MOSQUES");
        dataList.add("PANAH GAH'S");
        dataList.add("PETROL PUMPS");
        dataList.add("RESTAURANTS");

    }
}
