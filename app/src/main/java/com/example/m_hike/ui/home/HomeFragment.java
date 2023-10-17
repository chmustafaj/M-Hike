package com.example.m_hike.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_hike.AddHikeFragment;
import com.example.m_hike.ButtonVisibilityListener;
import com.example.m_hike.HikeRecyclerViewAdapter;
import com.example.m_hike.HomeScreenVisibilityListener;
import com.example.m_hike.MainActivity;
import com.example.m_hike.R;
import com.example.m_hike.database.AppDatabase;
import com.example.m_hike.databinding.FragmentHomeBinding;
import com.example.m_hike.objects.Hike;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    Toolbar homeToolbar;
    private RecyclerView recyclerViewHikes;
    HikeRecyclerViewAdapter adapter;
    private ConstraintLayout homeScreen;
    HomeScreenVisibilityListener homeScreenVisibilityListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initViews(root);
        AddHikeFragment addHikeFragment = new AddHikeFragment();
//        addHikeFragment.setHomeScreenCallBackInterface(this);
        AppDatabase db = AppDatabase.getInstance(getContext());
        ArrayList<Hike> allHikes = (ArrayList<Hike>) db.hikeDao().getAllHikes();
        adapter = new HikeRecyclerViewAdapter();
        adapter.setHikes(allHikes, getContext());
        recyclerViewHikes.setAdapter(adapter);
        recyclerViewHikes.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));


        Log.d("TAG", "onCreateView: "+allHikes);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    void initViews(View view){
        homeToolbar = view.findViewById(R.id.custom_toolbar);
        recyclerViewHikes = view.findViewById(R.id.recViewHikes);
        homeScreen = view.findViewById(R.id.homeScreen);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG", "onResume: ");
    }
}