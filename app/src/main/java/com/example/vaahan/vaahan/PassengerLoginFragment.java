package com.example.vaahan.vaahan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class  PassengerLoginFragment extends Fragment {


    Button passengerLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_passenger_login, container, false);
        passengerLogin = rootView.findViewById(R.id.passenger_login);
        passengerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passengerPhone = ((EditText)rootView.findViewById(R.id.driver_mobile)).getText().toString();

                Intent i = new Intent(getActivity(), PassengerMapsActivity.class);
                i.putExtra("PASSENGER_PHONE",passengerPhone);
                startActivity(i);
            }
        });
        return rootView;
    }
}