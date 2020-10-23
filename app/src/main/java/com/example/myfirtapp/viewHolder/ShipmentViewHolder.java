package com.example.myfirtapp.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirtapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShipmentViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView imageView;
    public TextView name,adrass,phone_number,totale_price;
    public Button products_order;
    public ShipmentViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.order_image_profile);
        name = itemView.findViewById(R.id.order_name);
        phone_number = itemView.findViewById(R.id.order_phone_number);
        adrass = itemView.findViewById(R.id.order_adrass);
        totale_price = itemView.findViewById(R.id.order_totale_price);
        products_order = itemView.findViewById(R.id.order_product);
    }
}
