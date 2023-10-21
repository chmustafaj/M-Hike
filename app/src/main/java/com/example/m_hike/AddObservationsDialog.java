package com.example.m_hike;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.m_hike.database.AppDatabase;
import com.example.m_hike.objects.Hike;
import com.example.m_hike.objects.Observation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddObservationsDialog extends DialogFragment {
    private Button addObservation;
    private TextView txtHike;
    private TextInputEditText txtYourName, txtObservation;
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
                Observation newObservation = new Observation(currentHike.hid, txtYourName.getText().toString(),getCurrentDate(), txtObservation.getText().toString());
                AppDatabase db = AppDatabase.getInstance(getContext());
                db.observationDao().insertSingleObservation(newObservation);

            }
        });
        return builder.create();
    }

    private void initViews(View view) {
        addObservation = view.findViewById(R.id.btnAddObservation);
        txtHike = view.findViewById(R.id.txtItemName);
        txtObservation = view.findViewById(R.id.txtObservation);
        txtYourName = view.findViewById(R.id.txtEnterYourName);
    }
    private String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        return sdf.format(calendar.getTime());
    }
}
