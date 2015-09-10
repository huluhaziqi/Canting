package cn.caimatou.canting.common;

import cn.caimatou.canting.BuildConfig;

/**
 * Description：常量类，存放全局常量，个别专属需求常量单独写类
 * <br/><br/>Created by Fausgoal on 2015/4/28.
 * <br/><br/>
 */
public final class GLConst {
    // TODO 正式版本，改为 false
    /**
     * 正式版本，改为false
     */
    public static final boolean IS_DEBUG = BuildConfig.IS_LOG;

    public static final int NEGATIVE = -1;
    public static final int NONE = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;

    public static final int PAGE_COUNT = 20;

    /**
     * 1s
     */
    public static final int ONE_SECOND = 1000;
    /**
     * 1m
     */
    public static final int ONE_MINUTE = 6 * ONE_SECOND;
    /**
     * 半小时
     */
    public static final int HALF_HOUR = 30 * ONE_MINUTE;
    /**
     * 1小时
     */
    public static final int ONE_HOUR = 60 * ONE_MINUTE;

    /**
     * 分页的条数
     */
    public static final int PAGE_SIZE = 20;

    public static final String INTENT_PARAM = "param-value";
    public static final String INTENT_PARAM1 = "param-value1";
    public static final String INTENT_PARAM2 = "param-value2";

    /**
     * 配送地址
     */
    public static final int REQUEST_DELIVERY_ADDRESS = 1001;
    /**
     * 管理配送地址
     */
    public static final int REQUEST_MANAGER_DELIVERY_ADDRESS = 1002;

    /**
     * 配送时间
     */
    public static final int DIALOG_DELIVERY_TIME = 1;
    /**
     * 菜品单位
     */
    public static final int DIALOG_UNIT = 2;

    /**
     * 撤消菜品
     */
    public static final int DIALOG_ORDER_CANCELED = 3;

    /**
     * 获取照片请求码
     */
    public static final int REQUEST_PHOTO = 100;
}
