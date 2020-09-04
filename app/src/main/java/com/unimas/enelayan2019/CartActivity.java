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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.unimas.enelayan2019.Model.Purchase;
import com.unimas.enelayan2019.Model.Users;
import com.unimas.enelayan2019.Model.test;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    private BottomNavigationView botNav;
    private RecyclerView cartRv;
    private CartAdapter cartAdapter;
    private ArrayList<Cart> cartArrayList;
    private Button checkOutButton;
    private FirebaseDatabase firebaseDatabase;
    private TextView total, noCart;
    private ProgressBar progressBar;
    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cartRv = findViewById(R.id.cartList);
        checkOutButton = findViewById(R.id.checkoutButton);
        total = findViewById(R.id.total);
        noCart = findViewById(R.id.noCart);
        progressBar = findViewById(R.id.progressBar);


        noCart.setVisibility(View.GONE);
        total.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        cartRv.setLayoutManager(linearLayoutManager);
        final double[] totalD = {0.0};

        cartArrayList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();

        final DatabaseReference cartRef= FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getUid());
        cartRef.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    progressBar.setVisibility(View.GONE);
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        final Cart cart = dataSnapshot1.getValue(Cart.class);
                        cartArrayList.add(cart);

                        cartAdapter = new CartAdapter(cartArrayList, CartActivity.this);
                        cartRv.setAdapter(cartAdapter);
                    }
                    for (int i = 0; i<cartArrayList.size(); i++){
                        totalD[0] += Double.parseDouble(cartArrayList.get(i).getPrice());
                    }
                    total.setVisibility(View.VISIBLE);
                    String totalString = String.format("%.2f", totalD[0]);
                    total.setText("Total Amount: RM "+totalString);
                    checkOutButton.setVisibility(View.VISIBLE);

                }else {
                    progressBar.setVisibility(View.GONE);
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
                final String[] method = {""};
                DatabaseReference userRef = firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Users users = dataSnapshot.getValue(Users.class);
                            String customerName = users.getName();
                            String customerPhone = users.getPhone();
                            String customerAddress = users.getAddress();

                            for (int i = 0; i<cartArrayList.size(); i++){
                                final DatabaseReference purchaseRef = firebaseDatabase.getReference().child("Purchase").push();
                                String purchaseId = purchaseRef.getKey();
                                if (cartArrayList.get(i).getCod() == true){
                                    method[0] = "COD";
                                }else if (cartArrayList.get(i).getPickup() == true){
                                    method[0] = "Pick-up";
                                }
                                Purchase purchase = new Purchase(
                                        purchaseId,
                                        cartArrayList.get(i).getProductId(),
                                        cartArrayList.get(i).getSellerId(),
                                        FirebaseAuth.getInstance().getUid(),
                                        cartArrayList.get(i).getAmountOrdered(),
                                        cartArrayList.get(i).getPrice(),
                                        cartArrayList.get(i).getProductName(),
                                        cartArrayList.get(i).getProductImage(),
                                        customerPhone,
                                        customerAddress,
                                        customerName,
                                        method[0]
                                );


                                purchaseRef.setValue(purchase).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        startActivity(new Intent(CartActivity.this, ConfirmOrderActivity.class));
                                        cartRef.removeValue();
                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    //Updating product amount

                for (int i = 0; i<cartArrayList.size(); i++){
                    final DatabaseReference productRef = firebaseDatabase.getReference().child("Products").child(cartArrayList.get(i).getProductId());
                    final int finalI = i;
                    productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Product product = dataSnapshot.getValue(Product.class);
                            double amountAvailable = Double.parseDouble(product.getAmountAvailable());
                            double amountPurchased = Double.parseDouble(cartArrayList.get(finalI).getAmountOrdered());
                            double amountLeft = amountAvailable - amountPurchased;

                            String amountLeftS = String.valueOf(amountLeft);

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("amountAvailable", amountLeftS);


                            productRef.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

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
