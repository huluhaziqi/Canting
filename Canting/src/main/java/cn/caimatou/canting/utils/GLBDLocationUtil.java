package cn.caimatou.canting.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.NumberFormat;

/**
 * Description：百度定位api
 * <br/><br/>Created by Fausgoal on 15/5/10.
 * <br/><br/>
 */
public class GLBDLocationUtil implements BDLocationListener {
    private static final String TAG = "GLBDLocationUtil";

    /**
     * 百度GPS转换的API地址，x为经度，y为纬度
     */
    private static final String MAP_API_BASE64_ENCODE = "http://api.map.baidu.com/ag/coord/convert?from=0&to=4&x=%1$s&y=%2$s";

    private static final int GET_SUCCESS = 0;
    private static final int GET_GPS_FAILED = 1;

    public LocationClient mLocationClient = null;
    private BDLocationCallbackListener mCallbackListener = null;

    private double mGpsLat = -1;
    private double mGpsLon = -1;
    private String mCity = null;

    private boolean isTranToRealyGPS = true;

    /**
     * 设置回调
     *
     * @param callback
     */
    public void setLocationCallbackListener(BDLocationCallbackListener callback) {
        this.mCallbackListener = callback;
    }

    /**
     * 是否将GPS转为真实的GPS
     *
     * @param isTranToRealyGPS true 转为真实的GPS，默认值
     */
    public void setTranToRealyGPS(boolean isTranToRealyGPS) {
        this.isTranToRealyGPS = isTranToRealyGPS;
    }

    public GLBDLocationUtil(Context context) {
        initLocation(context);
    }

    /**
     * 在Application中初始化
     *
     * @param context 为getApplicationContext();
     */
    private void initLocation(Context context) {
        mLocationClient = new LocationClient(context);
    }

