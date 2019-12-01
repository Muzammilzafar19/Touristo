package com.ecomway.touristo.fragments.event;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ecomway.touristo.EventsDetailsActivity;
import com.ecomway.touristo.R;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class EventFragment extends Fragment {

    private ListView listView;
    FrameLayout frame;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_event, container, false);
        frame=getActivity().findViewById(R.id.frame1);
        frame.setVisibility(View.GONE);
        listView = root.findViewById(R.id.listView);

        final List<String> dataList = new ArrayList<String>();
        dataList.add("National");
        dataList.add("RELIGIOUS");
        dataList.add("CULTURAL");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), dataList.get(i), Toast.LENGTH_SHORT).show();

                    getActivity().startActivity(new Intent(getActivity(), EventsDetailsActivity.class).putExtra("pagename", dataList.get(i)));
                    getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });
        return root;
    }
}