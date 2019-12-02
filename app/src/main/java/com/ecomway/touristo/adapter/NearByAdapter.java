package com.ecomway.touristo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecomway.touristo.MapsActivity;
import com.ecomway.touristo.R;
import com.ecomway.touristo.TouringPointsDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class NearByAdapter extends RecyclerView.Adapter<NearByAdapter.ViewHolder> {
    Context c;
    List<String> dataList;
    Activity activity;
    double lat,lng;
    public NearByAdapter(Context c, Activity activity,double lat,double lng) {
        this.c = c;
        this.dataList = dataList;
        this.activity=activity;
        this.lat=lat;
        this.lng=lng;
        spotSearchList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nearby_view, parent, false);

        return new NearByAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name=dataList.get(position);
        holder.txtSpotSearch.setText(name);
        holder.txtlng.setText(lat+"");
        holder.txtlat.setText(lng+"");
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtSpotSearch,txtlat,txtlng;
        public ViewHolder(View itemView) {
            super(itemView);
            txtSpotSearch = itemView.findViewById(R.id.txtSpotSearch);
            txtlat=itemView.findViewById(R.id.txtlat);
            txtlng=itemView.findViewById(R.id.txtlng);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    c.startActivity(new Intent(c, MapsActivity.class).putExtra("name",txtSpotSearch.getText().toString()).putExtra("lat",txtlat.getText().toString()).putExtra("lng",txtlng.getText().toString()));
                    activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            });
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
