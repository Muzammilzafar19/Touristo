package com.ecomway.touristo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.ecomway.touristo.MapsActivity;
import com.ecomway.touristo.R;

import java.util.ArrayList;
import java.util.List;

public class SpotSearchAdapter extends RecyclerView.Adapter<SpotSearchAdapter.MyViewHolder> {
    Context c;
    List<String> dataList;
    Activity activity;

    public SpotSearchAdapter(Context c, Activity activity) {
        this.c = c;
        this.activity = activity;
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
        String name = dataList.get(position);
        holder.txtSpotSearch.setText(name);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtSpotSearch;

        public MyViewHolder(View view) {
            super(view);
            txtSpotSearch = view.findViewById(R.id.txtSpotSearch);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!txtSpotSearch.getText().equals("PANAH GAH'S")) {
                        c.startActivity(new Intent(c, MapsActivity.class).putExtra("name", txtSpotSearch.getText().toString()).putExtra("lat", "").putExtra("lng", ""));
                        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    } else {
                        customAlert();
                    }
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
        dataList.add("MOSQUES");
        dataList.add("PANAH GAH'S");
        dataList.add("PETROL PUMPS");
        dataList.add("RESTAURANTS");

    }

    private void customAlert() {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(c);
        builderSingle.setIcon(R.drawable.logosplash);
        builderSingle.setTitle("Select City Name:-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(c, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Lahore");
        arrayAdapter.add("Multan");
        arrayAdapter.add("Rawalpindi");
        arrayAdapter.add("Islamabad");

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
                if (strName.equals("Rawalpindi") || strName.equals("Islamabad")|| strName.equals("Multan")) {
                    if (strName.equals("Rawalpindi")) {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?daddr=33.612574,73.055597"));
                        c.startActivity(intent);
                        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    } else if(strName.equals("Islamabad")){
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?daddr=33.636168,73.032156"));
                        c.startActivity(intent);
                        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }
                    else {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?daddr=30.177283,71.509318"));
                        c.startActivity(intent);
                        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }
                } else {
                    if (strName.equals("Lahore")) {
                        againCustomDialogForLahore();

                    }
                    else {

                    }
                }
            }
        });
        builderSingle.show();
    }

    private void againCustomDialogForLahore() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(c);
        builderSingle.setIcon(R.drawable.logosplash);
        builderSingle.setTitle("Select Lahore Panah gah point:-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(c, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Panah Gah General Bus stand Lahore");
        arrayAdapter.add("Panah Gah Data Ganj Baksh Darbar Lahore");
        arrayAdapter.add("Panah Gah Thokar Niaz Baig Multan Rd Lahore");
        arrayAdapter.add("Panah Gah Railway Station , Lahore");
        arrayAdapter.add("Panah Gah Fruit & Vegetable Market");
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
                if (strName.equals("Panah Gah General Bus stand Lahore")) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=31.591677,74.317504"));
                    c.startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else if (strName.equals("Panah Gah Data Ganj Baksh Darbar Lahore")) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=31.578624,74.307035"));
                    c.startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else if (strName.equals("Panah Gah Thokar Niaz Baig Multan Rd Lahore")) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=31.472603,74.241785"));
                    c.startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                } else if (strName.equals("Panah Gah Railway Station , Lahore")) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=31.574946,74.335969"));
                    c.startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else if (strName.equals("Panah Gah Fruit & Vegetable Market")) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=31.577773,74.366736"));
                    c.startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }
        });
        builderSingle.show();
    }

}
