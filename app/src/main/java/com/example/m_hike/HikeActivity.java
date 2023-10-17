package com.example.m_hike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.m_hike.database.AppDatabase;
import com.example.m_hike.objects.Hike;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class HikeActivity extends AppCompatActivity {
    private ImageView hikeImage;
    private Bitmap image;
    private TextView txtHikeName, txtHikeLocation, txtHikeDate, txtParkingAvailable, txtHikeDifficulty, txtHikeDescription, txtHikeLength;
    private String hikeName, hikeLocation, desc, hikeDifficulty;
    private int hikeLength, hikeYear, hikeMonth, hikeDay, hid;
    private boolean parkingAvailable;

    private ImageButton editButton;
    Hike currentHike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike);
        Intent intent = getIntent();
        if(intent!=null){
            hid = intent.getIntExtra("hid", 0);
            AppDatabase db = AppDatabase.getInstance(HikeActivity.this);
            ArrayList<Hike> allHikes = (ArrayList<Hike>) db.hikeDao().getAllHikes();
            for (Hike h: allHikes){
                if(h.hid== hid){
                    currentHike = h;
                }
            }
            initViews();
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


        hikeImage.setImageBitmap(currentHike.image);
        txtHikeLength.setText(String.valueOf(currentHike.lengthOfHeightInMeters));
        txtHikeName.setText(currentHike.name);
        txtHikeDescription.setText(currentHike.desc);
        txtHikeDifficulty.setText("Difficulty: "+currentHike.difficulty);
        txtHikeLocation.setText(currentHike.location);
        Log.d("TAG", "initViews: hike location "+hikeLocation);
        txtParkingAvailable.setText("Parking Available: "+String.valueOf(currentHike.parkingIsAvailable));
        txtHikeDate.setText(currentHike.day + "-" + (currentHike.month+ 1) + "-" + currentHike.year);



    }
}