package com.example.ghx.freefood.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ghx.freefood.MainActivity;
import com.example.ghx.freefood.R;
import com.example.ghx.freefood.base.BaseActivity;
import com.example.ghx.freefood.bean.User;
import com.example.ghx.freefood.utils.FileUtils;
import com.example.ghx.freefood.utils.ShowToast;
import com.google.android.material.snackbar.Snackbar;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.FileNotFoundException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import cn.leancloud.LCFile;
import cn.leancloud.LCObject;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ghx on 2021/6/21.
 * 编辑个人资料
 */

public class MyUserActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;

    private CircleImageView iv_avatar;//修改头像
    private ImageView iv_modify_nickname;//修改昵称
    private ImageView iv_modify_desc;//修改简介

    private LinearLayout ll_exit_user;//退出登录

    private TextView tv_nickname;//昵称
    private TextView tv_desc;//简介

    private AlertDialog dialog_nickname;//修改昵称对话框
    private AlertDialog dialog_desc;//修改简介对话框
    private EditText et_nickname;
    private EditText et_desc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_user);
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
        iv_modify_nickname = findViewById(R.id.iv_modify_nickname);
        iv_modify_desc = findViewById(R.id.iv_modify_desc);
        ll_exit_user = findViewById(R.id.ll_exit_user);

        iv_avatar.setOnClickListener(this);
        iv_modify_nickname.setOnClickListener(this);
        iv_modify_desc.setOnClickListener(this);
        ll_exit_user.setOnClickListener(this);

        tv_nickname = findViewById(R.id.tv_nickname);
        tv_desc = findViewById(R.id.tv_desc);

        if (mCurrentUser != null) {

            String url = mCurrentUser.getAvatar();
            if (url != null) {
                Glide.with(this).load(url).into(iv_avatar);
            }

            tv_nickname.setText(mCurrentUser.getNickname());
            tv_desc.setText(mCurrentUser.getDesc());
        }

    }

    private void initDialog() {
        View dialogNickname = LayoutInflater.from(this).inflate(R.layout.dialog_modify_nickname, null);
        et_nickname = dialogNickname.findViewById(R.id.et_nickname);
        dialog_nickname = new AlertDialog.Builder(this)
                .setView(dialogNickname)
                .setTitle("修改昵称")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mCurrentUser.setNickname(et_nickname.getText().toString());
                        mCurrentUser.saveInBackground().subscribe(new Observer<LCObject>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(LCObject lcObject) {
                                tv_nickname.setText(et_nickname.getText().toString());
                                ShowToast.showShortToast(MyUserActivity.this, "修改成功");
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                    }
                }).setNegativeButton("取消", null)
                .create();


        View dialogDesc = LayoutInflater.from(this).inflate(R.layout.dialog_modify_desc, null);
        et_desc = (EditText) dialogDesc.findViewById(R.id.et_desc);
        dialog_desc = new AlertDialog.Builder(this)
                .setView(dialogDesc)
                .setTitle("修改简介")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mCurrentUser.setDesc(et_desc.getText().toString());
                        mCurrentUser.saveInBackground().subscribe(new Observer<LCObject>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(LCObject lcObject) {
                                tv_desc.setText(et_desc.getText().toString());
                                ShowToast.showShortToast(MyUserActivity.this, "修改成功");
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                    }
                }).setNegativeButton("取消", null)
                .create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_avatar:
                Matisse.from(this)
                        .choose(MimeType.ofAll())
                        .theme(R.style.Matisse_Dracula)
                        .imageEngine(new GlideEngine())
                        .countable(false)
                        .maxSelectable(1)
                        .forResult(0);
                break;
            case R.id.iv_modify_nickname:
                dialog_nickname.show();
                break;
            case R.id.iv_modify_desc:
                dialog_desc.show();
                break;
            case R.id.ll_exit_user:
                User.logOut();
                startActivity(new Intent(MyUserActivity.this, LoginActivity.class));
                MyUserActivity.this.finish();
                MainActivity.instance.finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Uri uri = Matisse.obtainResult(data).get(0);
            if (uri != null) {
                Glide.with(this)
                        .load(uri)
                        .into(iv_avatar);

                String path = FileUtils.getFilePahtFromUri(this, uri);

                if (path != null) {
                    try {
                        LCFile file = LCFile.withAbsoluteLocalPath(FileUtils.getFileName(path), path);
                        file.saveInBackground().subscribe(new Observer<LCFile>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(LCFile lcFile) {
                                mCurrentUser.setAvatar(file.getUrl());
                                mCurrentUser.saveInBackground();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Snackbar.make(iv_avatar, "上传头像失败", Snackbar.LENGTH_LONG).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}
