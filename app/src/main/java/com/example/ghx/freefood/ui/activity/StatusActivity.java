package com.example.ghx.freefood.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.example.ghx.freefood.R;
import com.example.ghx.freefood.adapter.StatusAdapter;
import com.example.ghx.freefood.base.BaseActivity;
import com.example.ghx.freefood.bean.Status;
import com.example.ghx.freefood.utils.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.leancloud.LCStatus;
import cn.leancloud.LCStatusQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ghx on 2021/6/29.
 * 状态通知
 */

public class StatusActivity extends BaseActivity {

    private Toolbar mToolbar;

    private RecyclerView rv_status;
    private LinearLayoutManager mLayoutManager;
    private StatusAdapter mAdapter;
    private List<Status> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_status);
        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        rv_status = findViewById(R.id.rv_status);
        mLayoutManager = new LinearLayoutManager(this);
        rv_status.setLayoutManager(mLayoutManager);
        setupAdapter();
    }

    private void setupAdapter() {
        LCStatusQuery indexQuery = LCStatus.inboxQuery(mCurrentUser, LCStatus.INBOX_TYPE.PRIVATE.toString());
        indexQuery.setLimit(50);
        indexQuery.findInBackground().subscribe(new Observer<List<LCStatus>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<LCStatus> list) {
                if (list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        LCStatus lcStatus = list.get(i);
                        Map<String, Object> data = lcStatus.getServerData();
                        if (((String) data.get("type")).equals("Status")) {
                            Status status = new Status();
                            status.setAvatar((String) data.get(Config.GETAVATAR));
                            status.setNickname((String) data.get(Config.GETNICKNAME));
                            status.setFoodname((String) data.get(Config.GETFOODNAME));
                            status.setFoodphoto((String) data.get(Config.GETFOODPHOTO));
                            status.setPublishtime((String) data.get(Config.GETFOODTIME));
                            mList.add(status);
                        }
                    }
                    mAdapter = new StatusAdapter(StatusActivity.this, mList);
                    rv_status.setAdapter(mAdapter);
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
