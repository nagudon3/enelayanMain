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
import com.unimas.enelayan2019.Model.Purchase;
import com.unimas.enelayan2019.R;

import java.util.List;

public class MyPurchaseAdapter extends RecyclerView.Adapter<MyPurchaseAdapter.PurchaseViewHolder> {

    List<Purchase> purchaseList;
    Context mContext;

    public MyPurchaseAdapter(List<Purchase> purchaseList, Context mContext) {
        this.purchaseList = purchaseList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.my_puchase_item_for_rv_list, parent, false);
        return new MyPurchaseAdapter.PurchaseViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return purchaseList.size();
    }


    public class PurchaseViewHolder extends RecyclerView.ViewHolder{
        TextView productName, orderPrice, orderAmount, paymentMethod;
        double totalPrice;
        ImageView productImage;

        public PurchaseViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            orderAmount = itemView.findViewById(R.id.amountOrdered);
            orderPrice = itemView.findViewById(R.id.price);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);
        }
    }
}
