package com.example.myfirtapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirtapp.R;
import com.example.myfirtapp.Shipment;
import com.example.myfirtapp.model.ShipmentOrder;
import com.example.myfirtapp.viewHolder.ShipmentViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.zip.Inflater;

public class ProductOrders extends AppCompatActivity {
     private TextView totale_price;
     private RecyclerView recyclerView;
     private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_orders);
        totale_price = findViewById(R.id.totalePrice1);
        recyclerView = findViewById(R.id.recyclerView_order);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("shipment product");

        FirebaseRecyclerOptions<ShipmentOrder> options = new FirebaseRecyclerOptions.Builder<ShipmentOrder>().setQuery(databaseReference,ShipmentOrder.class).build();
        FirebaseRecyclerAdapter<ShipmentOrder, ShipmentViewHolder> adapter = new FirebaseRecyclerAdapter<ShipmentOrder, ShipmentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ShipmentViewHolder holder, final int position, @NonNull final ShipmentOrder model) {
                holder.name.setText("Name: "+model.getName());
                holder.adrass.setText("adress: "+model.getAdrass());
                holder.phone_number.setText("Mobile number: "+model.getPhone_number());
                holder.totale_price.setText("Totale price: "+model.getTotale_price()+" DA");
                Picasso.get().load(model.getImage()).into(holder.imageView);
                holder.products_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pos = getRef(position).getKey();
                        Intent intent = new Intent(ProductOrders.this,CartItem.class);
                        intent.putExtra("PID",pos);
                        startActivity(intent);

                    }
                });
            }

            @NonNull
            @Override
            public ShipmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders,parent,false);

                return new ShipmentViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
