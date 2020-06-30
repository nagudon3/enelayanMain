package com.unimas.enelayan2019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimas.enelayan2019.Adapters.MyPurchaseAdapter;
import com.unimas.enelayan2019.Model.Purchase;

import java.util.ArrayList;

public class CustomerPurchaseList extends AppCompatActivity {
    private RecyclerView customerPurchaseRV;
    private ArrayList<Purchase> purchaseArrayList;
    private FirebaseDatabase firebaseDatabase;
    private TextView noItem;
    private MyPurchaseAdapter myPurchaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_purchase_list);

        customerPurchaseRV = findViewById(R.id.custPurchaseRV);
        noItem = findViewById(R.id.noItem);

        noItem.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        customerPurchaseRV.setLayoutManager(linearLayoutManager);

        purchaseArrayList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();

        final DatabaseReference purchaseRef= FirebaseDatabase.getInstance().getReference().child("Purchase");
        purchaseRef.orderByChild("sellerId").equalTo(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        Purchase purchase = dataSnapshot1.getValue(Purchase.class);
                        purchaseArrayList.add(purchase);

                        myPurchaseAdapter = new MyPurchaseAdapter(purchaseArrayList, CustomerPurchaseList.this);
                        customerPurchaseRV.setAdapter(myPurchaseAdapter);
                    }

                }else {
                    noItem.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}