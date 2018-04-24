package com.example.vaahan.vaahan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.*;

public class DriverLoginFragment extends Fragment {


    Button driverLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_driver_login, container, false);
        driverLogin = rootView.findViewById(R.id.driver_login);

        driverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), RideDetailsActivity.class);
                startActivity(i);
            }
        });
        return rootView;
    }
}
