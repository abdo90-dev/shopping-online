package com.example.myfirtapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myfirtapp.model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProductsOrderDetails extends AppCompatActivity {
    private ImageView imageProduct;
    private EditText description,price,name;
    private Button mantain,delete;
    private String pid;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_order_details);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("products").child(pid);
        imageProduct = findViewById(R.id.product_image_details);
        description = findViewById(R.id.product_description_details);
        price = findViewById(R.id.product_price_details);
        mantain = findViewById(R.id.product_details_btn);
        pid = getIntent().getStringExtra("pId");
        name = findViewById(R.id.product_name_details);
        delete = findViewById(R.id.delete_product_btn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ProductsOrderDetails.this);
                alert.setTitle("Delete");
                alert.setMessage("do you want to delete this product");
                alert.setCancelable(true);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    Toast.makeText(ProductsOrderDetails.this, "the product deleted successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ProductsOrderDetails.this,Home.class);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();


            }
        });
        mantain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String , Object> data = new HashMap<>();
                data.put("name",name.getText().toString());
                data.put("description",description.getText().toString());
                data.put("price",price.getText().toString());
                data.put("pId",pid);
              databaseReference.updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      if (task.isSuccessful()){
                          Toast.makeText(ProductsOrderDetails.this, "the Data updated successfully", Toast.LENGTH_SHORT).show();
                          Intent intent = new Intent(ProductsOrderDetails.this,category.class);
                          startActivity(intent);
                      }
                  }
              });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Products product = snapshot.getValue(Products.class);
                    description.setText(product.getDiscreption());
                    price.setText(product.getPrice());
                    name.setText(product.getName());
                    Picasso.get().load(product.getImage()).into(imageProduct);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
