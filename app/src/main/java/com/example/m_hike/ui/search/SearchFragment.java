package com.example.m_hike.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_hike.HikeRecyclerViewAdapter;
import com.example.m_hike.R;
import com.example.m_hike.database.AppDatabase;
import com.example.m_hike.databinding.FragmentSearchBinding;
import com.example.m_hike.objects.Hike;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private TextInputEditText edtSearch;
    private RecyclerView recyclerView;
    private HikeRecyclerViewAdapter adapter;
    private ArrayList<Hike> allHikes = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel dashboardViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initViews(root);
        AppDatabase db = AppDatabase.getInstance(getContext());
        allHikes = (ArrayList<Hike>) db.hikeDao().getAllHikes();
        adapter = new HikeRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!edtSearch.getText().toString().equals("")) {
                    String name = edtSearch.getText().toString();
                    ArrayList<Hike> items =searchForItems(name);
                    if(items!=null){
                        adapter.setHikes(items, getContext());
                    }
                }else{
                    adapter.setHikes(new ArrayList<>(), getContext());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return root;
    }
    void initViews(View view){
        edtSearch = view.findViewById(R.id.txtSearch);
        recyclerView = view.findViewById(R.id.recyclerView);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public ArrayList<Hike> searchForItems(String text) {
        ArrayList<Hike> allItems = allHikes;
        if (null != allItems) {
            ArrayList<Hike> items = new ArrayList<>();
            String searchText = text.toLowerCase().replaceAll("\\s", ""); // Convert search text to lowercase and remove spaces

            for (Hike item : allItems) {
                String hikeName = item.getName().toLowerCase().replaceAll("\\s", ""); // Convert hike name to lowercase and remove spaces

                if (hikeName.contains(searchText)) {
                    items.add(item);
                }
            }

            return items;
        }

        return new ArrayList<>(); // Return an empty list if allItems is null
    }



}