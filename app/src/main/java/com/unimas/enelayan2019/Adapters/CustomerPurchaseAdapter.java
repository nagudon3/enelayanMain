package com.unimas.enelayan2019.Adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unimas.enelayan2019.CustomerPurchaseList;
import com.unimas.enelayan2019.Model.Purchase;
import com.unimas.enelayan2019.R;

import java.util.List;

public class CustomerPurchaseAdapter extends RecyclerView.Adapter<CustomerPurchaseAdapter.CustomerPurchaseViewHolder> {
    List<Purchase> purchaseList;
    Context mContext;
    View.OnClickListener callListener;
    View.OnClickListener wsListener;

    public CustomerPurchaseAdapter(List<Purchase> purchaseList, Context mContext, View.OnClickListener callListener, View.OnClickListener wsListener) {
        this.purchaseList = purchaseList;
        this.mContext = mContext;
        this.callListener = callListener;
        this.wsListener = wsListener;
    }

    @NonNull
    @Override
    public CustomerPurchaseAdapter.CustomerPurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.customer_purchase_list, parent, false);
        return new CustomerPurchaseAdapter.CustomerPurchaseViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerPurchaseViewHolder holder, final int position) {
        holder.productName.setText(purchaseList.get(position).getProductName());
        double priceRounded = Double.parseDouble(purchaseList.get(position).getPurchasePrice());
        holder.orderPrice.setText("RM "+String.format("%.2f", priceRounded));
        holder.orderAmount.setText(purchaseList.get(position).getAmountPurchased()+" KG");
        holder.paymentMethod.setText(purchaseList.get(position).getPaymentMethod());
        Glide.with(mContext).load(purchaseList.get(position).getProductImage()).into(holder.productImage);
//        holder.callButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
//                    ActivityCompat.requestPermissions(mContext, new String[] {Manifest.permission.CALL_PHONE}, 1);
//                }else {
//                    String custPhoneNumber = "tel:" + purchaseList.get(position).getCustomerPhone();
//                    Intent intent = new Intent(Intent.ACTION_CALL);
//                    intent.setData(Uri.parse(custPhoneNumber));
//                    mContext.startActivity(intent);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return purchaseList.size();
    }

    public class CustomerPurchaseViewHolder extends RecyclerView.ViewHolder{
        TextView productName, orderPrice, orderAmount, paymentMethod;
        double totalPrice;
        ImageView productImage, callButton, wsButton;

        public CustomerPurchaseViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            orderAmount = itemView.findViewById(R.id.amountOrdered);
            orderPrice = itemView.findViewById(R.id.price);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);
            callButton = itemView.findViewById(R.id.callButton);
            wsButton = itemView.findViewById(R.id.wsButton);

            callButton.setOnClickListener(callListener);
            wsButton.setOnClickListener(wsListener);
        }
    }
}
