package com.example.ghx.freefood.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.example.ghx.freefood.R;
import com.example.ghx.freefood.adapter.WasteAdapter;
import com.example.ghx.freefood.base.BaseActivity;
import com.example.ghx.freefood.bean.Food;
import com.example.ghx.freefood.utils.Config;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ghx on 2021/6/21.
 * 浪费的食物列表
 */

public class WasteFoodActivity extends BaseActivity {

    private Toolbar mToolbar;

    private RecyclerView rv_waste_food;
    private LinearLayoutManager mLayoutManager;
    private WasteAdapter mAdapter;
    private List<Food> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_waste_food);
        initView();
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

        rv_waste_food = findViewById(R.id.rv_waste_food);
        mLayoutManager = new LinearLayoutManager(this);
        rv_waste_food.setLayoutManager(mLayoutManager);
        setWasteFoodAdapter();

    }

    private void setWasteFoodAdapter() {
        LCQuery<Food> query = LCObject.getQuery(Food.class);
        query.whereEqualTo(Config.PUBLISHER, mCurrentUser.getObjectId());
        query.whereEqualTo(Config.STATE, 3);
        query.findInBackground().subscribe(new Observer<List<Food>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Food> foodList) {
                if (foodList.size() != 0) {
                    mList.clear();
                    mList.addAll(foodList);
                    mAdapter = new WasteAdapter(WasteFoodActivity.this, mList);
                    rv_waste_food.setAdapter(mAdapter);
                } else {
                    rv_waste_food.removeAllViews();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
