package cn.caimatou.canting.bean;

import java.io.Serializable;

/**
 * Description：经纬度等位置的实体
 * <br/><br/>Created by Fausgoal on 15/9/6.
 * <br/><br/>
 */
public class GLLocation implements Serializable {
    private String mCity;
    private double mLatitude;
    private double mLongitude;

    public GLLocation() {
    }

    public GLLocation(String city, double latitude, double longitude) {
        this.mCity = city;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }
}
