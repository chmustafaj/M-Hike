package com.example.m_hike.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.m_hike.R;
import com.example.m_hike.Database.AppDatabase;
import com.example.m_hike.Models.Hike;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class EditHikeActivity extends AppCompatActivity {
    private AppDatabase db;
    private Hike currentHike;
    private DialogInterface.OnClickListener dialogClickListener;

    private String hikeName, hikeLocation, desc, hikeDifficulty;
    private int hikeLength, hikeYear, hikeMonth, hikeDay, hid;
    private boolean parkingAvailable;
    private Bitmap image;
    TextInputLayout datePicker;
    private ImageView btnAddImage;
    private TextView txtDate, txtParking;

    Button btnClose, btnDone;
    Spinner difficultiesSpinner;
    private TextInputEditText edtName, edtLocation, edtDesc, edtLength, edtElevation;
    private TextInputLayout textBoxName, textBoxLocation, textBoxDesc, textBoxLength;
    private Bitmap hikeImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hike);
        db=AppDatabase.getInstance(EditHikeActivity.this);

        Intent intent = getIntent();
        if (intent != null) {
//
            hid = intent.getIntExtra("hid", 0);
            Log.d("TAG", "onCreate: id : "+hid);
            AppDatabase db = AppDatabase.getInstance(EditHikeActivity.this);
            ArrayList<Hike> allHikes = (ArrayList<Hike>) db.hikeDao().getAllHikes();
            for (Hike h: allHikes){
                if(h.hid== hid){
                    currentHike = h;
                }
            }
            hikeImage = currentHike.image;
//
            initViews();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    EditHikeActivity.this,
                    R.array.Difficulties ,
                    android.R.layout.simple_spinner_dropdown_item
            );
// Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner.
            difficultiesSpinner.setAdapter(adapter);
            // Registers a photo picker activity launcher in single-select mode.
            ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                    registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                        // Callback is invoked after the user selects a media item or closes the
                        // photo picker.
                        if (uri != null) {
                            Log.d("PhotoPicker", "Selected URI: " + uri);
                            btnAddImage.setImageBitmap(null);
                            btnAddImage.setImageURI(uri);
                            try {
                                hikeImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }


                        } else {
                            Log.d("PhotoPicker", "No media selected");
                        }
                    });
            db= AppDatabase.getInstance(this);
            btnAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Launch the photo picker and let the user choose only images.
                    pickMedia.launch(new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build());

                }
            });
            txtDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // on below line we are getting
                    // the instance of our calendar.
                    final Calendar c = Calendar.getInstance();

                    // on below line we are getting
                    // our day, month and year.
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    // on below line we are creating a variable for date picker dialog.
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            // on below line we are passing context.
                            EditHikeActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    // on below line we are setting date to our text view.
                                    hikeYear = year;
                                    hikeDay = day;
                                    hikeMonth = month;

                                    txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                }
                            },
                            // on below line we are passing year,
                            // month and day for selected date in our date picker.
                            year, month, day);
                    // at last we are calling show to
                    // display our date picker dialog.
                    datePickerDialog.show();
                }
            });
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    close();

                }
            });
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hikeName =edtName.getText().toString();
                    hikeLocation = edtLocation.getText().toString();
                    desc = edtDesc.getText().toString();
                    hikeDifficulty = difficultiesSpinner.getSelectedItem().toString();
                    if (txtParking.getText().toString().equals("Yes")){
                        parkingAvailable = true;
                    }else{
                        parkingAvailable = false;
                    }
                    if(!hikeName.equals("") && !hikeLocation.equals("") && !hikeDifficulty.equals("") && hikeYear!=-1 && !edtLength.getText().toString().equals("") && hikeMonth !=-1 && hikeDay!=-1 ){
                        hikeLength = Integer.parseInt(edtLength.getText().toString());
                        saveHike();
                        close();
                    }else{
                        if(hikeName.equals("")){
                            textBoxName.setError("Hike name is required!");
                        }
                        if(hikeLocation.equals("")){
                            textBoxLocation.setError("Location is required!");
                        }
                        if(hikeYear == -1 || hikeDay == -1 || hikeMonth == -1){
                            txtDate.setError("Date is required!");
                        }
                        if(hikeLength==-1){
                            textBoxLength.setError("Length is required!");
                        }

                    }


                }
            });
            txtParking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                // on below line we are setting a click listener
                                // for our positive button
                                case DialogInterface.BUTTON_POSITIVE:
                                    // on below line we are displaying a toast message.
                                    txtParking.setText("Yes");
                                    break;
                                // on below line we are setting click listener
                                // for our negative button.
                                case DialogInterface.BUTTON_NEGATIVE:
                                    // on below line we are dismissing our dialog box.
                                    txtParking.setText("No");
                                    dialog.dismiss();

                            }
                        }
                    };
                    // on below line we are creating a builder variable for our alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditHikeActivity.this);
                    // on below line we are setting message for our dialog box.
                    builder.setMessage("Select yes if parking is available")
                            // on below line we are setting positive button
                            // and setting text to it.
                            .setPositiveButton("Yes", dialogClickListener)
                            // on below line we are setting negative button
                            // and setting text to it.
                            .setNegativeButton("No", dialogClickListener)
                            // on below line we are calling
                            // show to display our dialog.
                            .show();

                }
            });

        }
    }
    void initViews(){
        btnClose = findViewById(R.id.crossButton);
        difficultiesSpinner = findViewById(R.id.difficultySpinner);
        edtName = findViewById(R.id.txtEnterHikeName);
        edtLocation = findViewById(R.id.txtEnterLocation);
        edtDesc = findViewById(R.id.txtDesc);
        edtLength = findViewById(R.id.txtLength);
        textBoxName = findViewById(R.id.nameOfHike);
        textBoxDesc = findViewById(R.id.description);
        textBoxLength = findViewById(R.id.length);
        textBoxLocation = findViewById(R.id.location);
        btnDone = findViewById(R.id.checkButton);
        btnAddImage = findViewById(R.id.btnAddImage);
        edtElevation = findViewById(R.id.txtElevation);
        txtDate = findViewById(R.id.date);
        txtParking = findViewById(R.id.parking);

        btnAddImage.setImageBitmap(currentHike.image);
        edtLength.setText(String.valueOf(currentHike.lengthOfHeightInMeters));
        edtDesc.setText(currentHike.desc);
        switch (currentHike.difficulty){
            case "Easy":
                difficultiesSpinner.setSelection(0,false);
            case "Medium":
                difficultiesSpinner.setSelection(1, false);
            case "Hard":
                difficultiesSpinner.setSelection(2, false);
        }
        edtLocation.setText(currentHike.location);
        edtElevation.setText(String.valueOf(currentHike.elevation));
        txtParking.setText(Boolean.toString(currentHike.parkingIsAvailable));
        txtDate.setText(currentHike.day+ "-" + (currentHike.month + 1) + "-" + currentHike.year);
        edtName.setText(currentHike.name);
    }
    private void saveHike() {
        if(hikeImage==null){
            hikeImage = BitmapFactory.decodeResource(getResources(), R.drawable.image);

        }
        String strElevation = edtElevation.getText().toString();
        int elevation = 0;
        if(!strElevation.equals("")){
            elevation = Integer.parseInt(edtElevation.getText().toString());
        }
        Hike editedHike = new Hike(edtName.getText().toString(), edtLocation.getText().toString(), hikeYear, hikeMonth, hikeDay, Boolean.getBoolean(txtParking.getText().toString()), Integer.parseInt(edtLength.getText().toString()), difficultiesSpinner.getSelectedItem().toString(), edtDesc.getText().toString(), hikeImage, elevation);
        editedHike.hid = hid;
        db.hikeDao().update(editedHike);


    }
    private void close() {
        Intent intent = new Intent(EditHikeActivity.this, HikeActivity.class);
        intent.putExtra("hid", hid);
        startActivity(intent);
        finish();
    }
}