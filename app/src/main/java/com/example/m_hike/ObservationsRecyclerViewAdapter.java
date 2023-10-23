package com.example.m_hike;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_hike.objects.Observation;

import java.util.ArrayList;

public class ObservationsRecyclerViewAdapter extends RecyclerView.Adapter<ObservationsRecyclerViewAdapter.ViewHolder>{
    public ArrayList<Observation> observations= new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.observation_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtObservation.setText(observations.get(position).text);
        holder.txtUserName.setText(observations.get(position).name);
        holder.txtDate.setText(observations.get(position).date);
    }

    @Override
    public int getItemCount() {
        return observations.size();
    }
    public void setObservations(ArrayList<Observation> observations) {
        this.observations = observations;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtUserName,txtObservation, txtDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName=itemView.findViewById(R.id.txtUserName);
            txtDate=itemView.findViewById(R.id.txtDate);
            txtObservation=itemView.findViewById(R.id.txtObservation);
        }
    }
}
