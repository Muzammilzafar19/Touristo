package com.ecomway.touristo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ecomway.touristo.adapter.CityTourPointsAdapter;
import com.ecomway.touristo.adapter.NearByAdapter;
import com.ecomway.touristo.model.RatingModel;
import com.ecomway.touristo.room.database.RoomDatabaseClient;
import com.ecomway.touristo.room.entities.CitiesTable;
import com.ecomway.touristo.room.entities.TouringPointsTable;
import com.ecomway.touristo.utils.SharePref;
import com.ecomway.touristo.utils.Urls;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.taufiqrahman.reviewratings.BarLabels;
import com.taufiqrahman.reviewratings.RatingReviews;

import java.util.Random;

public class TouringPointsDetailActivity extends AppCompatActivity {
    TextView txtTourName, txtTourHeading;
    ImageView tourImage;
    RatingBar rating;
    SharePref pref;
    Button btnMap, btnTourPoint;
    ImageButton imgweather;
    String ratingValue;
    double lng, lat;
    private DatabaseReference mDatabase, refroot;
    private String cityNameGlobal;
    RatingReviews ratingReviews;
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
        checkRating();
    }

    private void onClicked() {
        imgweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TouringPointsDetailActivity.this, WeatherActivity.class).putExtra("city", cityNameGlobal).putExtra("tourname", txtTourName.getText().toString()));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + lat + "," + lng));
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
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Toast.makeText(TouringPointsDetailActivity.this, String.valueOf(v), Toast.LENGTH_SHORT).show();
                ratingValue = String.valueOf(v);
                saveRating();
            }
        });
    }

    private void setValue() {
        try {
            TouringPointsTable ct = RoomDatabaseClient.getInstance(TouringPointsDetailActivity.this).getAppDatabase().touringPointsTableDao().getSpecificTour(getIntent().getExtras().getString("touringName"));
            txtTourName.setText(ct.getTouringPointName());
            txttourDescription.setText(ct.getTouringPointDescription());
            lat = Float.parseFloat(ct.getTourLat().equals("") ? "0" : ct.getTourLat());
            lng = Float.parseFloat(ct.getTourLng().equals("") ? "0" : ct.getTourLng());

            String city_id = ct.getTourCity();
            CitiesTable citiesTable = RoomDatabaseClient.getInstance(TouringPointsDetailActivity.this).getAppDatabase().citiesTableDao().getSpecificCityByServerId(city_id);
            cityNameGlobal = citiesTable.getCityName();
            RequestOptions myOptions = new RequestOptions()
                    .placeholder(R.drawable.defaultimg)
                    .error(R.drawable.defaultimg);
            Glide.with(this).load(ct.getTouringPointImagePath()).apply(myOptions).into(tourImage);
            int colors[] = new int[]{
                    Color.parseColor("#0e9d58"),
                    Color.parseColor("#bfd047"),
                    Color.parseColor("#ffc105"),
                    Color.parseColor("#ef7e14"),
                    Color.parseColor("#d36259")};

            int raters[] = new int[]{
                    new Random().nextInt(100),
                    new Random().nextInt(100),
                    new Random().nextInt(100),
                    new Random().nextInt(100),
                    new Random().nextInt(100)
            };

            ratingReviews.createRatingBars(100, BarLabels.STYPE1, colors, raters);
        } catch (Exception e) {
            Toast.makeText(this, "Touring Point Detail Not Found ", Toast.LENGTH_SHORT).show();
            this.finish();

            Log.e("exp", e.getMessage());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void init() {
        txtTourName = findViewById(R.id.txttourname);
        txtTourHeading = findViewById(R.id.heading);
        tourImage = findViewById(R.id.tourimgg);
        btnMap = findViewById(R.id.btntourmap);
        rating = findViewById(R.id.rating);
        btnTourPoint = findViewById(R.id.btnTourPointNearBy);
        txttourDescription = findViewById(R.id.txttourDescription);
        imgweather = findViewById(R.id.imgweather);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        pref = new SharePref(TouringPointsDetailActivity.this);
        ratingReviews = findViewById(R.id.rating_reviews);
    }

    private void customDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TouringPointsDetailActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.custom_dialog_nearby_view, null);


        RecyclerView list = convertView.findViewById(R.id.recycler_view);
        Button cancel = convertView.findViewById(R.id.cancel);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(true);

        NearByAdapter adapter = new NearByAdapter(TouringPointsDetailActivity.this, TouringPointsDetailActivity.this, lat, lng);
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

    private void saveRating() {
        RatingModel rm = new RatingModel();
        rm.setRating(ratingValue);
        rm.setUserId(pref.getUserId());
        rm.setTourPointName(txtTourName.getText().toString());
        // read the index key
        refroot = FirebaseDatabase.getInstance().getReference();
        String mGroupId = refroot.push().getKey();
        mDatabase.child("Rating").child(txtTourName.getText().toString()).child(mGroupId).setValue(rm);
    }

    private void checkRating() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        refroot = mDatabase.child("Rating").child(txtTourName.getText().toString());
        final ProgressDialog progressDialog = new ProgressDialog(TouringPointsDetailActivity.this);
        progressDialog.setTitle("Check Rating");
        progressDialog.setIcon(R.drawable.logosplash);
        progressDialog.setMessage("Please Wait.........");
        progressDialog.show();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //create new user
                    progressDialog.dismiss();
                    double total = 0, average = 0;
                    int count = 0;
                    boolean flag = false;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        double rating = Double.parseDouble(ds.child("rating").getValue().toString() == null ? "0" : ds.child("rating").getValue().toString());
                        total = total + rating;
                        count = count + 1;
                        average = total / count;
                        if (ds.child("userId").getValue().toString().equals(pref.getUserId())) {
                            flag = true;

                        }
                    }
                    if(flag) {
                        rating.setRating((float) average);
                        rating.setEnabled(false);
                    }
                    Log.i("chkAverg", average + "");
                    // Toast.makeText(ProfileActivity.this,"Value Exist",Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(TouringPointsDetailActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        refroot.addListenerForSingleValueEvent(eventListener);
    }
}
