package com.example.vaahan.vaahan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RideDetailsActivity extends AppCompatActivity {

    TextView from, to, dist, fare;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);

        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        dist = findViewById(R.id.dist);
        fare = findViewById(R.id.fare);

        initial();

        mDatabase = FirebaseDatabase.getInstance().getReference().getRoot().child("drivers");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    if(data.getKey().equals("from")) {
                        from.setText(data.getValue().toString());
                    }
                    if(data.getKey().equals("to")) {
                        to.setText(data.getValue().toString());
                    }

                    if(data.getKey().equals("dist")) {
                        dist.setText(data.getValue().toString());
                    }
                    if(data.getKey().equals("fare")) {
                        fare.setText(data.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Error occurred",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void decline(View v) {
        initial();

        // Remove from database
        mDatabase = FirebaseDatabase.getInstance().getReference().getRoot().child("drivers");
        mDatabase.child("to").removeValue();
        mDatabase.child("from").removeValue();
        mDatabase.child("dist").removeValue();
        mDatabase.child("fare").removeValue();
    }

    public void accept(View v) {
        Intent i = new Intent(this, DriverMapsActivity.class);
        startActivity(i);
    }

    public void initial() {
        from.setText("-");
        to.setText("-");
        dist.setText("-");
        fare.setText("-");
    }
}
