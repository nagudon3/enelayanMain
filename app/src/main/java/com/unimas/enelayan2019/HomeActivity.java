package com.unimas.enelayan2019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimas.enelayan2019.Adapters.PostAdapter;
import com.unimas.enelayan2019.Adapters.ProductViewAdapter;
import com.unimas.enelayan2019.Model.Post;
import com.unimas.enelayan2019.Model.Product;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView botNav;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    private ProductViewAdapter productViewAdapter;
    private ArrayList<Product> productArrayList;
    private RecyclerView productRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        productRV = (RecyclerView)findViewById(R.id.productRV);
        productRV.setLayoutManager(new GridLayoutManager(this, 2));
        productArrayList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference productReference = firebaseDatabase.getReference().child("Products");


        productReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Product product = dataSnapshot1.getValue(Product.class);
                    productArrayList.add(product);
                }
                productViewAdapter = new ProductViewAdapter(HomeActivity.this, productArrayList);
                productRV.setAdapter(productViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        botNav = findViewById(R.id.bottomNav);
        botNav.setSelectedItemId(R.id.home);
        botNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        return true;

                    case R.id.post:
                        startActivity(new Intent(getApplicationContext(), PostActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.cart:
                        startActivity(new Intent(getApplicationContext(), CartActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.account:
                        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
//
    @Override
    protected void onStart() {
        super.onStart();
        if (user==null){
            Intent i = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(i);
        }
    }
}
