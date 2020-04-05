package com.mohan.covid19admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FindSuspect extends Fragment {


    private RecyclerView recyclerView;
    private find_suspect_adapter adapter;
    private FloatingActionButton fb;
    private ArrayList<find_suspect_class> arrayList;
    private final String DEFAULT_STRING = "NULL";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    public FindSuspect() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_find_suspect, container, false);
        fb = v.findViewById(R.id.floatingActionButton);
        recyclerView = v.findViewById(R.id.findSuspectRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        arrayList = new ArrayList<>();
        arrayList.add(new find_suspect_class("Mohan Kumar","a","Bihar supervisor","xyz","A person had found corona positive in munger. As per his travel history he has travelled to " +
                "gaya on 17th  near gaay ghat at 4P.M., patna on 18 near railway station at 6P.M., any body who were there at same time kindly close yourself in a quarentine, and reposrt us here.",5));
        arrayList.add(new find_suspect_class("Mohan Kumar","a","Bihar supervisor","xyz","A person had found corona positive in munger. As per his travel history he has travelled to " +
                "gaya on 17th  near gaay ghat at 4P.M., patna on 18 near railway station at 6P.M., any body who were there at same time kindly close yourself in a quarentine, and reposrt us here.",5));
        arrayList.add(new find_suspect_class("Mohan Kumar","a","Bihar supervisor","xyz","A person had found corona positive in munger. As per his travel history he has travelled to " +
                "gaya on 17th  near gaay ghat at 4P.M., patna on 18 near railway station at 6P.M., any body who were there at same time kindly close yourself in a quarentine, and reposrt us here.",5));

        adapter = new find_suspect_adapter(getContext(),arrayList);
        recyclerView.setAdapter(adapter);

        SharedPreferences sp2 = getContext().getSharedPreferences("User",Context.MODE_PRIVATE);
        String state = sp2.getString("state",DEFAULT_STRING);
        databaseReference.child("query").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    arrayList.add(new find_suspect_class(snapshot.child("name").getValue().toString(),
                            snapshot.child("image").getValue().toString(),
                            snapshot.child("post").getValue().toString(),
                            snapshot.getKey().toString(),
                            snapshot.child("text").getValue().toString(),
                            Integer.parseInt(snapshot.child("reported").getValue().toString())));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_view,null);
                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setView(dialogView);
                final EditText et = dialogView.findViewById(R.id.dialog_text);
                dialogView.findViewById(R.id.dialog_post).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String str = et.getText().toString();
                        if(str.isEmpty()){
                            et.setError("post data should not be empty");
                            et.requestFocus();
                        }else{
                            alertDialog.dismiss();
                            uploadData(str);
                        }
//                        signup(email,name,members,image,id,true);


                    }
                });

                dialogView.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        return v;
    }

    void uploadData(String text){
//        Toast.makeText(getContext(), "sending", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User",0);
        String state = sharedPreferences.getString("state",DEFAULT_STRING);
        String name = sharedPreferences.getString("name",DEFAULT_STRING);
        String image = sharedPreferences.getString("image",DEFAULT_STRING);
        String id = sharedPreferences.getString("id",mAuth.getUid());
        DatabaseReference postref = databaseReference.child("query").push();
        String link = postref.getKey().toString();
        Map<String,Object> data = new HashMap();
        data.put("name",name);
        data.put("id",id);
        data.put("image",image);
        data.put("text",text);
        data.put("post","supervisor");
        data.put("reported",0);
        databaseReference.child("query").child(link).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Toast.makeText(getContext(), "Successfull", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
