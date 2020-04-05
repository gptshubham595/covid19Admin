package com.mohan.covid19admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class nenstedAdapter extends RecyclerView.Adapter<nenstedAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> arrayList;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public nenstedAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reported_single,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        String str = arrayList.get(position);
        databaseReference.child("User").child(str).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.name.setText(dataSnapshot.child("name").getValue().toString());
                final String image = dataSnapshot.child("thumb_image").getValue().toString();
                Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.profile_image).into(holder.img, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(image).placeholder(R.drawable.profile_image).into(holder.img);
                    }
                });
                holder.address.setText(dataSnapshot.child("state").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "call clicked", Toast.LENGTH_SHORT).show();
            }
        });
        holder.loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "loc clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView name,address;
        Button loc,call;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.reporter_image);
            name = itemView.findViewById(R.id.reporter_name);
            address = itemView.findViewById(R.id.reporter_address);
            loc = itemView.findViewById(R.id.reporter_loc);
            call = itemView.findViewById(R.id.reporter_call);
        }
    }
}
