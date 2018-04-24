package com.example.vaahan.vaahan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActualActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual);

        // TODO Get actual fares and distance and set in textViews

    }

    public void next(View v) {
        //remove ride details from DB
        mDatabase = FirebaseDatabase.getInstance().getReference().getRoot().child("drivers");
        mDatabase.child("to").removeValue();
        mDatabase.child("from").removeValue();
        mDatabase.child("dist").removeValue();
        mDatabase.child("fare").removeValue();

        Intent i = new Intent(this, RideDetailsActivity.class);
        startActivity(i);

    }
}
