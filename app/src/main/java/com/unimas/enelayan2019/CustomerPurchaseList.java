package com.unimas.enelayan2019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.unimas.enelayan2019.Adapters.CustomerPurchaseAdapter;
import com.unimas.enelayan2019.Adapters.MyPurchaseAdapter;
import com.unimas.enelayan2019.Model.Purchase;

import java.util.ArrayList;

public class CustomerPurchaseList extends AppCompatActivity {
    private RecyclerView customerPurchaseRV;
    private ArrayList<Purchase> purchaseArrayList;
    private FirebaseDatabase firebaseDatabase;
    private TextView noItem, total;
    private CustomerPurchaseAdapter myPurchaseAdapter;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_purchase_list);

        customerPurchaseRV = findViewById(R.id.custPurchaseRV);
        noItem = findViewById(R.id.noItem);
        total = findViewById(R.id.total);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerPurchaseList.this, AccountActivity.class));
            }
        });


        noItem.setVisibility(View.GONE);
        total.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        customerPurchaseRV.setLayoutManager(linearLayoutManager);

        purchaseArrayList = new ArrayList<>();
        final double[] totalD = {0.0};


        firebaseDatabase = FirebaseDatabase.getInstance();

        final DatabaseReference purchaseRef= FirebaseDatabase.getInstance().getReference().child("Purchase");
        purchaseRef.orderByChild("sellerId").equalTo(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        final Purchase purchase = dataSnapshot1.getValue(Purchase.class);
                        purchaseArrayList.add(purchase);

                        final View.OnClickListener callListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (ContextCompat.checkSelfPermission(CustomerPurchaseList.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                                    ActivityCompat.requestPermissions(CustomerPurchaseList.this, new String[] {Manifest.permission.CALL_PHONE}, 1);
                                }else {
                                    String custPhoneNumber = "tel:" + purchase.getCustomerPhone();
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse(custPhoneNumber));
                                    startActivity(intent);
                                }
                            }
                        };

                        View.OnClickListener wsListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url = "https://wa.me/+6"+purchase.getCustomerPhone();
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);
                            }
                        };

                        myPurchaseAdapter = new CustomerPurchaseAdapter(purchaseArrayList, CustomerPurchaseList.this, callListener, wsListener);
                        customerPurchaseRV.setAdapter(myPurchaseAdapter);
                    }
                    for (int i = 0; i<purchaseArrayList.size(); i++){
                        totalD[0] += Double.parseDouble(purchaseArrayList.get(i).getPurchasePrice());
                    }
                    total.setVisibility(View.VISIBLE);
                    String totalString = String.format("%.2f", totalD[0]);
                    total.setText("Total Sales: RM "+totalString);

                }else {
                    noItem.setVisibility(View.VISIBLE);
                    total.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}