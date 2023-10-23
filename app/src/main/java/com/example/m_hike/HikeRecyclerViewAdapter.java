package com.example.m_hike;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_hike.objects.Hike;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;

public class HikeRecyclerViewAdapter extends RecyclerView.Adapter<HikeRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Hike> hikesList = new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hike_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.hikeLocation.setText(hikesList.get(position).location);
        holder.hikeName.setText(hikesList.get(position).name);

        holder.hikeImageView.setImageBitmap(hikesList.get(position).image);
        int year = hikesList.get(position).year;
        int month = hikesList.get(position).month;
        int day = hikesList.get(position).day;
        String formattedDate = String.format("%d-%02d-%02d", year, month + 1, day);
        holder.hikeDate.setText(formattedDate);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onBindViewHolder: ID: "+hikesList.get(position).hid);

                Intent intent = new Intent(context, HikeActivity.class);
                intent.putExtra("hid", hikesList.get(position).hid);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return hikesList.size();
    }
    public void setHikes(ArrayList<Hike> hikes, Context context){
        this.hikesList = hikes;
        this.context = context;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView hikeImageView;
        private TextView hikeName, hikeDate, hikeLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hikeImageView = itemView.findViewById(R.id.imgHike);
            hikeName = itemView.findViewById(R.id.hikeNameTextView);
            hikeDate = itemView.findViewById(R.id.hikeDateTextView);
            hikeLocation = itemView.findViewById(R.id.locationTextView);
        }
    }
}
