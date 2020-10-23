package com.example.myfirtapp.viewHolder;

import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirtapp.R;
import com.example.myfirtapp.interfaces.ItemOnclickLisner;


public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
    public TextView product_name,product_description,product_price;
    public ImageView product_image;
    private ItemOnclickLisner lisner;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        product_image = itemView.findViewById(R.id.product_image);
        product_name = itemView.findViewById(R.id.product_name_item);
        product_description = itemView.findViewById(R.id.product_description);
        product_price = itemView.findViewById(R.id.product_price);

    }
    public void setItemClickLisener(ItemOnclickLisner lisner){
        this.lisner = lisner;
    }

    @Override
    public void onClick(View v) {
        lisner.onKlick(v,getAdapterPosition(),false);
    }


}
