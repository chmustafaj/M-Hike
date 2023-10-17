package com.example.m_hike.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.m_hike.R;
import com.example.m_hike.databinding.FragmentSearchBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardFragment extends Fragment {
    private FloatingActionButton addHike;
    private FragmentSearchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initViews(root);

        return root;
    }
    void initViews(View view){
//        addHike = view.findViewById(R.id.btnAddHike);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}