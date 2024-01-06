package com.example.m_hike.Activities;

import android.os.Bundle;
import android.view.View;

import com.example.m_hike.Fragments.AddHikeFragment;
import com.example.m_hike.R;
import com.example.m_hike.Fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.m_hike.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    Toolbar myToolBar;
    FloatingActionButton createHike;
    BottomNavigationView bottomNavigationView;
    AddHikeFragment addHikeFragment;
    HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNavigationView = binding.navView;
        homeFragment = new HomeFragment();
        initViews();
        FirebaseApp.initializeApp(this); // Initialize Firebase


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);


        createHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHikeFragment = new AddHikeFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_bottom,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out_bottom  // popExit
                        )
                        .replace(R.id.nav_host_fragment_activity_main, addHikeFragment)
                        .addToBackStack(null)
                        .commit();
                createHike.setVisibility(View.GONE);
                myToolBar.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.GONE);

            }
        });

    }
    private void initViews(){
        createHike = findViewById(R.id.btnAddHike);
        myToolBar = findViewById(R.id.custom_toolbar);

    }



}