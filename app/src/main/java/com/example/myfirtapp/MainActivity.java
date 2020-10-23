package com.example.myfirtapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import com.example.myfirtapp.Prevalent.Prevalent;
import com.example.myfirtapp.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button login , sinup;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        sinup = findViewById(R.id.sinUp);
        Paper.init(this);
        final String username = Paper.book().read(Prevalent.userNameKey);
        final String passwordKey = Paper.book().read(Prevalent.userPasswordKey);
        progressDialog = new ProgressDialog(this);

        if (!(TextUtils.isEmpty(username)&&TextUtils.isEmpty(passwordKey))){
            progressDialog.setTitle("Already login");
            progressDialog.setMessage("Please wait ......");

            final DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
            rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(Prevalent.userJob).child(username).exists()){
                        progressDialog.show();
                        Users usersdata = snapshot.child(Prevalent.userJob).child(username).getValue(Users.class);
                        if (usersdata.getUsername().equals(username) && usersdata.getPassword().equals(passwordKey)){
                            Toast.makeText(MainActivity.this,"you are already login",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,Home.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }else {
                            progressDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            progressDialog.dismiss();
        }
         login.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(MainActivity.this,LoginActivity.class);

                startActivity(intent);
             }
         });
         sinup.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent =  new Intent(MainActivity.this,register.class);
                 startActivity(intent);
             }
         });
    }
}
