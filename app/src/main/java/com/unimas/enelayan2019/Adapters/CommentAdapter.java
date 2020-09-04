package com.unimas.enelayan2019.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unimas.enelayan2019.Model.Comment;
import com.unimas.enelayan2019.R;

import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private Context mContext;
    private List<Comment> mComments;

    public CommentAdapter(Context mContext, List<Comment> mComments) {
        this.mContext = mContext;
        this.mComments = mComments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.comment_list, parent, false);
        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        long timestamp = (long) mComments.get(position).getTimeStamp();
        holder.commentDates.setText(timestampToString(timestamp));
        holder.commenterNames.setText(mComments.get(position).getUserName());
        holder.commentDetailss.setText(mComments.get(position).getComment());
        Glide.with(mContext).load(mComments.get(position).getUserImage()).into(holder.commenterImages);
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        CircleImageView commenterImages;
        TextView commenterNames, commentDetailss, commentDates;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commenterImages = itemView.findViewById(R.id.commenterImage);
            commenterNames = itemView.findViewById(R.id.commenterName);
            commentDetailss = itemView.findViewById(R.id.commentDetails);
            commentDates = itemView.findViewById(R.id.commentTime);
        }
    }

    private String timestampToString(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd/MM/yy", calendar).toString();
        String clock = DateFormat.format("h:mm aa", calendar).toString();
        String realDate = date +" at "+ clock;
        return realDate;
    }
}
