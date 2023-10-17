package com.example.m_hike;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.m_hike.database.AppDatabase;
import com.example.m_hike.objects.Hike;
import com.example.m_hike.ui.home.HomeFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.Calendar;


public class AddHikeFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    ButtonVisibilityListener buttonVisibilityListener;
//    HomeScreenVisibilityListener homeScreenVisibilityListener;

    private DialogInterface.OnClickListener dialogClickListener;
    private AppDatabase db;
    private String name, location,  difficulty, desc;
    private int hikeYear, hikeMonth, hikeDay, length =-1;
    private boolean parkingIsAvailable;
    TextInputLayout datePicker;
    TextInputEditText showDate;
    TextInputEditText parkingAvailable;
    Button btnClose, btnDone;
    Spinner difficultiesSpinner;
    private TextInputEditText edtName, edtLocation, edtDesc, edtLength;
    private TextInputLayout textBoxName, textBoxLocation, textBoxDesc, textBoxLength, textBoxDate;
    private ImageView btnAddImage;
    private Bitmap hikeImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_hike, container, false);
        initViews(view);
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
                            hikeImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }


                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });
        db=AppDatabase.getInstance(getContext());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.Difficulties ,
                android.R.layout.simple_spinner_dropdown_item
        );
// Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner.
        difficultiesSpinner.setAdapter(adapter);
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the photo picker and let the user choose only images.
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());

            }
        });
        showDate.setOnClickListener(new View.OnClickListener() {
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
                        getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                hikeYear = year;
                                hikeDay = day;
                                hikeMonth = month;

                                showDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

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
                // Pop the back stack to close the menu with a slide_out exit transition
                close();

            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name =edtName.getText().toString();
                location = edtLocation.getText().toString();
                desc = edtDesc.getText().toString();
                difficulty = difficultiesSpinner.getSelectedItem().toString();
                if (parkingAvailable.getText().toString().equals("Yes")){
                    parkingIsAvailable = true;
                }else{
                    parkingIsAvailable = false;
                }
                if(!name.equals("") && !location.equals("") && !difficulty.equals("") && hikeYear!=-1 && !edtLength.getText().toString().equals("") && hikeMonth !=-1 && hikeDay!=-1 ){
                    length = Integer.parseInt(edtLength.getText().toString());

                    saveHike();
                    close();
                }else{
                    if(name.equals("")){
                        textBoxName.setError("Hike name is required!");
                    }
                    if(location.equals("")){
                        textBoxLocation.setError("Location is required!");
                    }
                    if(hikeYear == -1 || hikeDay == -1 || hikeMonth == -1){
                        textBoxDate.setError("Date is required!");
                    }
                    if(length==-1){
                        textBoxLength.setError("Length is required!");
                    }

                }


            }
        });
        parkingAvailable.setOnClickListener(new View.OnClickListener() {
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
                                parkingAvailable.setText("Yes");
                                break;
                            // on below line we are setting click listener
                            // for our negative button.
                            case DialogInterface.BUTTON_NEGATIVE:
                                // on below line we are dismissing our dialog box.
                                parkingAvailable.setText("No");
                                dialog.dismiss();

                        }
                    }
                };
                // on below line we are creating a builder variable for our alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

        return view;
    }

    private void close() {
        if(buttonVisibilityListener!=null){
            buttonVisibilityListener.callBackMethod();
        }
//        if(homeScreenVisibilityListener!=null){
//            homeScreenVisibilityListener.callBackMethodHomeScreen();
//        }
        getActivity().onBackPressed();
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(buttonVisibilityListener!=null){
            buttonVisibilityListener.callBackMethod();
        }
        Log.d("TAG", "onStop: ");
    }

    private void saveHike() {
        db.hikeDao().insertSingleHike(new Hike(name, location, hikeYear, hikeMonth, hikeDay, parkingIsAvailable, length, difficulty, desc, hikeImage));

    }

    void initViews(View view){
        btnClose = view.findViewById(R.id.crossButton);
        datePicker = view.findViewById(R.id.date);
        showDate = view.findViewById(R.id.txtEnterDate);
        parkingAvailable = view.findViewById(R.id.txtParking);
        difficultiesSpinner = view.findViewById(R.id.difficultySpinner);
        edtName = view.findViewById(R.id.txtEnterHikeName);
        edtLocation = view.findViewById(R.id.txtEnterLocation);
        edtDesc = view.findViewById(R.id.txtDesc);
        edtLength = view.findViewById(R.id.txtLength);
        textBoxName = view.findViewById(R.id.nameOfHike);
        textBoxDesc = view.findViewById(R.id.description);
        textBoxLength = view.findViewById(R.id.length);
        textBoxLocation = view.findViewById(R.id.location);
        textBoxDate = view.findViewById(R.id.date);
        btnDone = view.findViewById(R.id.checkButton);
        btnAddImage = view.findViewById(R.id.btnAddImage);
    }

    public void setCallBackInterface(ButtonVisibilityListener buttonVisibilityListener){
         this.buttonVisibilityListener  = buttonVisibilityListener;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item is selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos).
    }
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback.
    }

}