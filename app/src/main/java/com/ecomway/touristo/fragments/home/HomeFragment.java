package com.ecomway.touristo.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecomway.touristo.CitiesActivity;
import com.ecomway.touristo.R;
import com.ecomway.touristo.TouringPointsActivity;
import com.ecomway.touristo.adapter.CitiesAdapter;
import com.ecomway.touristo.adapter.TouringPointsAdapter;
import com.ecomway.touristo.fragments.SearchableFragment;
import com.ecomway.touristo.fragments.TourTypeFragment;
import com.ecomway.touristo.helpinginterface.IndicationInterface;
import com.ecomway.touristo.model.CitiesModel;
import com.ecomway.touristo.model.TouringPointsModel;
import com.ecomway.touristo.room.database.RoomDatabaseClient;
import com.ecomway.touristo.room.entities.CitiesTable;
import com.ecomway.touristo.room.entities.TouringPointsTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements IndicationInterface {
    RecyclerView cityrecycle, tourpointlist;
    Button btnCityMore, btnTouringMore;
    List<CitiesModel> _list;
    List<TouringPointsModel> touringPointsModelList;
    FrameLayout frame;
    SearchView search;
    CitiesAdapter adapter;
    TouringPointsAdapter touringPointsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        toggleSearchBar("yes");
        _list = new ArrayList<>();
        touringPointsModelList = new ArrayList<>();
        populateList();
        frame = getActivity().findViewById(R.id.frame1);
        frame.setVisibility(View.VISIBLE);
        cityrecycle = root.findViewById(R.id.ctylist);
        btnCityMore = root.findViewById(R.id.btnCityMore);
        btnTouringMore = root.findViewById(R.id.btnTourMore);
        tourpointlist = root.findViewById(R.id.tourpointlist);


        adapter = new CitiesAdapter(_list, getActivity());
        touringPointsAdapter = new TouringPointsAdapter(getActivity(), touringPointsModelList);

        cityrecycle.setHasFixedSize(true);

        cityrecycle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        cityrecycle.setAdapter(adapter);

        tourpointlist.setHasFixedSize(true);
        tourpointlist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        tourpointlist.setAdapter(touringPointsAdapter);

        OnClicked();
        return root;
    }

    private void OnClicked() {
        btnCityMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             getActivity().startActivity(new Intent(getActivity(), CitiesActivity.class));
            }
        });
        btnTouringMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), TouringPointsActivity.class).putExtra("type",""));
            }
        });
    }

    public void replaceToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.commit();
    }

    private void populateList() {

        int i=0;
        for(CitiesTable ct: RoomDatabaseClient.getInstance(getActivity()).getAppDatabase().citiesTableDao().getAllCities())
        {
            if(i<4)
            {
                CitiesModel cm = new CitiesModel();
                cm.setCityDescription(ct.getCityDescription());
                cm.setName(ct.getCityName());
                cm.setImagePath(ct.getCityImagePath());
                cm.setImage(0);
                _list.add(cm);
                i++;
            }
            else {
                break;
            }

        }

        int count=0;
        for(TouringPointsTable tpt: RoomDatabaseClient.getInstance(getActivity()).getAppDatabase().touringPointsTableDao().getAll())
        {
           if(count<4)
           {
               TouringPointsModel t = new TouringPointsModel();
               t.setDescription(tpt.getTouringPointDescription());
               t.setTouingPointName(tpt.getTouringPointName());
               t.setImagePath(tpt.getTouringPointImagePath());
               touringPointsModelList.add(t);
               count++;
           }
           else {
               break;
           }

        }
    }

    @Override
    public void toggleSearchBar(String indication) {

    }
}