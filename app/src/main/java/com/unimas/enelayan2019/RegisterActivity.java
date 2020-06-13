package com.unimas.enelayan2019;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.unimas.enelayan2019.Model.Users;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, address, phone, password, confirmPassword, email;
    private Button regBtn, uploadBtn;
    private ImageView backBtn;
    private CircleImageView uploadImage;
    private Object JSONObject;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    Uri ImageURI;
    int PReqCode = 1;
    int REQUESTCODE = 1;
    public Boolean upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.inputName);
        address = (EditText) findViewById(R.id.inputAddress);
        email = (EditText) findViewById(R.id.inputEmail);
        phone = (EditText) findViewById(R.id.inputPhone);
        password = (EditText) findViewById(R.id.inputPassword);
        confirmPassword = (EditText) findViewById(R.id.inputConfirmPassword);
        regBtn = (Button) findViewById(R.id.registerButton);
        backBtn = (ImageView) findViewById(R.id.backBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        uploadImage = (CircleImageView) findViewById(R.id.uploadImage);

        upload = false;

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermissions();
                }
                else {
                    openGallery();
                }
                upload = true;
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(back);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upload){
                    registerUser();
                }else {
                    Toast.makeText(RegisterActivity.this, "You have to upload your profile image to continue registration!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openGallery(){
        CropImage.activity().start(RegisterActivity.this);
    }

    private void checkAndRequestForPermissions(){
        if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(getApplicationContext(), "Please accept the permission to access your storage", Toast.LENGTH_SHORT).show();
            }else {
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[] { Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
                openGallery();
            }
        }else {
            openGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode==RESULT_OK){
                ImageURI = result.getUri();
                uploadImage.setImageURI(ImageURI);
            }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception e = result.getError();
                Toast.makeText(RegisterActivity.this, "Possible error occured is " +e, Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser()!=null){
        }
    }

    private void registerUser() {
        final String postName = name.getText().toString();
        final String postAddress = address.getText().toString();
        final String postEmail = email.getText().toString();
        final String postPhone = phone.getText().toString();
        String postPassword = password.getText().toString();
        String postConfPassword = confirmPassword.getText().toString();

        progressBar.setVisibility(View.VISIBLE);
        regBtn.setVisibility(View.INVISIBLE);

        if(TextUtils.isEmpty(postName) || TextUtils.isEmpty(postAddress)|| TextUtils.isEmpty(postPhone)||
                TextUtils.isEmpty(postPassword)){
            Toast.makeText(RegisterActivity.this, "Please fill in all required field for registration.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            regBtn.setVisibility(View.VISIBLE);
        }else if(!postPassword.equals(postConfPassword)) {
            Toast.makeText(RegisterActivity.this, "The password does not match!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            regBtn.setVisibility(View.VISIBLE);
//        add validation for image upload
        }
//        else if(upload=false){
//            Toast.makeText(RegisterActivity.this, "You have to upload your profile image to continue registration!", Toast.LENGTH_SHORT).show();
//            progressBar.setVisibility(View.INVISIBLE);
//            regBtn.setVisibility(View.VISIBLE);
        else {
            mAuth.createUserWithEmailAndPassword(postEmail, postPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Users users = new Users(postName, postAddress, postEmail, postPhone);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    updateInfo(postName, ImageURI, mAuth.getCurrentUser());
                                }else {
                                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    regBtn.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }else {
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        regBtn.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    private void updateInfo(final String postName, final Uri imageURI, final FirebaseUser currentUser) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("users_photos");
        final StorageReference imageFilePath = storageReference.child(mAuth.getCurrentUser().getUid());

        imageFilePath.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().
                                setDisplayName(postName)
                                .setPhotoUri(imageURI)
                                .build();

                        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Successfully registered!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    regBtn.setVisibility(View.VISIBLE);
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                });
            }
        });
    }


}