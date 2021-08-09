package com.example.ghx.freefood.bean;

/**
 * Created by ghx on 2021/6/29.
 * 请求获得食物时发送的动态实体类
 */

public class Status {

    private String getId;//获得者ID
    private String avatar;//获得者头像
    private String nickname;//获得者昵称
    private String foodphoto;//食物图片
    private String foodname;//食物名称
    private String publishtime;//发布时间

    public String getGetId() {
        return getId;
    }

    public void setGetId(String getId) {
        this.getId = getId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFoodphoto() {
        return foodphoto;
    }

    public void setFoodphoto(String foodphoto) {
        this.foodphoto = foodphoto;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(String publishtime) {
        this.publishtime = publishtime;
    }
}
