package com.example.myfirtapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.myfirtapp.Prevalent.Prevalent;
import com.example.myfirtapp.model.Cart;
import com.example.myfirtapp.model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class ProductItem extends AppCompatActivity {
    private Button add_to_cart;
    private ImageView productImage;
    private ElegantNumberButton numberProduct;
    private TextView name,description,price;
    private String pid="",edit="",shipping="",intents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_item);
        productImage = findViewById(R.id.single_Item);
        numberProduct = findViewById(R.id.product_number);
        name = findViewById(R.id.name_single_Item_product);
        description = findViewById(R.id.description_single_Item_product);
        price = findViewById(R.id.price_single_Item_product);
        add_to_cart = findViewById(R.id.add_to_cart);
        intents = getIntent().getStringExtra("admin");
        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shipping=="shipped"||shipping=="not shipped"){

                    Toast.makeText(ProductItem.this, "you can order other product only when you receive your previous order", Toast.LENGTH_LONG).show();
                }else{
                add_product_to_cart();}
            }
        });
        pid = getIntent().getStringExtra("pId");
        if (getIntent().getStringExtra("forEdit")!=null)
        edit  = getIntent().getStringExtra("forEdit");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("products");
        databaseReference.child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Products product = snapshot.getValue(Products.class);
                    Picasso.get().load(product.getImage()).into(productImage);
                    name.setText(product.getName());
                    description.setText(product.getDiscreption());
                    price.setText(product.getPrice());
                    if (edit.equals("edit")){
                        add_to_cart.setText("Edit");
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("product cart").child("view user").child(Prevalent.curentOnlineuser.getUsername()).child(pid);
                        databaseReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    Cart cart = snapshot.getValue(Cart.class);
                                    numberProduct.setNumber(cart.getQuantity());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void add_product_to_cart() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formattertime = DateTimeFormatter.ofPattern("HH:mm:ss");
        String sDate = formatter.format(date);
        String sTime = formattertime.format(time);
        final HashMap<String,Object> dataMap = new HashMap<>();
        dataMap.put("productId",pid);
        dataMap.put("name",name.getText().toString());
        dataMap.put("price",price.getText().toString());
        dataMap.put("quantity",numberProduct.getNumber());
        dataMap.put("date",sDate);
        dataMap.put("time",sTime);

       final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("product cart");
        databaseReference.child("view user").child(Prevalent.curentOnlineuser.getUsername()).child(pid)
                .updateChildren(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    databaseReference.child("view admins").child(Prevalent.curentOnlineuser.getUsername()).child(pid)
                            .updateChildren(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ProductItem.this, "Product add to cart successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ProductItem.this,Home.class);
                                intent.putExtra("admin",intents);
                                startActivity(intent);
                            }
                        }
                    }) ;
                }
            }
        });

    }
    private void check_shiped()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("shipment product").child(Prevalent.curentOnlineuser.getUsername());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String shiped = snapshot.child("shipment").getValue().toString();
                    String user = snapshot.child("name").getValue().toString();
                    if (shiped.equals("shipped")){
                  shipping = shiped;

                    }else if(shiped.equals("not shipped")){
                         shipping = shiped;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
