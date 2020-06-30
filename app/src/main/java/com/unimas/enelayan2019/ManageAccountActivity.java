package com.unimas.enelayan2019;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.unimas.enelayan2019.Model.Users;
import com.unimas.enelayan2019.Seller.AddProductActivity;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManageAccountActivity extends AppCompatActivity {
    private EditText name, email, phone, address;
    private CircleImageView userImage;
    private Button doneBtn;
    private ImageView backBtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private Users users;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    Uri ImageURI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        name = (EditText) findViewById(R.id.accountNameInput);
        email = (EditText) findViewById(R.id.accountEmailInput);
        phone = (EditText) findViewById(R.id.accountPhoneInput);
        address = (EditText) findViewById(R.id.accountAddressInput);
        userImage = (CircleImageView) findViewById(R.id.accountImage);
        doneBtn = (Button) findViewById(R.id.doneButton);
        backBtn = (ImageView) findViewById(R.id.backBtn);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(i);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users").child(mAuth.getUid());

        Glide.with(ManageAccountActivity.this).load(firebaseUser.getPhotoUrl()).into(userImage);

        name.setText(firebaseUser.getDisplayName());
        email.setText(firebaseUser.getEmail());

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestForPermissions();
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users = dataSnapshot.getValue(Users.class);
                address.setText(users.getAddress());
                phone.setText(users.getPhone());

                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error code: " +databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneBtn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(ManageAccountActivity.this, "Updating..", Toast.LENGTH_SHORT).show();
                final String uName = name.getText().toString();
                final String uPhone = phone.getText().toString();
                final String uEmail = email.getText().toString();
                final String uAddress = address.getText().toString();

                updateInfo(uName, uPhone, uEmail, uAddress, ImageURI);
            }
       });
    }

    private void updateInfo(final String uName, final String uPhone, final String uEmail, final String uAddress, Uri imageURI) {
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("users_photos");
        final StorageReference imageFilePath = storageReference.child(currentUser.getUid());

        if (imageURI!=null){
            imageFilePath.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                            databaseReference.child(currentUser.getUid());

                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().
                                    setDisplayName(uName)
                                    .setPhotoUri(uri)
                                    .build();

                            currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("address", uAddress);
                                        map.put("name", uName);
                                        map.put("phone", uPhone);
                                        map.put("email", uEmail);

                                        reference.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getApplicationContext(), "Profile updated!", Toast.LENGTH_LONG).show();
                                                doneBtn.setVisibility(View.VISIBLE);
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Intent intent = new Intent(ManageAccountActivity.this, AccountActivity.class);
                                                startActivity(intent);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Fail to update profile!", Toast.LENGTH_LONG).show();
                                                doneBtn.setVisibility(View.VISIBLE);
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Intent intent = new Intent(ManageAccountActivity.this, AccountActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }else {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
            databaseReference.child(currentUser.getUid());

            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().
                    setDisplayName(uName)
                    .build();

            currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("address", uAddress);
                        map.put("name", uName);
                        map.put("phone", uPhone);
                        map.put("email", uEmail);

                        reference.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Profile updated!", Toast.LENGTH_LONG).show();
                                doneBtn.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(ManageAccountActivity.this, AccountActivity.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Fail to update profile!", Toast.LENGTH_LONG).show();
                                doneBtn.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(ManageAccountActivity.this, AccountActivity.class);
                                startActivity(intent);
                            }
                        });

                    }
                }
            });
        }
    }


    private void checkAndRequestForPermissions() {
        if (ContextCompat.checkSelfPermission(ManageAccountActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(ManageAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(getApplicationContext(), "Please accept the permission to access your storage", Toast.LENGTH_SHORT).show();
            }else {
                ActivityCompat.requestPermissions(ManageAccountActivity.this,
                        new String[] { Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
                openGallery();
            }
        }else {
            openGallery();

        }
    }

    private void openGallery(){
        CropImage.activity().start(ManageAccountActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode==RESULT_OK){
                ImageURI = result.getUri();
                userImage.setImageURI(ImageURI);
            }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception e = result.getError();
                Toast.makeText(ManageAccountActivity.this, "Possible error occurred is " +e, Toast.LENGTH_SHORT).show();
            }

        }
    }
}

