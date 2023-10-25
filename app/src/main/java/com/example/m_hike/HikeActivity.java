package com.example.m_hike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.m_hike.database.AppDatabase;
import com.example.m_hike.objects.Hike;
import com.example.m_hike.objects.Observation;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class HikeActivity extends AppCompatActivity implements ObservationAddedListener {
    private ImageView hikeImage;
    private RecyclerView observationsRecView;
    private TextView txtAddObservations;
    private Bitmap image;
    private TextView txtHikeName, txtHikeLocation, txtHikeDate, txtParkingAvailable, txtHikeDifficulty, txtHikeDescription, txtHikeLength, txtHikeElevation;
    private String hikeName, hikeLocation, desc, hikeDifficulty;
    private int hikeLength, hikeYear, hikeMonth, hikeDay, hid, hikeElevation;
    private boolean parkingAvailable;

    private ImageButton editButton;
    private ObservationsRecyclerViewAdapter adapter = new ObservationsRecyclerViewAdapter();
    Hike currentHike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike);
        Intent intent = getIntent();
        initViews();

        if(intent!=null){
            hid = intent.getIntExtra("hid", 0);
            initRecView();
            setViews();
            txtAddObservations.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddObservationsDialog dialog = new AddObservationsDialog();
                    dialog.setCallBackInterface(HikeActivity.this);
                    Bundle bundle = new Bundle();
                    bundle.putInt("hid",hid);
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(),"Add an observation");
                }
            });
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(HikeActivity.this, EditHikeActivity.class);
                    i.putExtra("hid", hid);
                    startActivity(i);


                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(HikeActivity.this, MainActivity.class));
    }
    void setViews(){
        hikeImage.setImageBitmap(currentHike.image);
        txtHikeLength.setText("Length: "+String.valueOf(currentHike.lengthOfHeightInMeters));
        txtHikeName.setText(currentHike.name);
        txtHikeDescription.setText(currentHike.desc);
        txtHikeDifficulty.setText("Difficulty: "+currentHike.difficulty);
        txtHikeLocation.setText(currentHike.location);
        Log.d("TAG", "initViews: hike location "+hikeLocation);
        txtParkingAvailable.setText("Parking Available: "+String.valueOf(currentHike.parkingIsAvailable));
        txtHikeDate.setText(currentHike.day + "-" + (currentHike.month+ 1) + "-" + currentHike.year);
        if(currentHike.elevation!=0){
            txtHikeElevation.setText("Elevation: "+currentHike.elevation);
        }else{
            txtHikeElevation.setVisibility(View.GONE);
        }

    }
    void initViews(){
        hikeImage = findViewById(R.id.hike_image);
        txtHikeName = findViewById(R.id.hike_name);
        txtHikeLocation = findViewById(R.id.hike_location);
        txtHikeDate = findViewById(R.id.hike_date);
        txtParkingAvailable = findViewById(R.id.parking_available);
        txtHikeDifficulty = findViewById(R.id.hike_difficulty);
        txtHikeDescription = findViewById(R.id.hike_description);
        txtHikeLength = findViewById(R.id.hike_length);
        editButton = findViewById(R.id.edit_button);
        txtAddObservations = findViewById(R.id.txtAddObservation);
        observationsRecView = findViewById(R.id.observationsRecView);
        txtHikeElevation = findViewById(R.id.hike_elevation);





    }
    void initRecView(){
        AppDatabase db = AppDatabase.getInstance(HikeActivity.this);
        ArrayList<Hike> allHikes = (ArrayList<Hike>) db.hikeDao().getAllHikes();
        ArrayList<Observation> allObservations = (ArrayList<Observation>) db.observationDao().getAllObservations();
        ArrayList<Observation> observations = new ArrayList<>();
        for (Hike h: allHikes){
            if(h.hid== hid){
                currentHike = h;
            }
        }
        observationsRecView.setAdapter(adapter);
        observationsRecView.setLayoutManager(new LinearLayoutManager(this));
        for (Observation o : allObservations){
            if(o.hid == currentHike.hid){
                observations.add(o);
            }
        }

        if(observations!=null){
            if(observations.size()>0){
                adapter.setObservations(observations);
            }
        }
    }

    @Override
    public void callbackMethodObservation() {
        initRecView();
        Log.d("TAG", "callbackMethodObservation: ");
    }
}