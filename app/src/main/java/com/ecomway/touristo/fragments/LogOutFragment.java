package com.ecomway.touristo.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ecomway.touristo.HomeActivity;
import com.ecomway.touristo.LoginActivity;
import com.ecomway.touristo.R;
import com.ecomway.touristo.fragments.home.HomeFragment;
import com.ecomway.touristo.utils.SharePref;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class LogOutFragment extends Fragment {
    SharePref pref;
    private FirebaseAuth auth;
    FrameLayout frame;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new SharePref(Objects.requireNonNull(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_log_out, container, false);
        frame=getActivity().findViewById(R.id.frame1);
        frame.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Do You Want To Log Out ?");
        builder.setIcon(R.drawable.ic_exit);
// Add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                pref.setLoginIndicator("no");
                auth = FirebaseAuth.getInstance();
                auth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

                getFragmentManager().beginTransaction().replace(R.id.logoutfram,new HomeFragment()).commit();

                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return v;
    }
    public void replaceToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.commit();
    }

}
