package com.mohan.covid19admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private Button btn;

    private EditText email;
    private EditText password;
    private Button login;
    private Button register;
    private ProgressBar progressBar;
//    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private DatabaseReference databaseReference;
    public static Activity La;
//    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private ProgressDialog dialog;

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getUid()!=null){
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email=(EditText)findViewById(R.id.login_email);
        password=(EditText)findViewById(R.id.login_password);
        login=(Button)findViewById(R.id.login_button);
        register=(Button)findViewById(R.id.signup_btn);
        dialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        //=FirebaseAuth.getInstance();
        La=this;
        progressBar=(ProgressBar)findViewById(R.id.progressBar_login);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(new Intent(MainActivity.this,statePicker.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                }
                else{
                    startActivity(new Intent(MainActivity.this,statePicker.class));
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_mail=email.getText().toString();
                String user_pass=password.getText().toString();
                if(!TextUtils.isEmpty(user_mail) && !TextUtils.isEmpty(user_pass)){
//                    progressBar.setVisibility(View.VISIBLE);
                    dialog.setTitle("LogIn");
                    dialog.setMessage("Please wait while we are verifying your credential...");
                    dialog.show();
                    mAuth.signInWithEmailAndPassword(user_mail,user_pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()){
                                        String token_id= FirebaseInstanceId.getInstance().getToken();
                                        final String current_user=mAuth.getCurrentUser().getUid();

                                        Map<String,Object> data=new HashMap<>();
                                        data.put("token_id",token_id);
                                        databaseReference.child(current_user).updateChildren(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                SharedPreferences sp = getSharedPreferences("User", Context.MODE_PRIVATE);
                                                //final String org = sp.getString("org","NULL");
//        final String userid = sp.getString("id",mAuth.getUid());


//            Toast.makeText(this, "Shared Preference is empty", Toast.LENGTH_SHORT).show();
                                                databaseReference.child(current_user).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        SharedPreferences sp2 = getSharedPreferences("User", Context.MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sp2.edit();
                                                        editor.putString("name", dataSnapshot.child("name").getValue().toString());
                                                        editor.putString("email", dataSnapshot.child("email").getValue().toString());
                                                        editor.putString("id", mAuth.getUid());
                                                        editor.putString("image", dataSnapshot.child("image").getValue().toString());
//                                                        editor.putString("state", dataSnapshot.child("state").getValue().toString());
//                                                        editor.putBoolean("superviser", Boolean.parseBoolean(dataSnapshot.child("superviser").getValue().toString()));
                                                        editor.apply();

                                                        progressBar.setVisibility(View.GONE);
                                                        dialog.dismiss();
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                                                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                                            finish();
                                                        }
                                                        else{
                                                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                                                            startActivity(intent);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                                            finish();
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });



                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        });
                                    }
                                    else{
                                        String error=task.getException().getMessage();
                                        dialog.dismiss();
                                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(MainActivity.this, "Email and password is required", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
