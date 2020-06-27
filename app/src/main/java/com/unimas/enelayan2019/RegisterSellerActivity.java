package com.unimas.enelayan2019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.unimas.enelayan2019.Model.Fisherman;
import com.unimas.enelayan2019.Model.Seller;
import com.unimas.enelayan2019.Model.Users;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterSellerActivity extends AppCompatActivity {

    private ImageView backBtn;
    private CircleImageView accountImage;
    private EditText sellingArea, fishSource, sellingReason;
    private Button submitButton;
    FirebaseAuth mAuth;
    Seller seller;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_seller);

        backBtn = (ImageView) findViewById(R.id.backBtn);
        accountImage = (CircleImageView)findViewById(R.id.userPics);
        sellingArea = (EditText)findViewById(R.id.sellingArea);
        fishSource = (EditText)findViewById(R.id.fishSource);
        sellingReason = (EditText)findViewById(R.id.reasonSelling);
        submitButton = (Button)findViewById(R.id.sellerSubmitButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        Glide.with(getApplicationContext()).load(mAuth.getCurrentUser().getPhotoUrl()).into(accountImage);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterSellerActivity.this, AccountActivity.class));
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitButton.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                registerSeller();
            }
        });

    }

    private void registerSeller() {
        final String sArea = sellingArea.getText().toString();
        final String sFishSource = fishSource.getText().toString();
        final String sReason = sellingReason.getText().toString();
        final Boolean approvalStatus = false;

        if (sArea.isEmpty() && sFishSource.isEmpty() && sReason.isEmpty()){
            Toast.makeText(this, "Please enter all required information.", Toast.LENGTH_SHORT).show();
        }else {
            FirebaseDatabase userDatabase = FirebaseDatabase.getInstance();
            final FirebaseDatabase sellerDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference userReference = userDatabase.getReference().child("Users").child(mAuth.getCurrentUser().getUid());
            final DatabaseReference sellerReference = sellerDatabase.getReference().child("Seller").child(mAuth.getCurrentUser().getUid());

            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Users users = dataSnapshot.getValue(Users.class);
                    String sellerName = users.getName();
                    String sellerImage = mAuth.getCurrentUser().getPhotoUrl().toString();
                    String sellerPhone = users.getPhone();
                    String sellerAddress = users.getAddress();

                    Seller seller = new Seller(mAuth.getUid(), sellerName, sellerImage, sArea, sReason, sFishSource, sellerPhone, sellerAddress, approvalStatus);
                    sellerReference.setValue(seller).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(RegisterSellerActivity.this, "Your application will be processed!", Toast.LENGTH_SHORT).show();
                            submitButton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterSellerActivity.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            submitButton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
//            userReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    users = dataSnapshot.getValue(Users.class);
//                    String sName = users.getName();
//                    String sPhone = users.getPhone();
//                    String sAddress = users.getAddress();
//                    String sImage = mAuth.getCurrentUser().getPhotoUrl().toString();
//
//                    seller = new Seller(mAuth.getCurrentUser().getUid(), sName, sImage, sArea, sReason, sFishSource, sPhone, sAddress, approvalStatus);
//                    sellerReference.setValue(seller).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()){
//                                Toast.makeText(getApplicationContext(), "Your application will be processed", Toast.LENGTH_SHORT).show();
//                                progressBar.setVisibility(View.INVISIBLE);
//                                submitButton.setVisibility(View.VISIBLE);
//                            }else {
//                                Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                        }
//                    });
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase sellerDatabase = FirebaseDatabase.getInstance();
        DatabaseReference sellerReference = sellerDatabase.getReference().child("Seller");

        sellerReference.orderByChild("sellerId").equalTo(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(RegisterSellerActivity.this, "You have registered for seller account", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
