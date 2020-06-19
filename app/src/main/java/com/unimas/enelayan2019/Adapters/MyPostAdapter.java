package com.unimas.enelayan2019.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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
        ImageView postImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            accountName = itemView.findViewById(R.id.accountName);
            postDetailsView = itemView.findViewById(R.id.postDetailsView);
            userImage = itemView.findViewById(R.id.accountImage);
            postImage = itemView.findViewById(R.id.postImageView);
            time = itemView.findViewById(R.id.time);

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
        }
    }
    private String timestampToString(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        return date;
    }
}
