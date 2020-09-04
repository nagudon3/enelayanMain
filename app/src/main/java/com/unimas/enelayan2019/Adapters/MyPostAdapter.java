package com.unimas.enelayan2019.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unimas.enelayan2019.DeletePostActivity;
import com.unimas.enelayan2019.EditPostActivity;
import com.unimas.enelayan2019.Model.Post;
import com.unimas.enelayan2019.Model.PostViewer;
import com.unimas.enelayan2019.PostDetailsActivity;
import com.unimas.enelayan2019.R;

import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.MyViewHolder> {

    Context mContext;
    List<Post> mPost;

    public MyPostAdapter(Context mContext, List<Post> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.my_post_list, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.accountName.setText(mPost.get(position).getUserName());
        holder.postDetailsView.setText(mPost.get(position).getDetails());
        Glide.with(mContext).load(mPost.get(position).getUserPics()).into(holder.userImage);
        Glide.with(mContext).load(mPost.get(position).getImage()).into(holder.postImage);
        long timestamp = (long) mPost.get(position).getTimeStamp();
        holder.time.setText(timestampToString(timestamp));
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView accountName, postDetailsView, time;
        CircleImageView userImage;
        ImageView postImage, deleteButton;
        Button editButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            accountName = itemView.findViewById(R.id.accountName);
            postDetailsView = itemView.findViewById(R.id.postDetailsView);
            userImage = itemView.findViewById(R.id.accountImage);
            postImage = itemView.findViewById(R.id.postImageView);
            time = itemView.findViewById(R.id.time);
            deleteButton = itemView.findViewById(R.id.deletePost);
            editButton = itemView.findViewById(R.id.editPost);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToDetails = new Intent(mContext, PostDetailsActivity.class);
                    int position = getAdapterPosition();

                    goToDetails.putExtra("userName", mPost.get(position).getUserName());
                    goToDetails.putExtra("postDetails", mPost.get(position).getDetails());
                    goToDetails.putExtra("postImage", mPost.get(position).getImage());
                    goToDetails.putExtra("posterImage", mPost.get(position).getUserPics());
                    goToDetails.putExtra("postKey", mPost.get(position).getPostKey());
                    long timestamp = (long) mPost.get(position).getTimeStamp();
                    goToDetails.putExtra("postDate", timestamp);
                    mContext.startActivity(goToDetails);
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent edit = new Intent(mContext, EditPostActivity.class);
                    int position = getAdapterPosition();

                    edit.putExtra("postDetails", mPost.get(position).getDetails());
                    edit.putExtra("postImage", mPost.get(position).getImage());
                    edit.putExtra("postKey", mPost.get(position).getPostKey());
                    mContext.startActivity(edit);

                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setCancelable(true);
                    builder.setMessage("Are you sure to delete this post?");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent delete = new Intent(mContext, DeletePostActivity.class);
                                    int position = getAdapterPosition();

                                    delete.putExtra("postId", mPost.get(position).getPostKey());
                                    mContext.startActivity(delete);
                                }
                            });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });


        }
    }
    private String timestampToString(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        return date;
    }
}
