package com.example.myfirtapp.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirtapp.R;
import com.example.myfirtapp.interfaces.ItemOnclickLisner;

public class MyAdaptor extends RecyclerView.ViewHolder implements ItemOnclickLisner {
    public TextView name,price,quantity;
    public ImageView image_product;
    public ItemOnclickLisner itemOnclickLisner;
    public Button edit,delete;

    public MyAdaptor(@NonNull View itemView) {

        super(itemView);
        name = itemView.findViewById(R.id.cart_name_product);
        price = itemView.findViewById(R.id.cart_product_price);
        quantity = itemView.findViewById(R.id.cart_product_quantity);
        image_product = itemView.findViewById(R.id.cart_image_product);
         edit = itemView.findViewById(R.id.edit_btn);
         delete = itemView.findViewById(R.id.delete_btn);
    }

    @Override
    public void onKlick(View view, int position, boolean longClick) {
          itemOnclickLisner.onKlick(view,getAdapterPosition(),false);
    }

    public void setItemOnclickLisner(ItemOnclickLisner itemOnclickLisner) {
        this.itemOnclickLisner = itemOnclickLisner;
    }
}
