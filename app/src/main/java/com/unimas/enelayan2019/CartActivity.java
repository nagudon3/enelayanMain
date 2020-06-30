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

        cartArrayList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();

        final DatabaseReference cartRef= FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getUid());
        cartRef.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        Cart cart = dataSnapshot1.getValue(Cart.class);
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
                                DatabaseReference purchaseRef = firebaseDatabase.getReference().child("Purchase").push();
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
