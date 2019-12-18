package com.ecomway.touristo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecomway.touristo.adapter.CitiesAdapter;
import com.ecomway.touristo.adapter.TouringPointsAdapter;
import com.ecomway.touristo.model.CitiesModel;
import com.ecomway.touristo.model.TouringPointsModel;
import com.ecomway.touristo.room.database.RoomDatabaseClient;
import com.ecomway.touristo.room.entities.CitiesTable;
import com.ecomway.touristo.room.entities.TouringPointsTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TouringPointsActivity extends AppCompatActivity {
    RecyclerView recycleviewTour;
    TouringPointsAdapter adapter;
    List<TouringPointsModel> _list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touring_points);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recycleviewTour=findViewById(R.id.recycleviewTour);

        if(Objects.requireNonNull(getIntent().getExtras()).getString("type")!=null&&!Objects.requireNonNull(getIntent().getExtras()).getString("type").equals(""))
        {
            adapter=new TouringPointsAdapter(this,populateTourListByType(Objects.requireNonNull(getIntent().getExtras()).getString("type").toLowerCase()));
        }
        else {
            adapter=new TouringPointsAdapter(this,populateCityList());
        }

        recycleviewTour.setHasFixedSize(true);

        recycleviewTour.setLayoutManager((new GridLayoutManager(this, 2)));
        recycleviewTour.setAdapter(adapter);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private List<TouringPointsModel> populateCityList()
    {_list=new ArrayList<>();
        for(TouringPointsTable ct: RoomDatabaseClient.getInstance(TouringPointsActivity.this).getAppDatabase().touringPointsTableDao().getAll())
        {
            TouringPointsModel cm=new TouringPointsModel();
            cm.setTouingPointName(ct.getTouringPointName());
            cm.setImagePath(ct.getTouringPointImagePath());
            cm.setDescription(ct.getTouringPointDescription());
            _list.add(cm);
        }

        return _list;
    }
    private List<TouringPointsModel> populateTourListByType(String type)
    {_list=new ArrayList<>();
        for(TouringPointsTable ct: RoomDatabaseClient.getInstance(TouringPointsActivity.this).getAppDatabase().touringPointsTableDao().getSpecificTourByType(type))
        {
            TouringPointsModel cm=new TouringPointsModel();
            cm.setTouingPointName(ct.getTouringPointName());
            cm.setImagePath(ct.getTouringPointImagePath());
            cm.setDescription(ct.getTouringPointDescription());
            _list.add(cm);
        }

        return _list;
    }

}
