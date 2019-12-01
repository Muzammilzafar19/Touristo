package com.ecomway.touristo.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ecomway.touristo.CitiesActivity;
import com.ecomway.touristo.CitiesDetailActivity;
import com.ecomway.touristo.R;
import com.ecomway.touristo.TouringPointsActivity;
import com.ecomway.touristo.model.CitiesModel;

import java.io.Serializable;
import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> {
    private List<CitiesModel> _list;
    private Context context;

    public CitiesAdapter(List<CitiesModel> _list, Context context) {
        this._list = _list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.cities_single_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CitiesModel cm=_list.get(position);
     Log.i("chkmodel",cm.getImage().toString()+" "+cm.getImagePath());
        holder.txtcityname1.setText(cm.getName());
        RequestOptions myOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.cityimgg)
                .error(R.drawable.cityimgg);
        if(cm.getImage()!=0)
        {Glide.with(context).load(cm.getImage()).apply(myOptions).into(holder.citypic);}
        else Glide.with(context).load(cm.getImagePath()).apply(myOptions).into(holder.citypic);

    }

    @Override
    public int getItemCount() {
        return _list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        com.ecomway.touristo.customfonts.MyTextView txtcityname1;
        de.hdodenhof.circleimageview.CircleImageView citypic;

        public ViewHolder(View itemView) {
            super(itemView);
            txtcityname1=itemView.findViewById(R.id.txtcityname1);
            citypic=itemView.findViewById(R.id.citypic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 context.startActivity(new Intent(context, CitiesDetailActivity.class).putExtra("citiesName", txtcityname1.getText().toString()));
                }
            });

        }
    }
}
