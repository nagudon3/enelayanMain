package com.unimas.enelayan2019.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unimas.enelayan2019.CartActivity;
import com.unimas.enelayan2019.Model.Cart;
import com.unimas.enelayan2019.Model.Product;
import com.unimas.enelayan2019.Product.ProductDetailsActivity;
import com.unimas.enelayan2019.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CardViewHolder> {
    List<Cart> cartList;
    Context mContext;


    public CartAdapter(List<Cart> cartList, Context mContext) {
        this.cartList = cartList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.cart_list_for_rv, parent, false);
        return new CartAdapter.CardViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.productName.setText(cartList.get(position).getProductName());
        double priceRounded = Double.parseDouble(cartList.get(position).getPrice());
        holder.orderPrice.setText("RM "+String.format("%.2f", priceRounded));
        holder.orderAmount.setText(cartList.get(position).getAmountOrdered()+" KG");

        cartList.get(position).getPickup();

        if (cartList.get(position).getCod() == true){
            holder.paymentMethod.setText("Cash on Delivery (COD)");
        }else if (cartList.get(position).getPickup() == true){
            holder.paymentMethod.setText("Self pick-up");
        }

        Glide.with(mContext).load(cartList.get(position).getProductImage()).into(holder.productImage);


    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder{
        TextView productName, orderPrice, orderAmount, paymentMethod;
        ImageView productImage, deleteButton;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            orderAmount = itemView.findViewById(R.id.amountOrdered);
            orderPrice = itemView.findViewById(R.id.price);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
    }
}
