package com.example.myfirtapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static android.widget.Toast.LENGTH_SHORT;

public class register extends AppCompatActivity {
     private Button button;
     private TextView username,password,email;
     private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        button = findViewById(R.id.creat_acount);
        username = findViewById(R.id.username_reg);
        password = findViewById(R.id.password_reg);
        email =  findViewById(R.id.email_reg);
        progressDialog = new ProgressDialog(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creat_acount();
            }
        });
    }

    private void creat_acount() 
    {
        String usernames = username.getText().toString();
        String passwords = password.getText().toString();
        String emails = email.getText().toString();
        if (usernames.isEmpty()||passwords.isEmpty()||emails.isEmpty()){
            Toast.makeText(this,"Please fill the fields", LENGTH_SHORT).show();
        }else {

            progressDialog.setTitle("creat acount");
            progressDialog.setMessage("please wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            checkValidateemail(usernames,passwords,emails);
        }
    }

    private void checkValidateemail(final String usernames, final String passwords, final String emails) {
    final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if (!(snapshot.child("users").child(usernames).exists())){
                HashMap<String ,Object> userdata = new HashMap<>();
                userdata.put("username",usernames);
                userdata.put("email",emails);
                userdata.put("password",passwords);
                rootRef.child("users").child(usernames).updateChildren(userdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(register.this,"congratilations your acount created successful", LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent intent = new Intent(register.this,Scurity_questions.class);
                            intent.putExtra("from","registering");
                            startActivity(intent);
                        }else{

                            Toast.makeText(register.this,"network Error: please try again later",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else{
                Toast.makeText(register.this,"this email already exist", LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

    }
}