    private void setLocOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setScanSpan(1000);// 设置发起定位请求的间隔时间为1000 ms
        option.setIsNeedAddress(true);
        /**
         * 默认值gcj02 1、bd09ll 返回是百度加密经纬度坐标 2、bd09 返回是百度加密墨卡托经纬度坐标 3、gcj02
         * 返回是国测局加密经纬度坐标
         *
         * 这里注意，如果不使用bd09ll百度加密纬度坐标，反转成真实GPS时会出现错误，只有为bd09ll百度加密纬度坐标时，
         * 转成的GPS坐标为真实的
         * */
        option.setCoorType("bd09ll");
        mLocationClient.setLocOption(option);
    }

    /**
     * 开始定位
     */
    public void startLocation() {
        mLocationClient.registerLocationListener(this);
        setLocOption();
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        mLocationClient.requestLocation();
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        if (mLocationClient != null) {
            if (mLocationClient.isStarted()) {
                mLocationClient.stop();
            }
            mLocationClient.unRegisterLocationListener(this);
        }
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (null == location)
            return;

        int locType = location.getLocType();
        final double latitude = location.getLatitude();
        final double longitude = location.getLongitude();
        float radius = location.getRadius();

        String city = location.getCity();
        mCity = city;

        String strMsg = "error code is " + locType;
        strMsg += ",\r\nlatitude：" + latitude;
        strMsg += ",longitude：" + longitude;
        strMsg += ",\r\nradius is " + radius;
        strMsg += ", city is " + city;

        boolean isStop;
        if (locType == BDLocation.TypeGpsLocation) {
            float speed = location.getSpeed();
            int statelliteNumber = location.getSatelliteNumber();

            strMsg += ",\r\nspeed is " + speed;
            strMsg += ",\r\nstatelliteNumber is " + statelliteNumber;

            isStop = true;
        } else if (locType == BDLocation.TypeNetWorkLocation) {
            String strAddrStr = location.getAddrStr();
            strMsg += ",\r\nstrAddrStr is " + strAddrStr;

            isStop = true;
        } else {
            isStop = locType == BDLocation.TypeOffLineLocation;
        }

        strMsg = "获取的位置是-->" + strMsg;

        GLLog.trace(TAG, strMsg);

        // 定位成功了，去停止定位服务
        stopLocation();

        if (isStop) {
            if (isTranToRealyGPS) {
                // 将百度GPS转为真实的GPS
                tran(latitude, longitude);
            } else {
                mGpsLat = latitude;
                mGpsLon = longitude;
                onSuccess();
            }
        } else {
            // 获取error code：
            // 61 ： GPS定位结果
            // 62 ： 扫描整合定位依据失败。此时定位结果无效。
            // 63 ： 网络异常，没有成功向服务器发起请求。此时定位结果无效。
            // 65 ： 定位缓存的结果。
            // 66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果
            // 67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果
            // 68 ： 网络连接失败时，查找本地离线定位时对应的返回结果
            // 161： 表示网络定位结果
            // 162~167： 服务端定位失败
            // 502：key参数错误
            // 505：key不存在或者非法
            // 601：key服务被开发者自己禁用
            // 602：key mcode不匹配
            // 501～700：key验证失败
            String msg = "定位失败";
            onFailed(msg);
        }
    }

    /**
     * Description: tran <br>
     * Comments: 将百度经纬度坐标转为真实的GPS坐标
     *
     * @param baiduLat 百度获取的纬度
     * @param baiduLon 百度获取的经度
     * @author FU ZHIXUE
     */
    private void tran(final double baiduLat, final double baiduLon) {
        // 这里起个线程去解GPS
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = String.format(MAP_API_BASE64_ENCODE, String.valueOf(baiduLon), String.valueOf(baiduLat));

                String json = getHttpValues(url);
                // 如果json为空
                if (TextUtils.isEmpty(json)) {
                    mHandler.sendEmptyMessage(GET_GPS_FAILED);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        int error = jsonObject.getInt("error");
                        if (error == 0) {
                            String x = jsonObject.getString("x");
                            String y = jsonObject.getString("y");

                            double lon1 = Double.parseDouble(Base64.decode2String(x));
                            double lat1 = Double.parseDouble(Base64.decode2String(y));

                            // 这里的就是GPS坐标 经过下面算法转换基本上与真实的GPS坐标相差无几
                            double gpsLat = baiduLat * 2 - lat1;
                            double gpsLon = baiduLon * 2 - lon1;

                            // 保留6位小数
                            NumberFormat numFormat = NumberFormat.getNumberInstance();
                            numFormat.setMaximumFractionDigits(6);
                            String strGpsLat = numFormat.format(gpsLat);
                            String strGpsLon = numFormat.format(gpsLon);
                            gpsLat = Double.parseDouble(strGpsLat);
                            gpsLon = Double.parseDouble(strGpsLon);

                            mGpsLat = gpsLat;
                            mGpsLon = gpsLon;

                            GLLog.trace(TAG, "转换前的GPS纬度为:" + baiduLat + "，经度为:" + baiduLon);
                            GLLog.trace(TAG, "转换后的真实GPS纬度为:" + gpsLat + "，经度为:" + gpsLon);

                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_SUCCESS;
                            mHandler.sendMessage(msg);
                        }

                    } catch (JSONException e) {
                        mHandler.sendEmptyMessage(GET_GPS_FAILED);
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 通过url地址取得网络上的内容
     *
     * @param url
     * @return
     */
    public static String getHttpValues(String url) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        StringBuffer strBuffer = new StringBuffer();
        try {
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            BufferedReader buffReader = new BufferedReader(
                    new InputStreamReader(entity.getContent()));
            String result;
            while ((result = buffReader.readLine()) != null) {
                strBuffer.append(result);
            }
        } catch (Exception e) {
            strBuffer = null;
            e.printStackTrace();
        } finally {
            post.abort();
        }
        if (null != strBuffer) {
            return strBuffer.toString();
        }
        return "";
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String errMsg;
            switch (msg.what) {
                case GET_GPS_FAILED:
                    errMsg = "定位失败";
                    onFailed(errMsg);
                    break;
                case GET_SUCCESS:
                    onSuccess();
                    break;
            }
        }
    };

    private void onFailed(String msg) {
        if (null != mCallbackListener) {
            mCallbackListener.onFailed(msg);
        }
    }

    private void onSuccess() {
        if (null != mCallbackListener) {
            mCallbackListener.onSuccess(mGpsLat, mGpsLon, mCity);
        }
    }

    public interface BDLocationCallbackListener {
        void onSuccess(double latitude, double longitude, String city);

        void onFailed(String errorMsg);
    }
}
