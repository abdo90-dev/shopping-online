package com.example.myfirtapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.myfirtapp.ProductItem;
import com.example.myfirtapp.R;
import com.example.myfirtapp.model.Products;
import com.example.myfirtapp.viewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class MyAdapter extends FirebaseRecyclerAdapter<Products, ProductViewHolder> {
 private Context context;
 private ArrayList<Products> list_globale;
  private  DatabaseReference setQuery;
  private FirebaseRecyclerOptions<Products> options;

    public MyAdapter(@NonNull FirebaseRecyclerOptions<Products> options,Context context,DatabaseReference setQuery) {
        super(options);
        this.context = context;
        this.options = options;
        list_globale = new ArrayList<>();
        this.setQuery = setQuery;

    }

    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
        holder.product_name.setText(model.getName());
        Picasso.get().load(model.getImage()).into(holder.product_image);
        holder.product_description.setText(model.getDiscreption());
        holder.product_price.setText(model.getPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductItem.class);
                intent.putExtra("pId",model.getpId());
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;
    }


}
