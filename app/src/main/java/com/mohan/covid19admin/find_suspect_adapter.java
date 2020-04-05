package com.mohan.covid19admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class find_suspect_adapter extends RecyclerView.Adapter<find_suspect_adapter.ViewHolder> {
    private Context context;
    private ArrayList<find_suspect_class> itemList;

    public find_suspect_adapter(Context context, ArrayList<find_suspect_class> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public find_suspect_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.find_suspect_single,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final find_suspect_adapter.ViewHolder holder, final int position) {

        find_suspect_class ne = itemList.get(position);
//        Picasso
        final String image = ne.getImage();
        Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.profile_image).into(holder.img, new Callback() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError(Exception e) {
                Picasso.get().load(image).placeholder(R.drawable.profile_image).into(holder.img);
            }
        });
        holder.name.setText(ne.getName());
        holder.post.setText(ne.getPost());
        holder.text.setText(ne.getText());
        holder.reported.setText("2 people have reposted");
        holder.reported.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean b = itemList.get(position).getOpened();
                if(!b){
                    itemList.get(position).setOpened(true);
                    nenstedAdapter adapter = new nenstedAdapter(context, itemList.get(position).getArrayList());
                    holder.rv.setHasFixedSize(true);
                    holder.rv.setLayoutManager(new LinearLayoutManager(context));
                    holder.rv.setAdapter(adapter);
                    holder.rv.setVisibility(View.VISIBLE);
                }else{
                    itemList.get(position).setOpened(false);
                    holder.rv.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView img;
        private TextView name,post,text,reported;
        private RecyclerView rv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.post_image);
            name = itemView.findViewById(R.id.post_name);
            post = itemView.findViewById(R.id.post_post);
            text = itemView.findViewById(R.id.post_test);
            reported = itemView.findViewById(R.id.post_n_reported);
            rv = itemView.findViewById(R.id.reportersRecycler);
        }
    }
}
