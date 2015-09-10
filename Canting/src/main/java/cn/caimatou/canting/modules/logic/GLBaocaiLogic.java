package cn.caimatou.canting.modules.logic;

import android.content.Context;

import cn.caimatou.canting.R;
import cn.caimatou.canting.utils.GLResourcesUtil;

/**
 * Description：报菜逻辑类
 * <br/><br/>Created by Fausgoal on 15/8/30.
 * <br/><br/>
 */
public class GLBaocaiLogic {

    public static final int REFRESH_SUCCESS = 0;
    public static final int LOADED_SUCCESS = 1;

    /**
     * 从订单里进入下单界面
     */
    public static final int ENTER_ORDER_FOR_MY_ORDER = 1001;
    /**
     * 从供应商信息中进入下单界面
     */
    public static final int ENTER_ORDER_FOR_PROVIDER = 1002;

    private final Context mContext;

    public GLBaocaiLogic(Context context) {
        mContext = context;
    }

    public String getBaocaiState(int state) {
        String strState = "";
        switch (state) {
            case 0:
                strState = GLResourcesUtil.getString(R.string.it_has_orders);
                break;
            case 1:
                strState = GLResourcesUtil.getString(R.string.pending);
                break;
            case 2:
                strState = GLResourcesUtil.getString(R.string.is_canceled);
                break;
            case 3:
                strState = GLResourcesUtil.getString(R.string.is_completed);
                break;
            default:
                break;
        }
        return strState;
    }

    public int getBaocaiStateColor(int state) {
        int resId = 0;
        switch (state) {
            case 0: // 已接单
                resId = GLResourcesUtil.getColor(R.color.green);
                break;
            case 1: // 待处理
                resId = GLResourcesUtil.getColor(R.color.oranger);
                break;
            case 2: //已取消
                resId = GLResourcesUtil.getColor(R.color.red);
                break;
            case 3:
                resId = GLResourcesUtil.getColor(R.color.gray1);
                break;
            default:
                break;
        }
        return resId;
    }
}
