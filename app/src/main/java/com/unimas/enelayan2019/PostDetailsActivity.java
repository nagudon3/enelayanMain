package com.unimas.enelayan2019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimas.enelayan2019.Adapters.CommentAdapter;
import com.unimas.enelayan2019.Model.Comment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailsActivity extends AppCompatActivity {

    private TextView postDetailsUserName, postDetailsDetails, postDetailsDate;
    private ImageView postDetailsImage;
    private CircleImageView postDetailsUserImage, postCurrentUserImage;
    private EditText comment;
    private RecyclerView commentView;
    CommentAdapter commentAdapter;
    List<Comment> commentList;
    private ImageView addCommentButton;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String PostKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        postDetailsDetails = (TextView) findViewById(R.id.postDetails);
        postDetailsUserName = (TextView) findViewById(R.id.posterName);
        postDetailsDate = (TextView) findViewById(R.id.postDate);
        postDetailsUserImage = (CircleImageView) findViewById(R.id.posterImage);
        postCurrentUserImage = (CircleImageView) findViewById(R.id.currentUserImage);
        postDetailsImage = (ImageView) findViewById(R.id.postImageDetails);
        comment = (EditText) findViewById(R.id.commentField);
        addCommentButton = (ImageView) findViewById(R.id.addCommentButton);
        commentView = (RecyclerView) findViewById(R.id.commentRV);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        String userName = getIntent().getExtras().getString("userName");
        String postDetails = getIntent().getExtras().getString("postDetails");
        String postDate = timestampToString(getIntent().getExtras().getLong("postDate"));
        String posterImages = getIntent().getExtras().getString("posterImage");
        String postImages = getIntent().getExtras().getString("postImage");
        Glide.with(this).load(postImages).into(postDetailsImage);

        postDetailsUserName.setText(userName);
        postDetailsDetails.setText(postDetails);
        postDetailsDate.setText(postDate);
        Glide.with(this).load(posterImages).into(postDetailsUserImage);
        Glide.with(this).load(currentUser.getPhotoUrl()).into(postCurrentUserImage);

        PostKey = getIntent().getExtras().getString("postKey");

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommentButton.setVisibility(View.INVISIBLE);
                DatabaseReference reference = database.getReference().child("Comments").child(PostKey).push();
                String comments = comment.getText().toString();
                String userId = currentUser.getUid();
                String userName = currentUser.getDisplayName();
                String userImage = currentUser.getPhotoUrl().toString();

                final Comment comment = new Comment(comments, userId, userName, userImage);

                reference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        addCommentButton.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Comment added successfully!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        addCommentButton.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Fail to add comment.", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        initComment();

    }

    private void initComment() {
        commentView.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference commentReference = database.getReference().child("Comments").child(PostKey);
        commentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Comment commented = dataSnapshot1.getValue(Comment.class);
                    commentList.add(commented);
                }

                commentAdapter = new CommentAdapter(PostDetailsActivity.this, commentList);
                commentView.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String timestampToString(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        return date;
    }
}
