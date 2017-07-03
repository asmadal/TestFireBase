package com.asma.android.testfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.graphActivity);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, GraphViewActivity.class));
            }
        });
        sendFireBaseData();
    }

    public void sendFireBaseData() {
        int max = 10;
        int min = 5;
        int totalNumber = 10;
        Random random = new Random();

        final String LIGHT = "Light";
        final String TEMPERATURE = "Temperature";
        final String HUMIDITY = "Humidity";
        FirebaseDatabase firebaseDatabase;
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference();
        for (int i = 0; i < 100; i++) {
            myRef.child(LIGHT).push().setValue(random.nextInt(100));
            myRef.child(TEMPERATURE).push().setValue(random.nextInt(100));
            myRef.child(HUMIDITY).push().setValue(random.nextInt(100));
        }
    }

}
