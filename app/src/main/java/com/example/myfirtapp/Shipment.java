package com.example.myfirtapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myfirtapp.Prevalent.Prevalent;
import com.example.myfirtapp.model.Cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class Shipment extends AppCompatActivity {

    private EditText name,phoneNumber,adress;
    private Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment);

        name = findViewById(R.id.name_edit_text);
        phoneNumber = findViewById(R.id.phone_number_edit_text);
        adress = findViewById(R.id.adress_edit_text);
        send = findViewById(R.id.send_btn);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });

    }






    @TargetApi(Build.VERSION_CODES.O)
    private void check() {
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"),formatterD = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("shipment product");

        final String timeS = formatter.format(time);
        final String dateS = formatterD.format(date);
        final Integer[] totalePrice = {0};
        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("product cart").child("view user").child(Prevalent.curentOnlineuser.getUsername());
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot price : snapshot.getChildren()){
                   Cart cart = price.getValue(Cart.class);
                   totalePrice[0] += Integer.valueOf(cart.getPrice())*Integer.valueOf(cart.getQuantity());

               }
                HashMap<String , Object> dataHash = new HashMap<>();
                dataHash.put("name",name.getText().toString());
                dataHash.put("phone_number",phoneNumber.getText().toString());
                dataHash.put("adrass",adress.getText().toString());
                dataHash.put("Totale_price",totalePrice[0].toString());
                dataHash.put("image",Prevalent.curentOnlineuser.getImage());
                dataHash.put("Time" , timeS );
                dataHash.put("Date",dateS);
                dataHash.put("shipment","not shipped");
                databaseReference.child(Prevalent.curentOnlineuser.getUsername()).updateChildren(dataHash)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    databaseReference1.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(Shipment.this, "the product send successful", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Shipment.this,Home.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
