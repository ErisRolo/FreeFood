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
import cn.leancloud.LCUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ghx on 2021/6/8.
 * 注册
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;

    private EditText et_register_username;
    private EditText et_register_password;
    private EditText et_register_pass;
    private EditText et_register_email;
    private Button btn_registered;
    private Button btn_reset;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        et_register_username = findViewById(R.id.et_register_username);
        et_register_password = findViewById(R.id.et_register_password);
        et_register_pass = findViewById(R.id.et_register_pass);
        et_register_email = findViewById(R.id.et_register_email);
        btn_registered = findViewById(R.id.btn_registered);
        btn_registered.setOnClickListener(this);
        btn_reset = findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_registered:
                String register_username = et_register_username.getText().toString().trim();
                String register_password = et_register_password.getText().toString().trim();
                String register_pass = et_register_pass.getText().toString().trim();
                String register_email = et_register_email.getText().toString().trim();

                if (!TextUtils.isEmpty(register_username) && !TextUtils.isEmpty(register_password)
                        && !TextUtils.isEmpty(register_pass) && !TextUtils.isEmpty(register_email)) {
                    if (register_password.equals(register_pass)) {

                        final User user = new User();
                        user.setUsername(register_username);
                        user.setPassword(register_password);
                        user.setEmail(register_email);
                        user.setNickname("未命名用户");
                        user.setDesc("这个人很懒，什么都没有留下。");
                        user.setScore1(0);
                        user.setScore2(0);
                        user.signUpInBackground().subscribe(new Observer<LCUser>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(LCUser lcUser) {
                                ShowToast.showShortToast(RegisterActivity.this, "注册成功，请前往邮箱激活账号！");
                                finish();
                            }

                            @Override
                            public void onError(Throwable e) {
                                ShowToast.showShortToast(RegisterActivity.this, e.getMessage());
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                    } else {
                        ShowToast.showShortToast(this, "两次输入密码不一致！");
                    }
                } else {
                    ShowToast.showShortToast(this, "输入框不能为空！");
                }
                break;
            case R.id.btn_reset:
                et_register_username.setText("");
                et_register_password.setText("");
                et_register_pass.setText("");
                et_register_email.setText("");
                break;
        }
    }
}
