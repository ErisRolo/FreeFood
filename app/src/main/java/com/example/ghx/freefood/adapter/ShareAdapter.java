package com.example.ghx.freefood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ghx.freefood.R;
import com.example.ghx.freefood.bean.Food;
import com.example.ghx.freefood.bean.User;
import com.example.ghx.freefood.utils.ShowToast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.LCUser;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ghx on 2021/6/25.
 * 分享列表Food的Adapter
 */

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {

    private Context mContext;
    private List<Food> mFoodList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View shareView;
        CircleImageView iv_buyer;
        TextView tv_buyer;
        TextView tv_state;
        ImageView iv_food_photo;
        TextView tv_food_name;
        TextView tv_food_desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            shareView = itemView;
            iv_buyer = itemView.findViewById(R.id.iv_buyer);
            tv_buyer = itemView.findViewById(R.id.tv_buyer);
            tv_state = itemView.findViewById(R.id.tv_state);
            iv_food_photo = itemView.findViewById(R.id.iv_food_photo);
            tv_food_name = itemView.findViewById(R.id.tv_food_name);
            tv_food_desc = itemView.findViewById(R.id.tv_food_desc);
        }
    }

    public ShareAdapter(Context context, List<Food> foodList) {
        mContext = context;
        mFoodList = foodList;
    }

    @NonNull
    @Override
    public ShareAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.share_item, parent, false);
        ShareAdapter.ViewHolder holder = new ShareAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShareAdapter.ViewHolder holder, int position) {
        Food food = mFoodList.get(position);
        String url = food.getFoodphoto();
        if (url != null) {
            Glide.with(mContext).load(url).into(holder.iv_food_photo);
        }
        holder.tv_food_name.setText(food.getFoodname());
        holder.tv_food_desc.setText(food.getFooddesc());
        String receiver = food.getReceiver();
        if (receiver != null) {
            LCQuery<User> query = LCObject.getQuery(User.class);
            query.whereEqualTo("objectId", receiver);
            query.findInBackground().subscribe(new Observer<List<User>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(List<User> users) {
                    User user = LCUser.cast(users.get(0), User.class);
                    if (user.getAvatar() != null) {
                        Glide.with(mContext)
                                .load(user.getAvatar())
                                .into(holder.iv_buyer);
                    }
                    holder.tv_buyer.setText(user.getNickname());
                }

                @Override
                public void onError(Throwable e) {
                    ShowToast.showShortToast(mContext, e.getMessage());
                }

                @Override
                public void onComplete() {

                }
            });
        } else {
            holder.tv_buyer.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }
}
