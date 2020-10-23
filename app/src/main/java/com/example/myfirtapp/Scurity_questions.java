package com.example.myfirtapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Scurity_questions extends AppCompatActivity {
    private TextView requirement;
    private EditText first_question,second_question,username;
    private Button sent;
    private String s;
    private DatabaseReference databaseReference;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scurity_questions);
        requirement = findViewById(R.id.required);
        first_question = findViewById(R.id.first_question);
        second_question = findViewById(R.id.second_question);
        sent = findViewById(R.id.send_and_regestretion);
        username = findViewById(R.id.check_username);
        
        s = getIntent().getStringExtra("from");
        requirement.setVisibility(View.GONE);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("questions");
        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (TextUtils.isEmpty(first_question.getText().toString()) || TextUtils.isEmpty(second_question.getText().toString())){
                    Toast.makeText(Scurity_questions.this, "please full the fields ", Toast.LENGTH_SHORT).show();
                }else{
                    
                    
                    HashMap<String , Object> data = new HashMap<>();
                    data.put("first question",first_question.getText().toString());
                    data.put("second question",second_question.getText().toString());
                    if (s.equals("registering")){
                        databaseReference.updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                Intent intent = new Intent(Scurity_questions.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(Scurity_questions.this, "your acount created successfully", Toast.LENGTH_SHORT).show();
                            }}
                        });

                    }else if (s.equals("login")){
                        String username1 = username.getText().toString();
                        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("users").child(username1);
                        databaseReference2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    final AlertDialog.Builder alert = new AlertDialog.Builder(Scurity_questions.this);
                                    alert.setTitle("reset password");
                                    alert.setMessage("please enter your new password");
                                    final EditText editText = new EditText(Scurity_questions.this) ;
                                    editText.setPadding(3,3,3,3);
                                    alert.setView(editText);
                                    alert.setCancelable(true);
                                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            HashMap<String , Object> data = new HashMap<>();
                                            if (!TextUtils.isEmpty(editText.getText().toString())){
                                            data.put("password",editText.getText().toString());
                                            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("users").child(editText.getText().toString()).child("password");
                                            databaseReference1.updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Intent intent = new Intent(Scurity_questions.this, LoginActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                        }else{
                                                Toast.makeText(Scurity_questions.this, "please fill the field", Toast.LENGTH_SHORT).show();
                                            }}
                                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                }else{
                                    Toast.makeText(Scurity_questions.this, "this user name does not exist", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }else{
                        databaseReference.updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(Scurity_questions.this,Home.class);
                                startActivity(intent);
                            }
                        });

                    }

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!s.equals("setting")){
            requirement.setVisibility(View.GONE);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String first_questions = snapshot.child("first question").getValue().toString();
                        String second_questions = snapshot.child("second question").getValue().toString();
                       first_question.setText(first_questions);
                       second_question.setText(second_questions);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if(s.equals("registering")){
            requirement.setVisibility(View.VISIBLE);
            
        }else{
            requirement.setVisibility(View.VISIBLE);
            
        }
    }
}
