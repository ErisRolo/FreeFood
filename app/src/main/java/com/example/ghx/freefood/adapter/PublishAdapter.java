package com.example.ghx.freefood.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ghx.freefood.R;
import com.example.ghx.freefood.bean.Food;
import com.example.ghx.freefood.bean.User;
import com.example.ghx.freefood.utils.ShowToast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.LCUser;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ghx on 2021/6/24.
 * 发布列表Food的Adapter
 */

public class PublishAdapter extends RecyclerView.Adapter<PublishAdapter.ViewHolder> {

    private Context mContext;
    private List<Food> mFoodList;

    private AlertDialog dialog_republish;
    private AlertDialog dialog_confirm;
    private AlertDialog dialog_waste;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View publishView;
        CircleImageView iv_buyer;
        TextView tv_buyer;
        TextView tv_state;
        ImageView iv_food_photo;
        TextView tv_food_name;
        TextView tv_food_desc;
        Button btn_republish;
        Button btn_confirm;
        Button btn_waste;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            publishView = itemView;
            iv_buyer = itemView.findViewById(R.id.iv_buyer);
            tv_buyer = itemView.findViewById(R.id.tv_buyer);
            tv_state = itemView.findViewById(R.id.tv_state);
            iv_food_photo = itemView.findViewById(R.id.iv_food_photo);
            tv_food_name = itemView.findViewById(R.id.tv_food_name);
            tv_food_desc = itemView.findViewById(R.id.tv_food_desc);
            btn_republish = itemView.findViewById(R.id.btn_republish);
            btn_confirm = itemView.findViewById(R.id.btn_confirm);
            btn_waste = itemView.findViewById(R.id.btn_waste);

        }
    }

    public PublishAdapter(Context context, List<Food> foodList) {
        mContext = context;
        mFoodList = foodList;
    }


    @NonNull
    @Override
    public PublishAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.publish_item, parent, false);
        PublishAdapter.ViewHolder holder = new PublishAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PublishAdapter.ViewHolder holder, int position) {
        Food food = mFoodList.get(position);
        int state = food.getState();
        switch (state) {
            case 0:
                holder.tv_state.setText("未交易");
                holder.btn_republish.setVisibility(View.INVISIBLE);
                holder.btn_confirm.setVisibility(View.INVISIBLE);
                holder.btn_waste.setVisibility(View.VISIBLE);
                break;
            case 1:
                holder.tv_state.setText("交易中");
                holder.btn_republish.setVisibility(View.VISIBLE);
                holder.btn_confirm.setVisibility(View.VISIBLE);
                holder.btn_waste.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.tv_state.setText("已交易");
                holder.btn_republish.setVisibility(View.INVISIBLE);
                holder.btn_confirm.setVisibility(View.INVISIBLE);
                holder.btn_waste.setVisibility(View.INVISIBLE);
                break;
            case 3:
                holder.tv_state.setText("已浪费");
                holder.btn_republish.setVisibility(View.INVISIBLE);
                holder.btn_confirm.setVisibility(View.INVISIBLE);
                holder.btn_waste.setVisibility(View.INVISIBLE);
                break;
        }
        String url = food.getFoodphoto();
        if (url != null) {
            Glide.with(mContext)
                    .load(url)
                    .into(holder.iv_food_photo);
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

        holder.btn_republish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogRepublish = LayoutInflater.from(mContext).inflate(R.layout.dialog_republish, null);
                dialog_republish = new AlertDialog.Builder(mContext)
                        .setView(dialogRepublish)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                food.setState(0);
                                food.setReceiver(null);
                                food.saveInBackground().subscribe(new Observer<LCObject>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(LCObject avObject) {
                                        holder.iv_buyer.setVisibility(View.INVISIBLE);
                                        holder.tv_buyer.setText("");
                                        holder.tv_state.setText("未交易");
                                        holder.btn_republish.setVisibility(View.INVISIBLE);
                                        holder.btn_confirm.setVisibility(View.INVISIBLE);
                                        holder.btn_waste.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        ShowToast.showShortToast(mContext, e.getMessage());
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });

                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                dialog_republish.show();
            }
        });

        holder.btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogConfirm = LayoutInflater.from(mContext).inflate(R.layout.dialog_confirm, null);
                dialog_confirm = new AlertDialog.Builder(mContext)
                        .setView(dialogConfirm)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                food.setState(2);
                                food.saveInBackground().subscribe(new Observer<LCObject>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(LCObject avObject) {
                                        holder.tv_state.setText("已交易");
                                        holder.btn_republish.setVisibility(View.INVISIBLE);
                                        holder.btn_confirm.setVisibility(View.INVISIBLE);
                                        holder.btn_waste.setVisibility(View.INVISIBLE);
                                        //添加正积分
                                        User mCurrentUser = User.getCurrentUser(User.class);
                                        mCurrentUser.setScore1(mCurrentUser.getScore1() + (int) Math.round(food.getFootprint()));
                                        mCurrentUser.saveInBackground().subscribe(new Observer<LCObject>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {

                                            }

                                            @Override
                                            public void onNext(LCObject lcObject) {
                                                ShowToast.showShortToast(mContext, "获得了正积分！");
                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onComplete() {

                                            }
                                        });

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                dialog_confirm.show();
            }
        });

        holder.btn_waste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogWaste = LayoutInflater.from(mContext).inflate(R.layout.dialog_waste, null);
                dialog_waste = new AlertDialog.Builder(mContext)
                        .setView(dialogWaste)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                food.setState(3);
                                food.saveInBackground().subscribe(new Observer<LCObject>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(LCObject avObject) {
                                        //holder.iv_buyer.setVisibility(View.INVISIBLE);
                                        holder.tv_state.setText("已浪费");
                                        holder.btn_republish.setVisibility(View.INVISIBLE);
                                        holder.btn_confirm.setVisibility(View.INVISIBLE);
                                        holder.btn_waste.setVisibility(View.INVISIBLE);
                                        //添加负积分
                                        User mCurrentUser = User.getCurrentUser(User.class);
                                        mCurrentUser.setScore2(mCurrentUser.getScore2() + (int) Math.round(food.getFootprint()));
                                        mCurrentUser.saveInBackground().subscribe(new Observer<LCObject>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {

                                            }

                                            @Override
                                            public void onNext(LCObject lcObject) {
                                                ShowToast.showShortToast(mContext, "获得了负积分！");
                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onComplete() {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                dialog_waste.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

}
