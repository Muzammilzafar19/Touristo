package com.ecomway.touristo.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ecomway.touristo.R;


public class ThingsToKeepFragment extends Fragment {
FrameLayout frame;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frame=getActivity().findViewById(R.id.frame1);
        frame.setVisibility(View.GONE);
        return inflater.inflate(R.layout.fragment_things_to_keep, container, false);
    }

}
