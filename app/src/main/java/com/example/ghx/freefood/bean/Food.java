package com.example.ghx.freefood.bean;

import android.os.Parcelable;

import com.example.ghx.freefood.utils.Config;

import cn.leancloud.LCObject;
import cn.leancloud.LCParcelableObject;
import cn.leancloud.annotation.LCClassName;
import cn.leancloud.types.LCGeoPoint;

/**
 * Created by ghx on 2021/6/18.
 * 子类化食物
 */

@LCClassName("Food")
public class Food extends LCObject {

    public static final Parcelable.Creator CREATOR = LCParcelableObject.LCObjectCreator.instance;

    public Food() {

    }


    public String getFoodname() {
        return getString(Config.FOODNAME);
    }

    public void setFoodname(String foodname) {
        put(Config.FOODNAME, foodname);
    }

    public String getFooddesc() {
        return getString(Config.FOODDESC);
    }

    public void setFooddesc(String fooddesc) {
        put(Config.FOODDESC, fooddesc);
    }

    public String getFoodphoto() {
        return getString(Config.FOODPHOTO);
    }

    public void setFoodphoto(String foodphoto) {
        put(Config.FOODPHOTO, foodphoto);
    }

    public double getFoodweight() {
        return getDouble(Config.FOODWEIGHT);
    }

    public void setFoodweight(double weight) {
        put(Config.FOODWEIGHT, weight);
    }

    public String getFoodtype() {
        return getString(Config.FOODTYPE);
    }

    public void setFoodtype(String type) {
        put(Config.FOODTYPE, type);
    }

    public double getFootprint1() {
        return getDouble(Config.FOOTPRINT1);
    }

    public void setFootprint1(double footprint1) {
        put(Config.FOOTPRINT1, footprint1);
    }

    public double getDistance() {
        return getDouble(Config.DISTANCE);
    }

    public void setDistance(double distance) {
        put(Config.DISTANCE, distance);
    }

    public double getFootprint2() {
        return getDouble(Config.FOOTPRINT2);
    }

    public void setFootprint2(double footprint2) {
        put(Config.FOOTPRINT2, footprint2);
    }

    public double getFootprint() {
        return getDouble(Config.FOOTPRINT);
    }

    public void setFootprint(double footprint) {
        put(Config.FOOTPRINT, footprint);
    }

    public String getPlace() {
        return getString(Config.PLACE);
    }

    public void setPlace(String place) {
        put(Config.PLACE, place);
    }

    public LCGeoPoint getWhereCreated() {
        return getLCGeoPoint(Config.WHERECREATED);
    }

    public void setWhereCreated(LCGeoPoint point) {
        put(Config.WHERECREATED, point);
    }

    public int getState() {
        return getInt(Config.STATE);
    }

    public void setState(int state) {
        put(Config.STATE, state);
    }

    public String getPublisher() {
        return getString(Config.PUBLISHER);
    }

    public void setPublisher(String publisher) {
        put(Config.PUBLISHER, publisher);
    }

    public String getReceiver() {
        return getString(Config.RECEIVER);
    }

    public void setReceiver(String receiver) {
        put(Config.RECEIVER, receiver);
    }


}
