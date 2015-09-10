package cn.caimatou.canting.manager;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import cn.caimatou.canting.bean.GLRestaurant;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.utils.GLListUtil;

/**
 * Description：餐厅管理类
 * <br/><br/>Created by Fausgoal on 15/9/8.
 * <br/><br/>
 */
public class GLCantingManager {
    /**
     * 注册餐厅保存
     *
     * @param restaurant
     */
    public static void saveCantingInfo(Context context, GLRestaurant restaurant) {
        if (null == restaurant) {
            return;
        }

        GLRestaurant locRestaurant = getCantingInfo(context, restaurant.getId());
        if (null != locRestaurant) {
            restaurant.set_id(locRestaurant.get_id());
        }
        try {
            GLDatabaseManager.replaceRestaurant(context, restaurant);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static GLRestaurant getCantingInfo(Context context, long id) {
        try {
            List<GLRestaurant> infos = (List<GLRestaurant>) GLDatabaseManager.queryRestaurant(context, new String[]{"id"}, new Object[]{id});
            if (!GLListUtil.isEmpty(infos)) {
                return infos.get(GLConst.NONE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
