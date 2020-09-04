package com.unimas.enelayan2019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimas.enelayan2019.Fisherman.AddWholesaleActivity;
import com.unimas.enelayan2019.Model.Post;
import com.unimas.enelayan2019.Seller.AddProductActivity;

public class AccountActivity extends AppCompatActivity {
    private BottomNavigationView botNav;
    private TextView logoutButton, manageAcc, regSeller, regFisherman, myPost, addProduct, addWholesale, purchaseList, custPurchaseList, accountType, sales;
    private ProgressBar loading;
    FirebaseAuth mAuth;
    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        logoutButton = (TextView)findViewById(R.id.logoutBtn);
        manageAcc = (TextView) findViewById(R.id.manageAcc);
        regSeller = (TextView) findViewById(R.id.regSeller);
        regFisherman = (TextView) findViewById(R.id.regFishermen);
        sales = (TextView) findViewById(R.id.salesSummary);
        myPost = (TextView) findViewById(R.id.myPost);
        addProduct = (TextView) findViewById(R.id.addProduct);
        loading = (ProgressBar) findViewById(R.id.loading);
        purchaseList = (TextView)findViewById(R.id.myPurchase);
        custPurchaseList = (TextView)findViewById(R.id.custOrderList);
        addWholesale = (TextView) findViewById(R.id.addWholesaleProduct);
        accountType = (TextView) findViewById(R.id.accountType);

        custPurchaseList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountActivity.this, CustomerPurchaseList.class));
            }
        });
        purchaseList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountActivity.this, MyPurchaseActivity.class));
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddProductActivity.class));
            }
        });

        regFisherman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AccountActivity.this, RegisterFishermanActivity.class);
                startActivity(i);
            }
        });

        regSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AccountActivity.this, RegisterSellerActivity.class);
                startActivity(i);
            }
        });

        manageAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AccountActivity.this, ManageAccountActivity.class);
                startActivity(i);
            }
        });

        myPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AccountActivity.this, MyPostActivity.class);
                startActivity(i);
            }
        });

        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AccountActivity.this, ReportActivity.class);
                startActivity(i);
            }
        });
        addWholesale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddWholesaleActivity.class));
            }
        });
        mAuth = FirebaseAuth.getInstance();
        botNav = findViewById(R.id.bottomNav);
        botNav.setSelectedItemId(R.id.account);
        botNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
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
                        return true;
                }
                return false;
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



//    //Edit onstart later for fisherman account and seller account
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser()!=null){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final DatabaseReference fishermanReference = FirebaseDatabase.getInstance().getReference().child("Fisherman").child(mAuth.getCurrentUser().getUid());
            fishermanReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        if (dataSnapshot.child("approvalStatus").getValue().equals(true)){
                            regSeller = (TextView) findViewById(R.id.regSeller);
                            regFisherman = (TextView) findViewById(R.id.regFishermen);
                            addWholesale = (TextView) findViewById(R.id.addWholesaleProduct);
                            addProduct = (TextView) findViewById(R.id.addProduct);
                            addProduct.setVisibility(View.VISIBLE);
                            addWholesale.setVisibility(View.VISIBLE);
                            regSeller.setVisibility(View.GONE);
                            regFisherman.setVisibility(View.GONE);
                            sales.setVisibility(View.VISIBLE);
                            custPurchaseList = (TextView)findViewById(R.id.custOrderList);
                            custPurchaseList.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            accountType.setText("Fisherman Account");

                        }else {
                            loading.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                    }else
                    {
                        final DatabaseReference sellerRef = FirebaseDatabase.getInstance().getReference().child("Seller").child(mAuth.getCurrentUser().getUid());
                        sellerRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    if (dataSnapshot.child("approvalStatus").getValue().equals(true)){
                                        regSeller = (TextView) findViewById(R.id.regSeller);
                                        regFisherman = (TextView) findViewById(R.id.regFishermen);
                                        addWholesale = (TextView) findViewById(R.id.addWholesaleProduct);
                                        addProduct = (TextView) findViewById(R.id.addProduct);
                                        addProduct.setVisibility(View.VISIBLE);
                                        regSeller.setVisibility(View.GONE);
                                        custPurchaseList = (TextView)findViewById(R.id.custOrderList);
                                        custPurchaseList.setVisibility(View.VISIBLE);
                                        regFisherman.setVisibility(View.GONE);
                                        loading.setVisibility(View.GONE);
                                        accountType.setText("Seller Account");
                                        sales.setVisibility(View.VISIBLE);
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    }else {
                                        loading.setVisibility(View.GONE);
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    }
                                }
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        loading.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else {
            startActivity(new Intent(AccountActivity.this, MainActivity.class));
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
