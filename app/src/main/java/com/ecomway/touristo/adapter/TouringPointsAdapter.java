package com.ecomway.touristo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ecomway.touristo.R;
import com.ecomway.touristo.TouringPointsActivity;
import com.ecomway.touristo.TouringPointsDetailActivity;
import com.ecomway.touristo.model.CitiesModel;
import com.ecomway.touristo.model.TouringPointsModel;

import java.io.Serializable;
import java.util.List;

public class TouringPointsAdapter extends RecyclerView.Adapter<TouringPointsAdapter.ViewHolder> {
    private Context context;
    private List<TouringPointsModel> _list;
    private int globalPosition;

    public TouringPointsAdapter(Context context, List<TouringPointsModel> _list) {
        this.context = context;
        this._list = _list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.touring_points_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TouringPointsModel tp = _list.get(position);
        globalPosition = position;
        // Log.i("chkmodel",cm.getImage().toString());
        holder.txttourname1.setText(tp.getTouingPointName());
        holder.rating.setRating(Float.parseFloat(tp.getRating()!=null?tp.getRating():"0"));
        RequestOptions myOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.cityimgg)
                .error(R.drawable.cityimgg);
        try {
            Glide.with(context).load(Integer.parseInt(tp.getImagePath())).apply(myOptions).into(holder.tourpic);
        } catch (NumberFormatException e) {
            Glide.with(context).load(tp.getImagePath()).apply(myOptions).into(holder.tourpic);
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return _list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        com.ecomway.touristo.customfonts.MyTextView txttourname1;
        de.hdodenhof.circleimageview.CircleImageView tourpic;
        RatingBar rating;

        public ViewHolder(View itemView) {
            super(itemView);
            txttourname1 = itemView.findViewById(R.id.txttourname1);
            tourpic = itemView.findViewById(R.id.tourpic);
            rating=itemView.findViewById(R.id.rating);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, TouringPointsDetailActivity.class).putExtra("touringName", txttourname1.getText().toString()).putExtra("type", ""));
                }
            });
        }
    }
}
