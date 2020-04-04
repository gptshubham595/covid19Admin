package com.mohan.covid19admin;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SuspectReportAdapter extends RecyclerView.Adapter<SuspectReportAdapter.ViewHolder>
{

    private Context context;
    private ArrayList<Suspect> itemList;
    Callinterface2 callInterface2;

    public SuspectReportAdapter(Context context, ArrayList<Suspect> itemList, Callinterface2 callinterface2) {
        this.context = context;
        this.itemList = itemList;
        this.callInterface2 = callinterface2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.suspect_entry_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Suspect s = itemList.get(position);
        final String name = s.getName();
        final String contact = s.getContact();
        final String latitude = s.getLatitude();
        final String longitude = s.getLongitude();

        holder.nameTextView.setText(name);
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callInterface2.onCallClick(contact);
            }
        });
        holder.location.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                callInterface2.onLocationClick(longitude, latitude);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private Button call;
        private Button location;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            call = itemView.findViewById(R.id.call);
            location = itemView.findViewById(R.id.location);
        }
    }

    interface Callinterface2
    {

        void onCallClick(String contact);
        void onLocationClick(String longitude, String latitude);
    }
}
