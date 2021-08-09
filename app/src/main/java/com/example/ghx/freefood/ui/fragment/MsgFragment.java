package com.example.ghx.freefood.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.ghx.freefood.R;
import com.example.ghx.freefood.base.BaseFragment;
import com.example.ghx.freefood.ui.activity.StatusActivity;
import com.example.ghx.freefood.utils.FragmentUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import cn.leancloud.chatkit.activity.LCIMConversationListFragment;

/**
 * Created by ghx on 2021/6/8.
 * 消息和通知
 */

public class MsgFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout ll_notification;

    private FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg, null);
        fm = getChildFragmentManager();
        findView(view);
        return view;
    }

    private void findView(View view) {

        ll_notification = view.findViewById(R.id.ll_notification);
        ll_notification.setOnClickListener(this);

        FragmentUtils.addFragment(fm, R.id.fragment_container, new LCIMConversationListFragment());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_notification:
                startActivity(new Intent(getActivity(), StatusActivity.class));
                break;
        }
    }
}
