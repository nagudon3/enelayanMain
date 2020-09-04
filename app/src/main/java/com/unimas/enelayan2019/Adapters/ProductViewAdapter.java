package com.unimas.enelayan2019.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

public class ProductViewAdapter extends RecyclerView.Adapter<ProductViewAdapter.MyViewHolder> implements Filterable {

    Context mContext;
    List<Product> productList;
    List<Product> productFiltered;

    public ProductViewAdapter(Context mContext, List<Product> productList) {
        this.mContext = mContext;
        this.productList = productList;
        this.productFiltered = productList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.product_grid_view, parent, false);
        return new ProductViewAdapter.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.productName.setText(productFiltered.get(position).getProductName());
        double pricePerKg = Double.parseDouble(productFiltered.get(position).getProductPricePerKg());
        holder.productPrice.setText("RM "+String.format("%.2f", pricePerKg)+" /KG");
        Glide.with(mContext).load(productFiltered.get(position).getProductImage()).into(holder.productImage);

        String fCategory = productFiltered.get(position).getProductCategory();
        if (fCategory.equals("Wholesale")){
            holder.wholesale.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return productFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key = charSequence.toString();

                if (key.isEmpty()){
                    productFiltered = productList;
                }else {
                    List<Product> IsFilter = new ArrayList<>();
                    for (Product row: productList){
                        if (row.getProductName().toLowerCase().contains(key.toLowerCase())){
                            IsFilter.add(row);
                        }
                    }
                    productFiltered = IsFilter;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productFiltered = (List<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView productName, productPrice, wholesale;
        ImageView productImage;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productImage = itemView.findViewById(R.id.productImage);
            productPrice = itemView.findViewById(R.id.productPricePerKg);
            cardView = itemView.findViewById(R.id.productCV);
            wholesale = itemView.findViewById(R.id.wholesale);

            wholesale.setVisibility(View.GONE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToDetails = new Intent(mContext, ProductDetailsActivity.class);
                    int position = getAdapterPosition();

                    goToDetails.putExtra("sellerName", productFiltered.get(position).getSellerName());
                    goToDetails.putExtra("sellerId", productFiltered.get(position).getSellerId());
                    goToDetails.putExtra("sellerImage", productFiltered.get(position).getSellerImage());
                    goToDetails.putExtra("sellingArea", productFiltered.get(position).getSellingArea());
                    goToDetails.putExtra("sellerPhone", productFiltered.get(position).getSellerPhone());
                    goToDetails.putExtra("productImage", productFiltered.get(position).getProductImage());
                    goToDetails.putExtra("productName", productFiltered.get(position).getProductName());
                    goToDetails.putExtra("productPrice", productFiltered.get(position).getProductPricePerKg());
                    goToDetails.putExtra("productAmount", productFiltered.get(position).getAmountAvailable());
                    goToDetails.putExtra("productId", productFiltered.get(position).getProductId());
                    goToDetails.putExtra("isCod", productFiltered.get(position).getCod());
                    goToDetails.putExtra("isSelfPickup", productFiltered.get(position).getPickup());
                    goToDetails.putExtra("productCategory", productFiltered.get(position).getProductCategory());
                    mContext.startActivity(goToDetails);
                }
            });
        }
    }
}
