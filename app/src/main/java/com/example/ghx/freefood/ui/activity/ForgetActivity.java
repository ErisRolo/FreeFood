package com.example.ghx.freefood.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ghx.freefood.R;
import com.example.ghx.freefood.bean.User;
import com.example.ghx.freefood.utils.ShowToast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.leancloud.types.LCNull;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ghx on 2021/6/8.
 * 忘记密码
 */

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;

    private EditText et_forget_email;
    private Button btn_send;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

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

        et_forget_email = findViewById(R.id.et_forget_email);
        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                String email = et_forget_email.getText().toString().trim();
                if (!TextUtils.isEmpty(email)) {
                    User.requestPasswordResetInBackground(email).subscribe(new Observer<LCNull>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(LCNull lcNull) {
                            ShowToast.showShortToast(ForgetActivity.this, "邮件已发送，请查收并设定新密码！");
                            finish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            ShowToast.showShortToast(ForgetActivity.this, e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                } else {
                    ShowToast.showShortToast(ForgetActivity.this, "输入框不能为空！");
                }
                break;
        }
    }
}
