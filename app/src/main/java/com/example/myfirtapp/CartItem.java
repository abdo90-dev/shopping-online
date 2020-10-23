package com.example.myfirtapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirtapp.Prevalent.Prevalent;
import com.example.myfirtapp.model.Cart;
import com.example.myfirtapp.model.Products;
import com.example.myfirtapp.viewHolder.MyAdaptor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CartItem extends AppCompatActivity {
      private RecyclerView recyclerView;
      private RecyclerView.LayoutManager layoutManager;
      private Button nextButton;
      private TextView totalePrice,shipment;
      private int count_price = 0;
      private String pid ;
      private FirebaseRecyclerOptions<Cart> options;
      private DatabaseReference cartDataref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_item);
        recyclerView = findViewById(R.id.cart_list);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        nextButton = findViewById(R.id.next_btn);
        totalePrice = findViewById(R.id.totalePrice);
        shipment = findViewById(R.id.shipment);
        pid = getIntent().getStringExtra("PID");
        cartDataref = FirebaseDatabase.getInstance().getReference().child("product cart");
        if (!(pid == null)){
            options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartDataref.
                    child("view admins").child(pid),Cart.class).build();
        }else{
            options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartDataref.
                    child("view user").child(Prevalent.curentOnlineuser.getUsername()),Cart.class).build();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        check_shiped();




        FirebaseRecyclerAdapter<Cart, MyAdaptor> adapter = new FirebaseRecyclerAdapter<Cart, MyAdaptor>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MyAdaptor holder, int position, @NonNull final Cart model) {
               holder.name.setText("Name= "+model.getName());
               holder.quantity.setText("Quantity= "+model.getQuantity());
               holder.price.setText("price= "+model.getPrice()+" DA");
               count_price += Integer.valueOf(model.getPrice())*Integer.valueOf(model.getQuantity());
               totalePrice.setText("Totale price = "+count_price+" DA");

               holder.edit.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent intent = new Intent(CartItem.this,ProductItem.class);
                       intent.putExtra("pId",model.getProductId());
                       intent.putExtra("forEdit","edit");
                       startActivity(intent);
                   }
               });
               holder.delete.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       final AlertDialog.Builder alert = new AlertDialog.Builder(CartItem.this);
                       alert.setTitle("Delete");
                       alert.setMessage("Really do you want to delete this product from cart list");
                       alert.setCancelable(true);
                       alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               cartDataref.child("view user").child(Prevalent.curentOnlineuser.getUsername()).child(model.getProductId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       Toast.makeText(CartItem.this, "The product deleted successful", Toast.LENGTH_SHORT).show();
                                   }
                               });
                           }
                       });
                       alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.cancel();
                           }
                       });
                       alert.show();
                   }

               });


               DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("products")
                       .child(model.getProductId());
               databaseReference.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if (snapshot.exists()) {
                           Products cart = snapshot.getValue(Products.class);
                           Picasso.get().load(cart.getImage()).into(holder.image_product);
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
            }


            @NonNull
            @Override
            public MyAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);

                return new MyAdaptor(view);
            }
        };
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartItem.this,Shipment.class);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.startListening();
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
                       totalePrice.setText("Dear "+user+"your orders are shiped successfully");
                       recyclerView.setVisibility(View.GONE);
                       nextButton.setVisibility(View.GONE);
                       shipment.setText("congratilations your final order has shipped. soon it will receive");
                       shipment.setVisibility(View.VISIBLE);

                    }else if(shiped.equals("not shipped")){
                        totalePrice.setText("shiping status = not shiped");
                        recyclerView.setVisibility(View.GONE);
                        nextButton.setVisibility(View.GONE);
                        shipment.setText("your order has placed successfully. soon it will be verified");
                        shipment.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
