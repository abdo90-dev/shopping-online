package com.example.myfirtapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirtapp.Prevalent.Prevalent;
import com.example.myfirtapp.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
        private EditText username , password;
        private static String job="users";
        private Button login;
        private ProgressDialog progressDialog;
        private CheckBox checkBox;
        private TextView admin,not_admin,forgot_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login_btn);
        checkBox = findViewById(R.id.remanber_me);
        forgot_password = findViewById(R.id.forget_password);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,Scurity_questions.class);
                intent.putExtra("from","login");
                startActivity(intent);
            }
        });
        progressDialog = new ProgressDialog(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        admin = findViewById(R.id.admin);
        not_admin = findViewById(R.id.not_admin);
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             not_admin.setVisibility(View.VISIBLE);
             admin.setVisibility(View.INVISIBLE);
             job = "users";
            }
        });
        not_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin.setVisibility(View.VISIBLE);
                not_admin.setVisibility(View.INVISIBLE);
                job = "admins";
            }
        });
    }

    private void loginUser()
    {
        String usernames = username.getText().toString();
        String passwords = password.getText().toString();
        if (usernames.isEmpty()||passwords.isEmpty()){
            Toast.makeText(LoginActivity.this,"please fiil the field",Toast.LENGTH_SHORT).show();
        }else{
           progressDialog.setTitle("checking informations");
           progressDialog.setMessage("please waiting for we are checking informations");
           progressDialog.setCanceledOnTouchOutside(false);
           progressDialog.show();
           AllowAccessToAcount(usernames,passwords);
        }

    }

    private void AllowAccessToAcount(final String usernames, final String passwords) {
        if (checkBox.isChecked()){
            Paper.book().write(Prevalent.userNameKey,usernames);
            Paper.book().write(Prevalent.userPasswordKey,passwords);
            Paper.book().write(Prevalent.userJob,job);
        }
        final DatabaseReference rootRif = FirebaseDatabase.getInstance().getReference();
        rootRif.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             if (snapshot.child(job).child(usernames).exists()){
                 Users usersdata = snapshot.child(job).child(usernames).getValue(Users.class);
                 if (usersdata.getUsername().equals(usernames) && usersdata.getPassword().equals(passwords)){
                     if (job.equals("users")){

                         Intent intent = new Intent(LoginActivity.this,Home.class);
                         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         intent.putExtra("admin","users");
                         startActivity(intent);
                         progressDialog.dismiss();
                         Prevalent.curentOnlineuser = usersdata;

                     }else{
                         Intent intent = new Intent(LoginActivity.this,category.class);
                         intent.putExtra("admin","admin");
                         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         startActivity(intent);
                         progressDialog.dismiss();
                         Prevalent.curentOnlineuser = usersdata;
                     }

                 }else{
                     Toast.makeText(LoginActivity.this,"does not exist",Toast.LENGTH_SHORT).show();
                     progressDialog.dismiss();
                 }

             }else{
                 Toast.makeText(LoginActivity.this,"this user does not exist",Toast.LENGTH_SHORT).show();
                 progressDialog.dismiss();
             }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
