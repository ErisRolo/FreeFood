package com.example.ghx.freefood.base;

import android.os.Bundle;

import com.example.ghx.freefood.bean.User;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by ghx on 2021/6/7.
 * Activity基类
 */

public class BaseActivity extends AppCompatActivity {

    protected User mCurrentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentUser = User.getCurrentUser(User.class);
    }
}
