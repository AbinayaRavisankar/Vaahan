package com.example.vaahan.vaahan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class PassengerOTPActivity extends AppCompatActivity {

    TextView otp, dist, fare;
    Random random;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_otp);

        random = new Random();

        otp = findViewById(R.id.otp);
        dist = findViewById(R.id.dist);
        fare = findViewById(R.id.fare);

        String id = String.format("%04d", random.nextInt(10000));
        otp.setText(id);

        String to = getIntent().getStringExtra("FROM_TEXT");
        String from = getIntent().getStringExtra("TO_TEXT");

        mDatabase = FirebaseDatabase.getInstance().getReference().getRoot().child("passengers").child(PassengerMapsActivity.passengerMob);
        mDatabase.child("from").setValue(from);
        mDatabase.child("to").setValue(to);
        mDatabase.child("dist").setValue(dist.getText().toString());
        mDatabase.child("fare").setValue(fare.getText().toString());

        // update driver with from,to data
        mDatabase = FirebaseDatabase.getInstance().getReference().getRoot().child("drivers");
        mDatabase.child("from").setValue(from);
        mDatabase.child("to").setValue(to);
        mDatabase.child("dist").setValue(dist.getText().toString());
        mDatabase.child("fare").setValue(fare.getText().toString());

    }

    public void map(View v) {
        Intent i = new Intent(this, PassengerMapsActivity.class);
        startActivity(i);
    }
}
