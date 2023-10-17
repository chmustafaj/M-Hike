package com.example.m_hike;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.m_hike.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements ButtonVisibilityListener {

    private ActivityMainBinding binding;
    Toolbar myToolBar;
    FloatingActionButton createHike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        createHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeFragment fragment = new AddHikeFragment();
                fragment.setCallBackInterface(MainActivity.this);
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_bottom,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out_bottom  // popExit
                        )
                        .replace(R.id.nav_host_fragment_activity_main, fragment)
                        .addToBackStack(null)
                        .commit();
                createHike.setVisibility(View.GONE);
                myToolBar.setVisibility(View.GONE);

            }
        });

    }
    private void initViews(){
        createHike = findViewById(R.id.btnAddHike);
        myToolBar = findViewById(R.id.custom_toolbar);

    }

    @Override
    public void callBackMethod() {
        myToolBar.setVisibility(View.VISIBLE);
        createHike.setVisibility(View.VISIBLE);
        Log.d("TAG", "onButtonVisibilityChange: ");
    }



}