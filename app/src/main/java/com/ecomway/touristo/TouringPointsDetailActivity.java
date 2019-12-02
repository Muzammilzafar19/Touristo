package com.ecomway.touristo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ecomway.touristo.adapter.CityTourPointsAdapter;
import com.ecomway.touristo.adapter.NearByAdapter;
import com.ecomway.touristo.room.database.RoomDatabaseClient;
import com.ecomway.touristo.room.entities.CitiesTable;
import com.ecomway.touristo.room.entities.TouringPointsTable;

public class TouringPointsDetailActivity extends AppCompatActivity {
    TextView txtTourName, txtTourHeading;
    ImageView tourImage;
    Button btnMap, btnTourPoint;
    ImageButton imgweather;
    double lng,lat;
    private String cityNameGlobal;

    com.ecomway.touristo.customfonts.MyTextView txttourDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touring_points_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        init();
setValue();
        onClicked();
    }
    private void onClicked()
    {
        imgweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TouringPointsDetailActivity.this,WeatherActivity.class).putExtra("city",cityNameGlobal));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" +lat + "," + lng));
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        btnTourPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog();
            }
        });
    }
    private void setValue()
    {
        try {
            TouringPointsTable ct= RoomDatabaseClient.getInstance(TouringPointsDetailActivity.this).getAppDatabase().touringPointsTableDao().getSpecificTour(getIntent().getExtras().getString("touringName"));
            txtTourName.setText(ct.getTouringPointName());
            txttourDescription.setText(ct.getTouringPointDescription());
            lat=Float.parseFloat(ct.getTourLat().equals("")?"0":ct.getTourLat());
            lng=Float.parseFloat(ct.getTourLng().equals("")?"0":ct.getTourLng());

            String city_id=ct.getTourCity();
            CitiesTable citiesTable=RoomDatabaseClient.getInstance(TouringPointsDetailActivity.this).getAppDatabase().citiesTableDao().getSpecificCityByServerId(city_id);
            cityNameGlobal=citiesTable.getCityName();
            RequestOptions myOptions = new RequestOptions()
                    .placeholder(R.drawable.defaultimg)
                    .error(R.drawable.defaultimg);
            Glide.with(this).load(ct.getTouringPointImagePath()).apply(myOptions).into(tourImage);
        } catch (Exception e) {
            Toast.makeText(this, "Touring Point Detail Not Found ", Toast.LENGTH_SHORT).show();
            this.finish();

           Log.e("exp",e.getMessage());
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void init()
    {
        txtTourName=findViewById(R.id.txttourname);
        txtTourHeading=findViewById(R.id.heading);
        tourImage=findViewById(R.id.tourimgg);
        btnMap=findViewById(R.id.btntourmap);
        btnTourPoint=findViewById(R.id.btnTourPointNearBy);
        txttourDescription=findViewById(R.id.txttourDescription);
        imgweather=findViewById(R.id.imgweather);
    }
    private void customDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TouringPointsDetailActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.custom_dialog_nearby_view, null);


        RecyclerView list = convertView.findViewById(R.id.recycler_view);
        Button cancel = convertView.findViewById(R.id.cancel);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(true);

        NearByAdapter adapter = new NearByAdapter(TouringPointsDetailActivity.this,TouringPointsDetailActivity.this,lat,lng);
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
}
