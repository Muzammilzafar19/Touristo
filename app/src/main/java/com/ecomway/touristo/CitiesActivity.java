package com.ecomway.touristo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ecomway.touristo.adapter.CitiesAdapter;
import com.ecomway.touristo.model.CitiesModel;
import com.ecomway.touristo.room.database.RoomDatabaseClient;
import com.ecomway.touristo.room.entities.CitiesTable;

import java.util.ArrayList;
import java.util.List;

public class CitiesActivity extends AppCompatActivity {
RecyclerView cityrecycleviewList;
CitiesAdapter adapter;
List<CitiesModel> _list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        cityrecycleviewList=findViewById(R.id.cityrecycleviewList);
        adapter=new CitiesAdapter(populateCityList(),this);
        cityrecycleviewList.setHasFixedSize(true);

        cityrecycleviewList.setLayoutManager((new GridLayoutManager(this, 2)));
        cityrecycleviewList.setAdapter(adapter);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private List<CitiesModel> populateCityList()
    {_list=new ArrayList<>();
        for(CitiesTable ct: RoomDatabaseClient.getInstance(CitiesActivity.this).getAppDatabase().citiesTableDao().getAllCities())
        {
            CitiesModel cm=new CitiesModel();
            cm.setName(ct.getCityName());
            cm.setImagePath(ct.getCityImagePath());
            cm.setCityDescription(ct.getCityDescription());
            cm.setImage(0);
            _list.add(cm);
        }

        return _list;
    }
}
