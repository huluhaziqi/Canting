/**
 *
 */
package cn.caimatou.canting.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import cn.caimatou.canting.common.GLConst;


/**
 * px to dp or dp to px
 * 手机尺寸、应用版本等工具类
 * Created by Fausgoal on 2015/5/5.
 */
public class GLPixelsUtil {
    private static final String TAG = "GLPixelsUtil";

    /**
     * 根据手机的分辨率从dp 的单位 转成为px(像素)
     *
     * @param context context
     * @param dp      dp
     * @return px
     */
    public static int dip2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从px(像素) 的单位 转成为dp
     *
     * @param context context
     * @param px      px
     * @return dp
     */
    public static int px2dip(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context context
     * @param px      px
     * @return sp
     */
    public static int px2sp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context context
     * @param sp      sp
     * @return px
     */
    public static int sp2px(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    /**
     * 获取设备唯一标示
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        return deviceId;
    }

    /**
     * 获取设备的生成厂家、机型及系统的版本
     *
     * @return
     */
    public static String getDeviceModelAndVersion() {
        int sdkVer = Build.VERSION.SDK_INT;
        String release = Build.VERSION.RELEASE;// 固件版本
        String manufacture = Build.MANUFACTURER;// 生产厂家
        String model = Build.MODEL;// 机型

        String strInfo = manufacture + "," + model + "," + release;
        GLLog.trace(TAG, strInfo);
        strInfo = manufacture + "," + model;

        return strInfo;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), GLConst.ONE);
            if (null != packageInfo) {
                return packageInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            GLLog.trace(TAG, "getVersionName 异常");
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取versionCode
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), GLConst.ONE);
            int version = packageInfo.versionCode;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            GLLog.trace(TAG, "getVersionName 异常");
            e.printStackTrace();
        }
        return GLConst.ONE;
    }

    /**
     * 获取手机屏幕的分辨率
     *
     * @param context context
     * @return Point x 宽度， y 高度
     */
    public static Point getDeviceSize(Context context) {
        if (null == context)
            return null;

        if (!(context instanceof Activity))
            return null;

        Activity activity;
        try {
            activity = (Activity) context;
        } catch (Exception e) {
            activity = null;
        }

        if (null == activity)
            return null;

        int screenWidth;
        int screenHeight;
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= 17) {
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(dm);
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels;
        } else {
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels;
        }

        Point point = new Point();
        point.x = screenWidth;
        point.y = screenHeight;
        return point;
    }
}
