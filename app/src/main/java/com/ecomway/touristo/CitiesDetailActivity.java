package com.ecomway.touristo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ecomway.touristo.adapter.CityTourPointsAdapter;
import com.ecomway.touristo.model.CitiesModel;
import com.ecomway.touristo.room.database.RoomDatabaseClient;
import com.ecomway.touristo.room.entities.CitiesTable;
import com.ecomway.touristo.room.entities.TouringPointsTable;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CitiesDetailActivity extends AppCompatActivity {
    TextView txtCityName, txtCityHeading;
    ImageView cityImage;
    Button btnMap, btnNearby;
    List<TouringPointsTable> tourList;
    ImageView imgweather;
    CityTourPointsAdapter cityTourPointsAdapter;
    double lng, lat;
    String cityServerid;
    CitiesModel citiesModel;
    List<LatLng> latLngs;
    com.ecomway.touristo.customfonts.MyTextView txtcityDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        init();
        setValue();
        onClicked();
        relatedTourPoints();

/*        lat=  Objects.requireNonNull(latLngs).get(0).latitude;
        lng=latLngs.get(0).longitude;*/

        //    cityLatLng(txtCityName.getText().toString());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    private void customDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CitiesDetailActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.custom_dialog_view, null);


        RecyclerView list = convertView.findViewById(R.id.recycler_view);
        Button cancel = convertView.findViewById(R.id.cancel);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(true);

        CityTourPointsAdapter adapter = new CityTourPointsAdapter(CitiesDetailActivity.this, tourList,CitiesDetailActivity.this);
        list.setAdapter(adapter);
        alertDialog.setView(convertView);

        final AlertDialog dialog = alertDialog.create();
        dialog.getWindow().setLayout(600, 400);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              dialog.dismiss();
            }
        });

        dialog.show();
        // adapter.notifyDataSetChanged();
    }

    private void onClicked() {
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + lat + "," + lng));
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);


            }
        });
        btnNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog();
            }
        });
        imgweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CitiesDetailActivity.this, WeatherActivity.class).putExtra("city", txtCityName.getText().toString().trim()).putExtra("tourname", "no"));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  getLatLongByCityName(txtCityName.getText().toString().trim());
    }

    private void getLatLongByCityName(String name) {
        List<LatLng> ll = null;

        if (Geocoder.isPresent()) {
            try {
                String location = name;
                Geocoder gc = new Geocoder(this);
                List<Address> addresses = gc.getFromLocationName(location, 5); // get the found Address Objects

                ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                for (Address a : addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        lat = a.getLatitude();
                        lng = a.getLongitude();
                        ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                    }
                }
                Log.i("chllng", new Gson().toJson(ll));


            } catch (IOException e) {
                Toast.makeText(this, "Location Not Found", Toast.LENGTH_SHORT).show();
                // handle the exception


            }

        }


    }

    private void relatedTourPoints() {
        for (TouringPointsTable tpt : RoomDatabaseClient.getInstance(CitiesDetailActivity.this).getAppDatabase().touringPointsTableDao().getSpecificTourByCityServerId(cityServerid)) {
            tourList.add(tpt);
        }
    }

    private void setValue() {
        try {
            CitiesTable ct = RoomDatabaseClient.getInstance(CitiesDetailActivity.this).getAppDatabase().citiesTableDao().getSpecificCity(getIntent().getExtras().getString("citiesName"));
            txtCityName.setText(ct.getCityName());
            txtcityDescription.setText(ct.getCityDescription());
            cityServerid = ct.getCityIdFromServer();
            lat = Float.parseFloat(ct.getCityLat());
            lng = Float.parseFloat(ct.getCityLng());
            RequestOptions myOptions = new RequestOptions()
                    .placeholder(R.drawable.defaultimg)
                    .error(R.drawable.defaultimg);
            Glide.with(this).load(ct.getCityImagePath()).apply(myOptions).into(cityImage);
        } catch (Exception e) {
            Toast.makeText(this, "Detail not found against this City ", Toast.LENGTH_SHORT).show();
            this.finish();

            e.printStackTrace();
        }
    }

    private void init() {
        txtCityName = findViewById(R.id.txtcityname);
        txtCityHeading = findViewById(R.id.heading);
        btnMap = findViewById(R.id.btncitymap);
        btnNearby = findViewById(R.id.btnTourPoint);
        cityImage = findViewById(R.id.cityImage);
        txtcityDescription = findViewById(R.id.txtcityDescription);
        imgweather = findViewById(R.id.imgweather);
        citiesModel = new CitiesModel();

        tourList = new ArrayList<>();


    }

}
