package com.example.ghx.freefood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ghx.freefood.R;
import com.example.ghx.freefood.bean.Status;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ghx on 2021/6/29.
 * 状态item的Adapter
 */

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {

    private Context mContext;
    private List<Status> mList;


    static class ViewHolder extends RecyclerView.ViewHolder {

        View statusView;

        CircleImageView iv_avatar;
        TextView tv_nickname;
        TextView tv_time;
        TextView tv_food_name;
        ImageView iv_food_photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            statusView = itemView;
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_food_name = itemView.findViewById(R.id.tv_food_name);
            iv_food_photo = itemView.findViewById(R.id.iv_food_photo);

        }
    }

    public StatusAdapter(Context context, List<Status> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public StatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.status_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StatusAdapter.ViewHolder holder, int position) {
        Status status = mList.get(position);
        String avatar = status.getAvatar();
        if (avatar != null) {
            Glide.with(mContext)
                    .load(avatar)
                    .into(holder.iv_avatar);
        }
        holder.tv_nickname.setText(status.getNickname());
        holder.tv_time.setText(status.getPublishtime());
        holder.tv_food_name.setText(status.getFoodname());
        String foodphoto = status.getFoodphoto();
        if (foodphoto != null) {
            Glide.with(mContext)
                    .load(foodphoto)
                    .into(holder.iv_food_photo);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
