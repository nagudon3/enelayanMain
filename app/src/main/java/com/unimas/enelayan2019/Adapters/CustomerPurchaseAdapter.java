package com.unimas.enelayan2019.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unimas.enelayan2019.CustomerPurchaseList;
import com.unimas.enelayan2019.Model.Purchase;
import com.unimas.enelayan2019.R;

import java.util.List;

public class CustomerPurchaseAdapter extends RecyclerView.Adapter<CustomerPurchaseAdapter.CustomerPurchaseViewHolder> {
    List<Purchase> purchaseList;
    Context mContext;

    public CustomerPurchaseAdapter(List<Purchase> purchaseList, Context mContext) {
        this.purchaseList = purchaseList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CustomerPurchaseAdapter.CustomerPurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.customer_purchase_list, parent, false);
        return new CustomerPurchaseAdapter.CustomerPurchaseViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerPurchaseViewHolder holder, int position) {
        holder.productName.setText(purchaseList.get(position).getProductName());
        double priceRounded = Double.parseDouble(purchaseList.get(position).getPurchasePrice());
        holder.orderPrice.setText("RM "+String.format("%.2f", priceRounded));
        holder.orderAmount.setText(purchaseList.get(position).getAmountPurchased()+" KG");
        holder.paymentMethod.setText(purchaseList.get(position).getPaymentMethod());
        Glide.with(mContext).load(purchaseList.get(position).getProductImage()).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return purchaseList.size();
    }

    public class CustomerPurchaseViewHolder extends RecyclerView.ViewHolder{
        TextView productName, orderPrice, orderAmount, paymentMethod;
        double totalPrice;
        ImageView productImage;

        public CustomerPurchaseViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            orderAmount = itemView.findViewById(R.id.amountOrdered);
            orderPrice = itemView.findViewById(R.id.price);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);
        }
    }
}
