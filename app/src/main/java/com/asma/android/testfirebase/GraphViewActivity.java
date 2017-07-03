package com.asma.android.testfirebase;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphViewActivity extends AppCompatActivity {


    static final String LIGHT = "Light";
    static final String TEMPERATURE = "Temperature";
    static final String HUMIDITY = "Humidity";

    FirebaseDatabase firebaseDatabase;

    private final Handler mHandler = new Handler();
    private Runnable mTimer;
    private double graphLastXValue = 5d;
    private LineGraphSeries<DataPoint> mSeriesLIGHT;
    private LineGraphSeries<DataPoint> mSeriesTEMPERATURE;
    private LineGraphSeries<DataPoint> mSeriesHUMIDITY;

    GraphView graphViewTEMPERATURE;
    GraphView graphViewHUMIDITY;
    GraphView graphViewLIGHT;


    double lastValuesTEMPERATURE = 0;
    double lastValuesLIGHT = 0;
    double lastValuesHUMIDITY = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_view_activity);
        graphViewLIGHT = (GraphView) findViewById(R.id.graphLIGHT);
        graphViewHUMIDITY = (GraphView) findViewById(R.id.graphHUMIDITY);
        graphViewTEMPERATURE = (GraphView) findViewById(R.id.graphTEMPERATURE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference();
        Query lastTem = myRef.child(TEMPERATURE).orderByKey().limitToLast(1);
        lastTem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null)
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        lastValuesTEMPERATURE = child.getValue(Double.class);
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Query lastLit = myRef.child(LIGHT).orderByKey().limitToLast(1);
        lastLit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null)
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        lastValuesLIGHT = child.getValue(Double.class);
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Query lastHum = myRef.child(HUMIDITY).orderByKey().limitToLast(1);
        lastHum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null)
                    if (dataSnapshot != null)
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            lastValuesHUMIDITY = child.getValue(Double.class);
                        }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        initGraph(graphViewLIGHT);
        initGraph(graphViewHUMIDITY);
        initGraph(graphViewTEMPERATURE);
    }

    public void initGraph(GraphView graph) {
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(4);

        graph.getGridLabelRenderer().setLabelVerticalWidth(100);

        // first mSeries is a line
        switch (graph.getId()) {
            case R.id.graphLIGHT: {
                mSeriesLIGHT = new LineGraphSeries<>();
                mSeriesLIGHT.setDrawDataPoints(true);
                mSeriesLIGHT.setDrawBackground(true);
                graph.addSeries(mSeriesLIGHT);
                break;
            }
            case R.id.graphHUMIDITY: {
                mSeriesHUMIDITY = new LineGraphSeries<>();
                mSeriesHUMIDITY.setDrawDataPoints(true);
                mSeriesHUMIDITY.setDrawBackground(true);
                graph.addSeries(mSeriesHUMIDITY);
                break;
            }
            case R.id.graphTEMPERATURE: {
                mSeriesTEMPERATURE = new LineGraphSeries<>();
                mSeriesTEMPERATURE.setDrawDataPoints(true);
                mSeriesTEMPERATURE.setDrawBackground(true);
                graph.addSeries(mSeriesTEMPERATURE);
                break;
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mTimer = new Runnable() {
            @Override
            public void run() {
                graphLastXValue += 0.25d;
                mSeriesTEMPERATURE.appendData(new DataPoint(graphLastXValue, lastValuesTEMPERATURE), true, 22);
                mSeriesHUMIDITY.appendData(new DataPoint(graphLastXValue, lastValuesHUMIDITY), true, 22);
                mSeriesLIGHT.appendData(new DataPoint(graphLastXValue, lastValuesLIGHT), true, 22);
                mHandler.postDelayed(this, 330);
            }
        };
        mHandler.postDelayed(mTimer, 1500);
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mTimer);
    }
}
