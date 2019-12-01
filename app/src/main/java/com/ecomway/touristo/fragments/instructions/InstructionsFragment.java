package com.ecomway.touristo.fragments.instructions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ecomway.touristo.R;

public class InstructionsFragment extends Fragment {
FrameLayout frame;
    private InstructionsViewModel instructionsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_instructions, container, false);
        frame=getActivity().findViewById(R.id.frame1);
        frame.setVisibility(View.GONE);
return root;
    }
}