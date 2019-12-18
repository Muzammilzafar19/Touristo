package com.ecomway.touristo.fragments.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.ecomway.touristo.TouringPointsDetailActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment implements IndicationInterface {
    RecyclerView cityrecycle, tourpointlist;
    Button btnCityMore, btnTouringMore;
    List<CitiesModel> _list;
    List<TouringPointsModel> touringPointsModelList;
    FrameLayout frame;
     TouringPointsModel t;
    SearchView search;
    CitiesAdapter adapter;
    String globalRating="0";
    private DatabaseReference mDatabase, refroot;
    TouringPointsAdapter touringPointsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        toggleSearchBar("yes");
        _list = new ArrayList<>();
        touringPointsModelList = new ArrayList<>();

        frame = getActivity().findViewById(R.id.frame1);
        frame.setVisibility(View.VISIBLE);
        cityrecycle = root.findViewById(R.id.ctylist);
        btnCityMore = root.findViewById(R.id.btnCityMore);
        btnTouringMore = root.findViewById(R.id.btnTourMore);
        tourpointlist = root.findViewById(R.id.tourpointlist);
        populateList();





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
        adapter = new CitiesAdapter(_list, getActivity());
        cityrecycle.setHasFixedSize(true);
        cityrecycle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        cityrecycle.setAdapter(adapter);

        int count=0;
        for(TouringPointsTable tpt: RoomDatabaseClient.getInstance(getActivity()).getAppDatabase().touringPointsTableDao().getAll())
        {
           if(count<4)
           {    t = new TouringPointsModel();
               t.setDescription(tpt.getTouringPointDescription());
               t.setTouingPointName(tpt.getTouringPointName());
               t.setImagePath(tpt.getTouringPointImagePath());
               final int min = 1;
               final int max = 5;
               final int random = new Random().nextInt((max - min) + 1) + min;
               t.setRating(String.valueOf(random));
               touringPointsModelList.add(t);
               count++;
           }
           else {
               break;
           }

        }
        touringPointsAdapter = new TouringPointsAdapter(getActivity(), touringPointsModelList);
        tourpointlist.setHasFixedSize(true);
        tourpointlist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        tourpointlist.setAdapter(touringPointsAdapter);
    }

    @Override
    public void toggleSearchBar(String indication) {

    }
    private void  checkRating(final TouringPointsTable tourname) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        refroot = mDatabase.child("Rating").child(tourname.getTouringPointName());
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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

                    }
                    globalRating=String.valueOf(average);

                    t.setRating(globalRating);

                    Log.i("chkAverg", average + "");
                    // Toast.makeText(ProfileActivity.this,"Value Exist",Toast.LENGTH_SHORT).show();
                } else {

                    t.setRating("0");

                    progressDialog.dismiss();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        refroot.addListenerForSingleValueEvent(eventListener);
    }
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                int time = Integer.parseInt(params[0])*1000;

                Thread.sleep(time);
                resp = "Slept for " + params[0] + " seconds";
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();

        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(),
                    "ProgressDialog",
                    "Wait a seconds");
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }
}