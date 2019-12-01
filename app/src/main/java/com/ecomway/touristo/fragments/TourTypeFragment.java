package com.ecomway.touristo.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import com.ecomway.touristo.R;
import com.ecomway.touristo.adapter.TourTypeAdapter;

import java.util.ArrayList;
import java.util.List;

public class TourTypeFragment extends Fragment {

    RecyclerView tourList;
    TourTypeAdapter adapter;
    FrameLayout frame;
    ArrayAdapter<String> arrayAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_tour_type, container, false);
        frame=getActivity().findViewById(R.id.frame1);
        frame.setVisibility(View.GONE);
        tourList=v.findViewById(R.id.tourTypeList);
        adapter=new TourTypeAdapter(getActivity(),getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        tourList.setLayoutManager(mLayoutManager);
        tourList.setItemAnimator(new DefaultItemAnimator());
        tourList.setAdapter(adapter);


        return v;
    }


}
