package com.example.myfirtapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class category extends AppCompatActivity {
    private ImageView sportTshert,tshert,dresses,coat;
    private ImageView glasses,bags,hats,shoes;
    private ImageView headPhone,laptop,watch,mobilePhone;
   private Button logout,order_product,maitain_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        sportTshert = findViewById(R.id.tshert_sport);
        tshert = findViewById(R.id.tshert);
        dresses = findViewById(R.id.fimmele_dresses);
        coat = findViewById(R.id.sweathers);
        logout = findViewById(R.id.logout);
        order_product = findViewById(R.id.order_product);
        maitain_btn = findViewById(R.id.mantaine_product);
        glasses = findViewById(R.id.glasses);
        bags = findViewById(R.id.bags);
        hats = findViewById(R.id.hats);
        shoes = findViewById(R.id.shoes);

        headPhone = findViewById(R.id.headPhone);
        laptop = findViewById(R.id.laptop);
        watch = findViewById(R.id.wach);
        mobilePhone = findViewById(R.id.mobile_phone);
        maitain_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(category.this,Home.class);
                intent.putExtra("admin","admin");
                startActivity(intent);
            }
        });
         logout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(category.this,MainActivity.class);
                 startActivity(intent);
             }
         });
         order_product.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(category.this,ProductOrders.class);
                 startActivity(intent);
             }
         });
        sportTshert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(category.this,Admins.class);
                intent.putExtra("category","Sport t shert");
                startActivity(intent);
            }
        });
        tshert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(category.this,Admins.class);
                intent.putExtra("category","T shert");
                startActivity(intent);
            }
        });
        dresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(category.this,Admins.class);
                intent.putExtra("category","Dresses");
                startActivity(intent);
            }
        });
        coat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(category.this,Admins.class);
                intent.putExtra("category","Coat");
                startActivity(intent);
            }
        });
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(category.this,Admins.class);
                intent.putExtra("category","Glasses");
                startActivity(intent);
            }
        });
        bags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(category.this,Admins.class);
                intent.putExtra("category","Bags");
                startActivity(intent);
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(category.this,Admins.class);
                intent.putExtra("category","Shoes");
                startActivity(intent);
            }
        });
        headPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(category.this,Admins.class);
                intent.putExtra("category","Head phone");
                startActivity(intent);
            }
        });
        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(category.this,Admins.class);
                intent.putExtra("category","Laptop");
                startActivity(intent);
            }
        });
        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(category.this,Admins.class);
                intent.putExtra("category","Watches");
                startActivity(intent);
            }
        });
        mobilePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(category.this,Admins.class);
                intent.putExtra("category","Mobile phone");
                startActivity(intent);
            }
        });
    }
}
