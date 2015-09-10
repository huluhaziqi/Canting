package cn.caimatou.canting.utils;


import android.text.TextUtils;

import cn.caimatou.canting.common.GLConst;


/**
 * Description：数据转换处理工具类
 * <br/><br/>Created by Fausgoal on 2015/5/12.
 * <br/><br/>
 */
public class GLNumberUtils {
    public static int floatToInt(float fl) {
        try {
            return (int) fl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GLConst.NONE;
    }

    public static double stringToDouble(String str) {
        if (TextUtils.isEmpty(str))
            return GLConst.NONE;
        try {
            return Double.valueOf(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GLConst.NONE;
    }

    public static long stringToLong(String str) {
        if (TextUtils.isEmpty(str))
            return GLConst.NONE;
        try {
            return Long.valueOf(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GLConst.NONE;
    }

    public static int stringToInt(String str) {
        if (TextUtils.isEmpty(str))
            return GLConst.NONE;
        try {
            return Integer.valueOf(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GLConst.NONE;
    }

    /**
     * 根据显示的数据条数，及数据总数计算分页多少页
     *
     * @param loadDataCount  每页显示数据的条数
     * @param totalDataCount 数据总数
     * @return 分页多少页
     */
    public static long getTotalPageCount(long loadDataCount, long totalDataCount) {
        if (loadDataCount <= GLConst.NONE) {
            loadDataCount = GLConst.ONE;
        }

        long totalPageCount = totalDataCount / loadDataCount;

        if (totalDataCount % loadDataCount != GLConst.NONE) {
            totalPageCount += GLConst.ONE;
        }

        if (totalPageCount <= GLConst.NONE) {
            totalPageCount = GLConst.ONE;
        }
        return totalPageCount;
    }
}
