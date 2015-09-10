package cn.caimatou.canting.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cn.caimatou.canting.bean.GLLocation;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.manager.GLLocationManager;
import cn.caimatou.canting.utils.GLBDLocationUtil;
import cn.caimatou.canting.utils.GLLog;

/**
 * Description：处理一些全局的任务服务
 * <br/><br/>Created by Fausgoal on 15/9/6.
 * <br/><br/>
 */
public class GLGlobalService extends Service implements GLBDLocationUtil.BDLocationCallbackListener {
    private static final String TAG = "GLGlobalService";

    private Context mContext = null;
    private GLBDLocationUtil mBDLocation = null;

    private double mLatitude = GLConst.NEGATIVE;
    private double mLongitude = GLConst.NEGATIVE;
    private GLLocation mLocation = null;

    /**
     * 启动服务时定位一次
     */
    private boolean isFirst = false;

    @Override
    public void onCreate() {
        super.onCreate();

        isFirst = true;
        mContext = this;
        initLocation();
    }

    private void initLocation() {
        mBDLocation = new GLBDLocationUtil(mContext);
        mBDLocation.setLocationCallbackListener(this);
        mBDLocation.setTranToRealyGPS(false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isFirst) {
            // 第一次启动服务都要进行定位
            isFirst = false;
            // 进行定位
            mBDLocation.startLocation();
        } else {
            // 先判断如果没有经纬度，都进行定位
            if (mLatitude == GLConst.NEGATIVE
                    || mLongitude == GLConst.NEGATIVE
                    || null == mLocation) {
                mLocation = GLLocationManager.getLocation();
                if (null == mLocation) {
                    // 进行定位
                    mBDLocation.startLocation();
                } else {
                    mLongitude = mLocation.getLongitude();
                    mLatitude = mLocation.getLatitude();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBDLocation.stopLocation();
    }

    @Override
    public void onSuccess(double latitude, double longitude, String city) {
        // 定位成功
        mLatitude = latitude;
        mLongitude = longitude;
        if (null == mLocation) {
            mLocation = new GLLocation(city, latitude, longitude);
        } else {
            mLocation.setCity(city);
            mLocation.setLatitude(latitude);
            mLocation.setLongitude(longitude);
        }
        GLLocationManager.saveLocation(mLocation);
        GLLog.trace(TAG, "定位成功：city：" + city + "，latitude：" + latitude + "，longitude：" + longitude);
    }

    @Override
    public void onFailed(String errorMsg) {
        GLLog.trace(TAG, errorMsg);
    }
}
