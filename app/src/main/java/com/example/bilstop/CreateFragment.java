package com.example.bilstop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class CreateFragment extends Fragment {
    private Button fromButton;
    private Button toButton;
    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        fromButton = view.findViewById(R.id.fromButton);
        toButton = view.findViewById(R.id.toButton);

        intent = new Intent(getActivity(), PlacesActivity.class);
        intent.putExtra("intentPage", "create");

        fromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                intent.putExtra("buttonType", "from");
                startActivity(intent);
            }
        });

        toButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("buttonType", "to");
                startActivity(intent);
            }
        });


        return view;
    }
}