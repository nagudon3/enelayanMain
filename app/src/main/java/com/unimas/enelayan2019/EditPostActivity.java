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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.unimas.enelayan2019.Model.Post;

import java.util.HashMap;

public class EditPostActivity extends AppCompatActivity {
    private EditText postDetail;
    private ImageView postImages;
    private Button editButton;
    private FirebaseAuth mAuth;
    Post post;
    private FirebaseDatabase database;
    DatabaseReference postRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        postDetail = (EditText) findViewById(R.id.postDetails);
        editButton = (Button) findViewById(R.id.submitButton);
        postImages = (ImageView) findViewById(R.id.postImage);

        mAuth = FirebaseAuth.getInstance();
//
        String postDetails = getIntent().getExtras().getString("postDetails");
        String postImage = getIntent().getExtras().getString("postImage");
        final String postId = getIntent().getExtras().getString("postKey");
        Glide.with(this).load(postImage).into(postImages);

        postDetail.setText(postDetails);

        database = FirebaseDatabase.getInstance();
        postRef = database.getReference().child("Posts").child(postId);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uPostDetail = postDetail.getText().toString();

                updatePost(uPostDetail, postId);
            }
        });
    }

    private void updatePost(final String uPostDetail, final String postIds) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts").child(postIds);
        HashMap<String, Object> map = new HashMap<>();
        map.put("details", uPostDetail);
        map.put("postDetails", uPostDetail);

        databaseReference.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditPostActivity.this, "Post updated!", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(EditPostActivity.this, MyPostActivity.class));
            }
        });

    }

}