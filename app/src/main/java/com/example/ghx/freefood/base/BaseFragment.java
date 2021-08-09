package com.example.ghx.freefood.base;

import android.os.Bundle;

import com.example.ghx.freefood.bean.User;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by ghx on 2021/6/7.
 * Fragment基类
 */

public class BaseFragment extends Fragment {

    protected User mCurrentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentUser = User.getCurrentUser(User.class);

    }
}
