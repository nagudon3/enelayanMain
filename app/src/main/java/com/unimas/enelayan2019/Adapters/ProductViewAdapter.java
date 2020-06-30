package com.unimas.enelayan2019.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unimas.enelayan2019.HomeActivity;
import com.unimas.enelayan2019.Model.Product;
import com.unimas.enelayan2019.Model.Seller;
import com.unimas.enelayan2019.PostDetailsActivity;
import com.unimas.enelayan2019.Product.ProductDetailsActivity;
import com.unimas.enelayan2019.R;

import java.util.ArrayList;
import java.util.List;

public class ProductViewAdapter extends RecyclerView.Adapter<ProductViewAdapter.MyViewHolder> {

    Context mContext;
    List<Product> productList;

    public ProductViewAdapter(Context mContext, List<Product> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.product_grid_view, parent, false);
        return new ProductViewAdapter.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.productName.setText(productList.get(position).getProductName());
        double pricePerKg = Double.parseDouble(productList.get(position).getProductPricePerKg());
        holder.productPrice.setText("RM "+String.format("%.2f", pricePerKg)+" /KG");
        Glide.with(mContext).load(productList.get(position).getProductImage()).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView productName, productPrice;
        ImageView productImage;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productImage = itemView.findViewById(R.id.productImage);
            productPrice = itemView.findViewById(R.id.productPricePerKg);
            cardView = itemView.findViewById(R.id.productCV);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToDetails = new Intent(mContext, ProductDetailsActivity.class);
                    int position = getAdapterPosition();

                    goToDetails.putExtra("sellerName", productList.get(position).getSellerName());
                    goToDetails.putExtra("sellerId", productList.get(position).getSellerId());
                    goToDetails.putExtra("sellerImage", productList.get(position).getSellerImage());
                    goToDetails.putExtra("sellingArea", productList.get(position).getSellingArea());
                    goToDetails.putExtra("sellerPhone", productList.get(position).getSellerPhone());
                    goToDetails.putExtra("productImage", productList.get(position).getProductImage());
                    goToDetails.putExtra("productName", productList.get(position).getProductName());
                    goToDetails.putExtra("productPrice", productList.get(position).getProductPricePerKg());
                    goToDetails.putExtra("productAmount", productList.get(position).getAmountAvailable());
                    goToDetails.putExtra("productId", productList.get(position).getProductId());
                    goToDetails.putExtra("isCod", productList.get(position).getCod());
                    goToDetails.putExtra("isSelfPickup", productList.get(position).getPickup());
                    goToDetails.putExtra("productCategory", productList.get(position).getProductCategory());
                    mContext.startActivity(goToDetails);
                }
            });
        }
    }
}
