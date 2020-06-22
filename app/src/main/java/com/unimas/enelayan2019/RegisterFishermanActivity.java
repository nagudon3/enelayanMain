package com.unimas.enelayan2019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimas.enelayan2019.Model.Fisherman;
import com.unimas.enelayan2019.Model.Seller;
import com.unimas.enelayan2019.Model.Users;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterFishermanActivity extends AppCompatActivity {

    private CircleImageView userImage;
    private EditText years, fishingArea, fishingLicense;
    private Button submit;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Fisherman fisherman;
    Seller seller;
    Users users;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_fisherman);

        userImage = (CircleImageView) findViewById(R.id.userPics);
        years = (EditText) findViewById(R.id.yearsFisherman);
        fishingArea = (EditText) findViewById(R.id.fishingArea);
        fishingLicense = (EditText) findViewById(R.id.fishermanLicenseNumber);
        submit = (Button) findViewById(R.id.submitBtn);
        mAuth=FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(userImage);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerFisherman();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference fishermanReference = FirebaseDatabase.getInstance().getReference().child("Fisherman");
        fishermanReference.orderByChild("fishermanID").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists()){
                   Toast.makeText(getApplicationContext(), "Already registered!", Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(getApplicationContext(), AccountActivity.class));
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void registerFisherman() {
        progressBar.setVisibility(View.VISIBLE);
        final String fYears = years.getText().toString();
        final String fArea = fishingArea.getText().toString();
        final String fLicense = fishingLicense.getText().toString();
        final Boolean approvalStatus = false;

        progressBar.setVisibility(View.VISIBLE);
        submit.setVisibility(View.INVISIBLE);

        if(TextUtils.isEmpty(fArea) || TextUtils.isEmpty(fLicense)|| TextUtils.isEmpty(fYears)){
            Toast.makeText(RegisterFishermanActivity.this, "Please fill in all required field for registration.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            submit.setVisibility(View.VISIBLE);
        }else {
            FirebaseDatabase userDatabase = FirebaseDatabase.getInstance();
            final FirebaseDatabase fishermanDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference userReference = userDatabase.getReference().child("Users").child(user.getUid());
            final DatabaseReference fishermanReference = fishermanDatabase.getReference().child("Fisherman").child(user.getUid());

            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    users = dataSnapshot.getValue(Users.class);
                    String fName = users.getName().toString();
                    String fPhone = users.getPhone().toString();
                    String fAddress = users.getAddress().toString();

                    fisherman = new Fisherman(mAuth.getUid(), fName, fLicense, fArea, fPhone, fAddress, fYears, approvalStatus);
                    //tambah seller
                    fishermanReference.setValue(fisherman).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Your application will be processed", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                submit.setVisibility(View.VISIBLE);
                            }else {
                                Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
