package com.unimas.enelayan2019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimas.enelayan2019.Adapters.MyPostAdapter;
import com.unimas.enelayan2019.Adapters.PostAdapter;
import com.unimas.enelayan2019.Model.Post;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyPostActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Post> postArrayList;
    private MyPostAdapter postAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String currentUserId;
    private DatabaseReference reference;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        recyclerView = (RecyclerView) findViewById(R.id.myPostRV);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        emptyText = (TextView) findViewById(R.id.emptyText);
        emptyText.setVisibility(View.INVISIBLE);
        currentUserId = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Posts");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postArrayList = new ArrayList<>();

        reference.orderByChild("userId").equalTo(currentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        if (dataSnapshot1.exists()){
                            Post post = dataSnapshot1.getValue(Post.class);
                            postArrayList.add(post);
                        }else {
                            emptyText.setVisibility(View.VISIBLE);
                        }

                    }
                    postAdapter = new MyPostAdapter(MyPostActivity.this, postArrayList);
                    recyclerView.setAdapter(postAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
}
