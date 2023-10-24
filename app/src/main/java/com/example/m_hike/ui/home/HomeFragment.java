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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_hike.AddHikeFragment;
import com.example.m_hike.ButtonVisibilityListener;
import com.example.m_hike.CallbackInterfaceAddHike;
import com.example.m_hike.HikeRecyclerViewAdapter;
import com.example.m_hike.HomeScreenVisibilityListener;
import com.example.m_hike.MainActivity;
import com.example.m_hike.R;
import com.example.m_hike.database.AppDatabase;
import com.example.m_hike.databinding.FragmentHomeBinding;
import com.example.m_hike.objects.Hike;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    Toolbar homeToolbar;
    private RecyclerView recyclerViewHikes;
    HikeRecyclerViewAdapter adapter;
    private ConstraintLayout homeScreen;
    private TextView Empty;
    ArrayList<Hike> allHikes = new ArrayList<>();
    HomeScreenVisibilityListener homeScreenVisibilityListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initViews(root);
        initRecView();
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // this method is called
                // when the item is moved.
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                Hike deletedHike = allHikes.get(viewHolder.getAdapterPosition());

                // below line is to get the position
                // of the item at that position.
                int position = viewHolder.getAdapterPosition();

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                allHikes.remove(viewHolder.getAdapterPosition());

                // below line is to notify our item is removed from adapter.
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                AppDatabase db = AppDatabase.getInstance(getContext());
                db.hikeDao().deleteSingleHike(deletedHike);

                // below line is to display our snackbar with action.
                Snackbar.make(recyclerViewHikes, deletedHike.name, Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        allHikes.add(position, deletedHike);
                        db.hikeDao().insertSingleHike(deletedHike);

                        // below line is to notify item is
                        // added to our adapter class.
                        adapter.notifyItemInserted(position);
                    }
                }).show();
            }
            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(recyclerViewHikes);




        return root;
    }

    private void initRecView() {
        AppDatabase db = AppDatabase.getInstance(getContext());
        allHikes = (ArrayList<Hike>) db.hikeDao().getAllHikes();
        if(allHikes.size()>0){
            Empty.setVisibility(View.GONE);
            adapter = new HikeRecyclerViewAdapter();
            adapter.setHikes(allHikes, getContext());
            recyclerViewHikes.setAdapter(adapter);
            recyclerViewHikes.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        }else{
            Empty.setVisibility(View.VISIBLE);
        }
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
        Empty = view.findViewById(R.id.txtEmpty);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG", "onResume: ");
    }


}