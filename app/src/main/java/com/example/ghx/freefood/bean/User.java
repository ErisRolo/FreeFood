package com.example.ghx.freefood.bean;

import com.example.ghx.freefood.utils.Config;

import cn.leancloud.LCUser;
import cn.leancloud.annotation.LCClassName;

/**
 * Created by ghx on 2021/6/8.
 * 子类化User
 */
@LCClassName("User")
public class User extends LCUser {

//    private String avatar;//头像
//    private String nickname;//昵称
//    private String desc;//简介

    public String getAvatar() {
        return this.getString(Config.AVATAR);
    }

    public void setAvatar(String avatar) {
        this.put(Config.AVATAR, avatar);
    }

    public String getNickname() {
        return this.getString(Config.NICKNAME);
    }

    public void setNickname(String nickname) {
        this.put(Config.NICKNAME, nickname);
    }

    public String getDesc() {
        return this.getString(Config.DESC);
    }

    public void setDesc(String desc) {
        this.put(Config.DESC, desc);
    }

    public int getScore1() {
        return this.getInt(Config.SCORE1);
    }

    public void setScore1(int score1) {
        this.put(Config.SCORE1, score1);
    }

    public int getScore2() {
        return this.getInt(Config.SCORE2);
    }

    public void setScore2(int score2) {
        this.put(Config.SCORE2, score2);
    }


}
