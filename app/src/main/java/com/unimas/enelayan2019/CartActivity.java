package com.unimas.enelayan2019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimas.enelayan2019.Adapters.CartAdapter;
import com.unimas.enelayan2019.Adapters.PostAdapter;
import com.unimas.enelayan2019.Adapters.ProductViewAdapter;
import com.unimas.enelayan2019.Model.Cart;
import com.unimas.enelayan2019.Model.Post;
import com.unimas.enelayan2019.Model.Product;
import com.unimas.enelayan2019.Model.test;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private BottomNavigationView botNav;
    private RecyclerView cartRv;
    private CartAdapter cartAdapter;
    private ArrayList<Cart> cartArrayList;
    private Button checkOutButton;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<Product> productArrayList;
    private TextView total, noCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cartRv = findViewById(R.id.cartList);
        checkOutButton = findViewById(R.id.checkoutButton);
        total = findViewById(R.id.total);
        noCart = findViewById(R.id.noCart);

        noCart.setVisibility(View.GONE);
        total.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        cartRv.setLayoutManager(linearLayoutManager);
        final double[] totalD = {0.0};

        productArrayList = new ArrayList<>();

        cartArrayList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference productReference = firebaseDatabase.getReference().child("Products");


        productReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Product product = dataSnapshot1.getValue(Product.class);
                    productArrayList.add(product);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference cartRef= FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getUid());
        cartRef.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
//                        if (dataSnapshot1.exists()){
                        Cart cart = dataSnapshot1.getValue(Cart.class);
                        cartArrayList.add(cart);
//                        }
                        for (int i = 0; i<cartArrayList.size(); i++){
                            totalD[0] += Double.parseDouble(cartArrayList.get(i).getPrice());
                        }

                        cartAdapter = new CartAdapter(cartArrayList, CartActivity.this);
                        cartRv.setAdapter(cartAdapter);

                        total.setVisibility(View.VISIBLE);
                        String totalString = String.format("%.2f", totalD[0]);
                        total.setText("Total Amount: RM "+totalString);
                    }
                }else {
                    noCart.setVisibility(View.VISIBLE);
                    checkOutButton.setVisibility(View.GONE);
                    total.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        checkOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i<cartArrayList.size(); i++){
                    DatabaseReference purchaseRef = FirebaseDatabase.getInstance().getReference().child("Purchase").push();
                    String cid = cartArrayList.get(i).getUid();
                    String name = cartArrayList.get(i).getProductName();
                    String pid = purchaseRef.getKey();

                    test test = new test(pid, name, cid);
                    purchaseRef.setValue(test).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startActivity(new Intent(CartActivity.this, ConfirmOrderActivity.class));
                            cartRef.removeValue();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CartActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        botNav = findViewById(R.id.bottomNav);
        botNav.setSelectedItemId(R.id.cart);
        botNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.post:
                        startActivity(new Intent(getApplicationContext(), PostActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.cart:
                        return true;

                    case R.id.account:
                        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}
