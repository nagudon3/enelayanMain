package com.unimas.enelayan2019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimas.enelayan2019.Model.Post;

public class AccountActivity extends AppCompatActivity {
    private BottomNavigationView botNav;
    private TextView logoutButton, manageAcc, regSeller, regFisherman, myPost;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        logoutButton = (TextView)findViewById(R.id.logoutBtn);
        manageAcc = (TextView) findViewById(R.id.manageAcc);
        regSeller = (TextView) findViewById(R.id.regSeller);
        regFisherman = (TextView) findViewById(R.id.regFishermen);
        myPost = (TextView) findViewById(R.id.myPost);

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


    //Edit onstart later for fisherman account and seller account
    @Override
    protected void onStart() {
        super.onStart();
        final String a = "approved";
        final DatabaseReference fishermanReference = FirebaseDatabase.getInstance().getReference().child("Fisherman").child(mAuth.getCurrentUser().getUid());
        fishermanReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("approvalStatus").getValue().equals(true)){
                    Toast.makeText(AccountActivity.this, "Haha", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AccountActivity.this, "Sedih", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
