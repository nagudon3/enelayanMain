package com.unimas.enelayan2019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimas.enelayan2019.Model.Purchase;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {
    private Button doneBtn;
    private TextView totalSales;
    private ArrayList<Purchase> purchaseArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        doneBtn = findViewById(R.id.doneButton);
        totalSales = findViewById(R.id.totalSales);
        purchaseArrayList = new ArrayList<>();
        final double[] totalD = {0.0};

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReportActivity.this, AccountActivity.class));
            }
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Purchase");
        databaseReference.orderByChild("sellerId").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        Purchase purchase = dataSnapshot1.getValue(Purchase.class);
                        purchaseArrayList.add(purchase);
                    }
                    for (int i = 0; i<purchaseArrayList.size(); i++){
                        totalD[0] += Double.parseDouble(purchaseArrayList.get(i).getPurchasePrice());
                    }
                    totalSales.setVisibility(View.VISIBLE);
                    String totalString = String.format("%.2f", totalD[0]);
                    totalSales.setText("Total Sales: RM "+totalString);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}