package com.example.m_hike.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.m_hike.Callbacks.ObservationAddedListener;
import com.example.m_hike.R;
import com.example.m_hike.Database.AppDatabase;
import com.example.m_hike.Models.Hike;
import com.example.m_hike.Models.Observation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddObservationsDialog extends DialogFragment {
    private Button addObservation;
    private TextView txtHike, txtDate, txtTime;
    private int hikeYear, hikeMonth, hikeDay;
    private TextInputEditText txtYourName, txtObservation;
    private ObservationAddedListener observationAddedListener;
    private String yourName, strDate, strTime, strObservation;
    private TextInputLayout edtYourName, edtObservation;
    Hike currentHike = null;

    public interface AddObservation{               //Setting up a callback interface so observations are added from the grocery activity
        void onAddObservationResult(Observation observation);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.add_observation_dialog,null);
        initViews(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Bundle bundle = getArguments();
        if(null!=bundle){
            int hid = bundle.getInt("hid", 0);
            AppDatabase db = AppDatabase.getInstance(getContext());
            List<Hike> hikes = db.hikeDao().getAllHikes();
            for(Hike h : hikes){
                if(h.hid == hid){
                    currentHike = h;
                }
            }
            if(null!=currentHike){
                txtHike.setText(currentHike.name);
            }


        }
        addObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                yourName =txtYourName.getText().toString();
                strObservation = txtObservation.getText().toString();
                strDate = txtDate.getText().toString();
                strTime = txtTime.getText().toString();
                if(!yourName.equals("") &&  !strDate.equals("") && !strTime.equals("") && !strObservation.equals("") ){
                    Observation newObservation = new Observation(currentHike.hid, txtYourName.getText().toString(),txtDate.getText().toString()+", "+txtTime.getText().toString(), txtObservation.getText().toString());
                    AppDatabase db = AppDatabase.getInstance(getContext());
                    db.observationDao().insertSingleObservation(newObservation);
                    if(observationAddedListener!=null){
                        observationAddedListener.callbackMethodObservation();
                    }
                    dismiss();

                }else{
                    if(yourName.equals("")){
                        edtYourName.setError("Name is required!");
                    }
                    if(strObservation.equals("")){
                        edtObservation.setError("Please write an observation");
                    }
                    if(strDate.equals("")){
                        txtDate.setError("Date is required!");
                    }
                    if(strTime.equals("")){
                        txtTime.setError("Time is required!");
                    }

                }


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
                        getActivity(),
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
        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        txtTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        return builder.create();
    }

    private void initViews(View view) {
        addObservation = view.findViewById(R.id.btnAddObservation);
        txtHike = view.findViewById(R.id.txtItemName);
        txtObservation = view.findViewById(R.id.txtObservation);
        txtYourName = view.findViewById(R.id.txtEnterYourName);
        txtDate = view.findViewById(R.id.date);
        txtTime = view.findViewById(R.id.time);
        edtYourName = view.findViewById(R.id.yourName);
        edtObservation = view.findViewById(R.id.observation);

        txtDate.setText(getCurrentDate());
        txtTime.setText(getCurrentTime());

    }
    private String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        String date = sdf.format(calendar.getTime());

        return date;
    }
    private String getCurrentTime(){
        // Get the current time
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm"); // 24-hour format
        return dateFormat.format(now);
    }
    public void setCallBackInterface(ObservationAddedListener observationAddedListener){
        this.observationAddedListener  = observationAddedListener;
    }
}
