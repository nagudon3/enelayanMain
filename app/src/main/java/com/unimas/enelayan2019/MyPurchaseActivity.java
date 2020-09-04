package com.unimas.enelayan2019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimas.enelayan2019.Adapters.CartAdapter;
import com.unimas.enelayan2019.Adapters.MyPurchaseAdapter;
import com.unimas.enelayan2019.Model.Cart;
import com.unimas.enelayan2019.Model.Purchase;

import java.util.ArrayList;

public class MyPurchaseActivity extends AppCompatActivity {
    private RecyclerView myPurchaseRV;
    private ArrayList<Purchase> purchaseArrayList;
    private FirebaseDatabase firebaseDatabase;
    private TextView noItem;
    private MyPurchaseAdapter myPurchaseAdapter;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchase);
        myPurchaseRV = findViewById(R.id.myPurchaseRV);
        noItem = findViewById(R.id.noItem);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyPurchaseActivity.this, AccountActivity.class));
            }
        });

        noItem.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myPurchaseRV.setLayoutManager(linearLayoutManager);

        purchaseArrayList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();

        final DatabaseReference cartRef= FirebaseDatabase.getInstance().getReference().child("Purchase");
        cartRef.orderByChild("customerId").equalTo(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        Purchase purchase = dataSnapshot1.getValue(Purchase.class);
                        purchaseArrayList.add(purchase);

                        myPurchaseAdapter = new MyPurchaseAdapter(purchaseArrayList, MyPurchaseActivity.this);
                        myPurchaseRV.setAdapter(myPurchaseAdapter);
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