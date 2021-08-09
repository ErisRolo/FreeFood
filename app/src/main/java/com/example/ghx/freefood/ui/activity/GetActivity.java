package com.example.ghx.freefood.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ghx.freefood.R;
import com.example.ghx.freefood.base.BaseActivity;
import com.example.ghx.freefood.bean.Food;
import com.example.ghx.freefood.bean.User;
import com.example.ghx.freefood.utils.Config;
import com.example.ghx.freefood.utils.ShowToast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.LCStatus;
import cn.leancloud.LCUser;
import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ghx on 2021/6/19.
 * 获取食物（食物详情）
 */

public class GetActivity extends BaseActivity implements View.OnClickListener {

    private static final String FOOD_ID = "food_id";
    private String food_id;
    private String publisher;

    private Toolbar mToolbar;

    private CircleImageView iv_avatar;
    private TextView tv_nickname;
    private TextView tv_time;
    private ImageView iv_food;
    private TextView tv_food_name;
    private TextView tv_food_desc;
    private TextView tv_food_place;
    private Button btn_msg;
    private Button btn_get;

    private AlertDialog dialog_get;

    Food food;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_get);

        food_id = getIntent().getStringExtra(FOOD_ID);

        initView();
        initDialog();
    }

    private void initView() {
        mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_avatar = findViewById(R.id.iv_avatar);
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_time = findViewById(R.id.tv_time);
        iv_food = findViewById(R.id.iv_food);
        tv_food_name = findViewById(R.id.tv_food_name);
        tv_food_desc = findViewById(R.id.tv_food_desc);
        tv_food_place = findViewById(R.id.tv_food_place);
        btn_msg = findViewById(R.id.btn_msg);
        btn_get = findViewById(R.id.btn_get);


        LCQuery<Food> queryFood = LCObject.getQuery(Food.class);
        queryFood.whereEqualTo("objectId", food_id);
        queryFood.findInBackground().subscribe(new Observer<List<Food>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Food> foodList) {
                food = foodList.get(0);
                publisher = food.getPublisher();

                Glide.with(GetActivity.this)
                        .load(food.getFoodphoto())
                        .into(iv_food);
                tv_food_name.setText(food.getFoodname());
                tv_food_desc.setText(food.getFooddesc());
                tv_time.setText(food.getCreatedAtString());
                tv_food_place.setText(food.getPlace());
                LCQuery<User> queryUser = LCQuery.getQuery(User.class);
                queryUser.whereEqualTo("objectId", publisher);
                queryUser.findInBackground().subscribe(new Observer<List<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<User> users) {
                        User user = LCUser.cast(users.get(0), User.class);
                        Glide.with(GetActivity.this)
                                .load(user.getAvatar())
                                .into(iv_avatar);
                        tv_nickname.setText(user.getNickname());
                    }

                    @Override
                    public void onError(Throwable e) {
                        ShowToast.showLongToast(GetActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                ShowToast.showLongToast(GetActivity.this, e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });


        btn_msg.setOnClickListener(this);
        btn_get.setOnClickListener(this);


    }

    private void initDialog() {
        View dialogGet = LayoutInflater.from(this).inflate(R.layout.dialog_get, null);
        dialog_get = new AlertDialog.Builder(this)
                .setView(dialogGet)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        food.setReceiver(mCurrentUser.getObjectId());
                        food.setState(1);
                        food.saveInBackground().subscribe(new Observer<LCObject>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(LCObject lcObject) {
                                finish();
                                if (food.getState() == 0) {
                                    ShowToast.showShortToast(GetActivity.this, "申请成功，请去取货地点获取食物");
                                    Map<String, Object> data = new HashMap<String, Object>();
                                    data.put("type", "Status");
                                    data.put(Config.GETAVATAR, mCurrentUser.getAvatar());
                                    data.put(Config.GETNICKNAME, mCurrentUser.getNickname());
                                    data.put(Config.GETFOODNAME, food.getFoodname());
                                    data.put(Config.GETFOODPHOTO, food.getFoodphoto());
                                    data.put(Config.GETFOODTIME, food.getCreatedAtString());
                                    LCStatus status = LCStatus.createStatusWithData(data);
                                    status.sendPrivatelyInBackground(food.getPublisher()).subscribe(new Observer<LCStatus>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onNext(LCStatus lcStatus) {
                                            Log.i("GetActivity", "发送成功！");
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            ShowToast.showShortToast(GetActivity.this, e.getMessage());
                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });
                                } else {
                                    ShowToast.showShortToast(GetActivity.this, "该食物已被取走，再看看别的吧！");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                ShowToast.showShortToast(GetActivity.this, e.getMessage());
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                    }
                })
                .setNegativeButton("取消", null)
                .create();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_msg:
                Intent intent = new Intent(this, LCIMConversationActivity.class);
                intent.putExtra(LCIMConstants.PEER_ID, publisher);
                startActivity(intent);
                break;
            case R.id.btn_get:
                dialog_get.show();
                break;
        }

    }
}
