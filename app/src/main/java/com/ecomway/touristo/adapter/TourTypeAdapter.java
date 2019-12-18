package com.ecomway.touristo.adapter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecomway.touristo.R;
import com.ecomway.touristo.TouringPointsActivity;
import com.ecomway.touristo.TouringPointsDetailActivity;
import com.ecomway.touristo.customfonts.MyTextView;

import java.util.ArrayList;
import java.util.List;

public class TourTypeAdapter extends RecyclerView.Adapter<TourTypeAdapter.MyViewHolder> {
    private List<String> dataList;
    private Context c;
    private Activity activity;
    public TourTypeAdapter(Context c, Activity activity) {
        this.c = c;
        this.activity=activity;
        populate();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tour_type_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = dataList.get(position);
        holder.txtTourType.setText(name);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTourType;

        public MyViewHolder(View view) {
            super(view);
            txtTourType = view.findViewById(R.id.txtType);
             view.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     if(!txtTourType.getText().toString().equals("Religious")) {
                         c.startActivity(new Intent(c, TouringPointsActivity.class).putExtra("type", txtTourType.getText().toString().trim()));
                         activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                     }
                     else {
                         customAlert();
                     }
                 }
             });
        }
    }
    private void customAlert() {
        final androidx.appcompat.app.AlertDialog.Builder builderSingle = new AlertDialog.Builder(c);
        builderSingle.setIcon(R.drawable.logosplash);
        builderSingle.setTitle("Select Religious Type:-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(c, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Islamic");
        arrayAdapter.add("Christianity");
        arrayAdapter.add("Buddhism");
        arrayAdapter.add("Sikhism");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                c.startActivity(new Intent(c, TouringPointsActivity.class).putExtra("type", strName));
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        builderSingle.show();
    }

    private void populate() {
        dataList = new ArrayList<>();
        dataList.add("Adventure");
        dataList.add("Cultural");
        dataList.add("Historical");
        dataList.add("Religious");
    }



}
