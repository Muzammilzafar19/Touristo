package com.ecomway.touristo.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
import com.ecomway.touristo.adapter.SpotSearchAdapter;
import com.ecomway.touristo.adapter.TourTypeAdapter;


public class SpotSearchFragment extends Fragment {
    RecyclerView spotSearchList;
    SpotSearchAdapter adapter;
    FrameLayout frame;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_spot_search, container, false);
        frame = getActivity().findViewById(R.id.frame1);
        frame.setVisibility(View.GONE);
        spotSearchList = v.findViewById(R.id.spotSearchList);
        adapter = new SpotSearchAdapter(getActivity(), getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        spotSearchList.setLayoutManager(mLayoutManager);
        spotSearchList.setItemAnimator(new DefaultItemAnimator());
        spotSearchList.setAdapter(adapter);
        return v;
    }




}
