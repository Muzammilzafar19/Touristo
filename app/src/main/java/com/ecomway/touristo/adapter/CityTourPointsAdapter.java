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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ecomway.touristo.R;
import com.ecomway.touristo.TouringPointsActivity;
import com.ecomway.touristo.TouringPointsDetailActivity;
import com.ecomway.touristo.room.entities.TouringPointsTable;

import java.util.List;

public class CityTourPointsAdapter extends RecyclerView.Adapter<CityTourPointsAdapter.ViewHolder> {
    Context context;
    List<TouringPointsTable> list;
    Activity activity;

    public CityTourPointsAdapter(Context context, List<TouringPointsTable> list,Activity activity) {
        this.context = context;
        this.list = list;
        this.activity=activity;

    }

    @NonNull
    @Override
    public CityTourPointsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.tour_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CityTourPointsAdapter.ViewHolder holder, int position) {
       TouringPointsTable tpt=list.get(position);
       holder.txttourpoint.setText(tpt.getTouringPointName());
        RequestOptions myOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.cityimgg)
                .error(R.drawable.cityimgg);
        Glide.with(context).load(tpt.getTouringPointImagePath()).apply(myOptions).into(holder.tourpointimgview);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        de.hdodenhof.circleimageview.CircleImageView tourpointimgview;
        TextView txttourpoint;
        public ViewHolder(View itemView) {
            super(itemView);
            tourpointimgview=itemView.findViewById(R.id.tourpointimgview);
            txttourpoint=itemView.findViewById(R.id.txttourname);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, TouringPointsDetailActivity.class).putExtra("touringName", txttourpoint.getText().toString()).putExtra("type",""));
                    activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            });
        }
    }
}
