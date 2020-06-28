package com.unimas.enelayan2019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimas.enelayan2019.Adapters.CartAdapter;
import com.unimas.enelayan2019.Adapters.PostAdapter;
import com.unimas.enelayan2019.Model.Cart;
import com.unimas.enelayan2019.Model.Post;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private BottomNavigationView botNav;
    private RecyclerView cartRv;
    private CartAdapter cartAdapter;
    private ArrayList<Cart> cartArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cartRv = findViewById(R.id.cartList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        cartRv.setLayoutManager(linearLayoutManager);

        cartArrayList = new ArrayList<>();

        DatabaseReference cartRef= FirebaseDatabase.getInstance().getReference().child("Cart");
        cartRef.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    if (dataSnapshot1.exists()){
                        Cart cart = dataSnapshot1.getValue(Cart.class);
                        cartArrayList.add(cart);
                    }
                    else {
                        Toast.makeText(CartActivity.this, "You don't have anything in the cart!", Toast.LENGTH_SHORT).show();
                    }
                }
                cartAdapter = new CartAdapter(cartArrayList, CartActivity.this);
                cartRv.setAdapter(cartAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
