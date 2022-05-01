package com.example.bilstop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.bilstop.DataPickers.AdapterActivity;

public class HomeFragment extends Fragment {

    private Button adapterbutton;
    private Button fromButton;
    private Button toButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        adapterbutton = view.findViewById(R.id.adapterbutton);
        fromButton = view.findViewById(R.id.fromButton);
        toButton = view.findViewById(R.id.toButton);

        adapterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AdapterActivity.class);
                startActivity(intent);
            }
        });

        fromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PlacesActivity.class);
                intent.putExtra("intentPage","home");
                startActivity(intent);
            }
        });

        return view;

    }
}