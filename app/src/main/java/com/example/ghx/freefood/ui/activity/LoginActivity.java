package com.example.ghx.freefood.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ghx.freefood.MainActivity;
import com.example.ghx.freefood.R;
import com.example.ghx.freefood.bean.User;
import com.example.ghx.freefood.utils.ShowToast;
import com.example.ghx.freefood.widget.CustomDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.leancloud.LCUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ghx on 2021/6/8.
 * 登录
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_login_username;
    private EditText et_login_password;
    private Button btn_login;
    private Button btn_register;
    private TextView tv_forget;

    private CustomDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        et_login_username = findViewById(R.id.et_login_username);
        et_login_password = findViewById(R.id.et_login_password);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        tv_forget = findViewById(R.id.tv_forget);
        tv_forget.setOnClickListener(this);

        //初始化dialog
        mDialog = new CustomDialog(this, 100, 100, R.layout.dialog_loading, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        //屏幕外点击无效
        mDialog.setCancelable(false);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forget:
                startActivity(new Intent(this, ForgetActivity.class));
                break;
            case R.id.btn_login:
                String login_username = et_login_username.getText().toString().trim();
                String login_password = et_login_password.getText().toString().trim();
                if (!TextUtils.isEmpty(login_username) && !TextUtils.isEmpty(login_password)) {

                    mDialog.show();

                    User.logIn(login_username, login_password).subscribe(new Observer<LCUser>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(LCUser lcUser) {
                            mDialog.dismiss();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mDialog.dismiss();
                            ShowToast.showShortToast(LoginActivity.this, e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

                } else {
                    ShowToast.showShortToast(this, "输入框不能为空！");
                }
                break;
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }
}
