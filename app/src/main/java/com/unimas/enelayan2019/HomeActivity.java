package com.unimas.enelayan2019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView botNav;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    private ProductViewAdapter productViewAdapter;
    private ArrayList<Product> productArrayList;
    private RecyclerView productRV;
    Product product;
    private EditText searchBar;
    DatabaseReference productReference;
    private long backPressedTime;
    private Toast backToast;
    private CircleImageView all, wholesale, prawn, vos, sea, freshFish, dried;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        searchBar = findViewById(R.id.search);

        dried = findViewById(R.id.driedSeafood);
        all = findViewById(R.id.allCat);
        wholesale = findViewById(R.id.wholesaleCat);
        prawn = findViewById(R.id.pwn);
        vos = findViewById(R.id.variants);
        sea = findViewById(R.id.sf);
        freshFish = findViewById(R.id.freshWater);

        productRV = (RecyclerView)findViewById(R.id.productRV);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        productRV.setLayoutManager(gridLayoutManager);

        productArrayList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        productReference = firebaseDatabase.getReference().child("Products");


        productReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        product = dataSnapshot1.getValue(Product.class);
                        productArrayList.add(product);
                    }

                    productViewAdapter = new ProductViewAdapter(HomeActivity.this, productArrayList);
                    productRV.setAdapter(productViewAdapter);
                }
                else {
                    Toast.makeText(HomeActivity.this, "Nothing yet.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        wholesale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productReference.orderByChild("productCategory").equalTo("Wholesale").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            productArrayList.clear();
                            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                product = dataSnapshot1.getValue(Product.class);
                                productArrayList.add(product);
                            }

                            productViewAdapter = new ProductViewAdapter(HomeActivity.this, productArrayList);
                            productRV.setAdapter(productViewAdapter);
                        }
                        else {
                            Toast.makeText(HomeActivity.this, "Nothing yet.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            productArrayList.clear();
                            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                product = dataSnapshot1.getValue(Product.class);
                                productArrayList.add(product);
                            }

                            productViewAdapter = new ProductViewAdapter(HomeActivity.this, productArrayList);
                            productRV.setAdapter(productViewAdapter);
                        }
                        else {
                            Toast.makeText(HomeActivity.this, "Nothing yet.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        prawn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productReference.orderByChild("productCategory").equalTo("Prawn").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            productArrayList.clear();
                            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                product = dataSnapshot1.getValue(Product.class);
                                productArrayList.add(product);
                            }

                            productViewAdapter = new ProductViewAdapter(HomeActivity.this, productArrayList);
                            productRV.setAdapter(productViewAdapter);
                        }
                        else {
                            Toast.makeText(HomeActivity.this, "Nothing yet.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        freshFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productReference.orderByChild("productCategory").equalTo("Fresh Water Fish").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            productArrayList.clear();
                            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                product = dataSnapshot1.getValue(Product.class);
                                productArrayList.add(product);
                            }

                            productViewAdapter = new ProductViewAdapter(HomeActivity.this, productArrayList);
                            productRV.setAdapter(productViewAdapter);
                        }
                        else {
                            Toast.makeText(HomeActivity.this, "Nothing yet.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        vos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productReference.orderByChild("productCategory").equalTo("Variants of Seafood").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            productArrayList.clear();
                            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                product = dataSnapshot1.getValue(Product.class);
                                productArrayList.add(product);
                            }

                            productViewAdapter = new ProductViewAdapter(HomeActivity.this, productArrayList);
                            productRV.setAdapter(productViewAdapter);
                        }
                        else {
                            Toast.makeText(HomeActivity.this, "Nothing yet.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        dried.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productReference.orderByChild("productCategory").equalTo("Dried Seafood").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            productArrayList.clear();
                            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                product = dataSnapshot1.getValue(Product.class);
                                productArrayList.add(product);
                            }

                            productViewAdapter = new ProductViewAdapter(HomeActivity.this, productArrayList);
                            productRV.setAdapter(productViewAdapter);
                        }
                        else {
                            Toast.makeText(HomeActivity.this, "Nothing yet.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        sea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productReference.orderByChild("productCategory").equalTo("Sea Fish").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            productArrayList.clear();
                            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                product = dataSnapshot1.getValue(Product.class);
                                productArrayList.add(product);
                            }

                            productViewAdapter = new ProductViewAdapter(HomeActivity.this, productArrayList);
                            productRV.setAdapter(productViewAdapter);
                        }
                        else {
                            Toast.makeText(HomeActivity.this, "Nothing yet.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                productViewAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

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


    @Override
    protected void onStart() {
        super.onStart();
        if (user==null){
            Intent i = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }else {
            backToast = Toast.makeText(this, "Tap on back button again to exit.", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }
}
