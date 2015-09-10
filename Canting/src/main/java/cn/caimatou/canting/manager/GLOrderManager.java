package cn.caimatou.canting.manager;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import cn.caimatou.canting.bean.Order;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.utils.GLListUtil;

/**
 * Description：订单管理
 * <br/><br/>Created by Fausgoal on 15/9/9.
 * <br/><br/>
 */
public class GLOrderManager {
    private GLOrderManager() {
    }

    public static List<Order> getOrders(Context context) {
        long userId = GLUserManager.getUserId(context);
        try {
            return (List<Order>) GLDatabaseManager.queryOrder(context, new String[]{"userId"}, new Object[]{userId});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Order getOrder(Context context, long orderId) {
        long userId = GLUserManager.getUserId(context);
        try {
            List<Order> orders = (List<Order>) GLDatabaseManager.queryOrder(context, new String[]{"userId", "orderId"}, new Object[]{userId, orderId});
            if (!GLListUtil.isEmpty(orders)) {
                return orders.get(GLConst.NONE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveOrder(Context context, Order order) {
        if (null == order) {
            return;
        }

        Order locOrder = getOrder(context, order.getOrderId());
        if (null != locOrder) {
            order.set_id(locOrder.get_id());
        }

        try {
            GLDatabaseManager.replaceOrder(context, order);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveOrders(Context context, List<Order> orders) {
        if (GLListUtil.isEmpty(orders)) {
            return;
        }
        for (Order order : orders) {
            saveOrder(context, order);
        }
    }

    public static void delOrder(Context context, Order order) {
        if (null == order) {
            return;
        }
        try {
            GLDatabaseManager.deleteOrder(context, "orderId", order.getOrderId() + "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delAllOrders(Context context) {
        long userId = GLUserManager.getUserId(context);
        try {
            GLDatabaseManager.deleteOrder(context, "userId", userId + "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
