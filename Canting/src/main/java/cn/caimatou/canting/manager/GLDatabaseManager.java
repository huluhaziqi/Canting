package cn.caimatou.canting.manager;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;

import cn.caimatou.canting.bean.Company;
import cn.caimatou.canting.bean.GLDeliveryAddressModel;
import cn.caimatou.canting.bean.GLRestaurant;
import cn.caimatou.canting.bean.GLUserInfo;
import cn.caimatou.canting.bean.Order;
import cn.caimatou.canting.database.GLDbHelper;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 2015/8/31.
 * <br/><br/>
 */
public class GLDatabaseManager {
    private GLDatabaseManager() {
    }

    public static GLDbHelper getDataHelper(Context context) {
        return OpenHelperManager.getHelper(context, GLDbHelper.class);
    }

    /**
     * query
     */
    private static List<?> query(Context context, Dao<?, Integer> dao,
                                 String[] column, Object[] value,
                                 String[] orderBy,
                                 Boolean[] ascending, int start, int length) {
        List<?> list = null;
        try {
            QueryBuilder<?, Integer> queryBuilder = dao.queryBuilder();

            // 组装where条件
            if (column != null && column.length > 0) {
                int columnCount = column.length;
                Where<?, Integer> where = queryBuilder.where();
                for (int i = 0; i < columnCount; i++) {
                    String c = column[i];
                    Object v = value[i];
                    if (v instanceof Object[]) {
                        Object[] vv = (Object[]) v;
                        for (int j = 0; j < vv.length; j++) {
                            where.eq(c, vv[j]);
                            if (j < vv.length - 1)
                                where = where.or();
                        }
                    } else {
                        where.eq(c, v);
                    }
                    if (i < columnCount - 1) {
                        where = where.and();
                    }
                }
            }

            // 组装order by
            if (orderBy != null) {
                for (int i = 0; i < orderBy.length; i++) {
                    String o = orderBy[i];
                    Boolean asc = ascending[i];

                    queryBuilder.orderBy(o, asc);
                }
            } else {
                queryBuilder.orderBy("_id", false);
            }

            // 状态offset
            if (start != -1) {
                queryBuilder.offset(start);
            }

            if (length != -1) {
                queryBuilder.limit(length);
            }

            list = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<?> queryUserInfo(Context context, String[] column, Object[] value) throws SQLException {
        return query(context, getDataHelper(context).getUserInfoDao(), column, value, null, null, -1, -1);
    }

    public static List<?> queryUserInfo(Context context, String[] column,
                                        Object[] value, String[] orderByColumns, Boolean[] ascending,
                                        int start, int length) throws SQLException {
        return query(context, getDataHelper(context).getUserInfoDao(), column, value, orderByColumns, ascending, start, length);
    }

    public static List<?> queryRestaurant(Context context, String[] column, Object[] value) throws SQLException {
        return query(context, getDataHelper(context).getRestaurantDao(), column, value, null, null, -1, -1);
    }

    public static List<?> queryRestaurant(Context context, String[] column,
                                          Object[] value, String[] orderByColumns, Boolean[] ascending,
                                          int start, int length) throws SQLException {
        return query(context, getDataHelper(context).getRestaurantDao(), column, value, orderByColumns, ascending, start, length);
    }

    public static List<?> queryAddress(Context context, String[] column, Object[] value) throws SQLException {
        return query(context, getDataHelper(context).getAddressDao(), column, value, null, null, -1, -1);
    }

    public static List<?> queryAddress(Context context, String[] column,
                                       Object[] value, String[] orderByColumns, Boolean[] ascending,
                                       int start, int length) throws SQLException {
        return query(context, getDataHelper(context).getAddressDao(), column, value, orderByColumns, ascending, start, length);
    }

    public static List<?> queryOrder(Context context, String[] column, Object[] value) throws SQLException {
        return query(context, getDataHelper(context).getOrderDao(), column, value, null, null, -1, -1);
    }

    public static List<?> queryOrder(Context context, String[] column,
                                       Object[] value, String[] orderByColumns, Boolean[] ascending,
                                       int start, int length) throws SQLException {
        return query(context, getDataHelper(context).getOrderDao(), column, value, orderByColumns, ascending, start, length);
    }

    public static List<?> queryCompany(Context context, String[] column, Object[] value) throws SQLException {
        return  query(context, getDataHelper(context).getCompanyDao(), column, value, null, null, -1, -1);
    }

    public static List<?> queryCompany(Context context, String[] column,
                                       Object[] value, String[] orderByColumns, Boolean[] ascending,
                                       int start, int length) throws SQLException {
        return query(context, getDataHelper(context).getCompanyDao(), column, value, orderByColumns, ascending, start, length);
    }

    /**
     * add
     */

    public static void createUserInfo(Context context, GLUserInfo info) throws SQLException {
        getDataHelper(context).getUserInfoDao().createOrUpdate(info);
    }

    public static void createRestaurant(Context context, GLRestaurant info) throws SQLException {
        getDataHelper(context).getRestaurantDao().createOrUpdate(info);
    }

    public static void createAddress(Context context, GLDeliveryAddressModel info) throws SQLException {
        getDataHelper(context).getAddressDao().createOrUpdate(info);
    }
    public static void createOrder(Context context, Order info) throws SQLException {
        getDataHelper(context).getOrderDao().createOrUpdate(info);
    }

    public static void createCompany(Context context, Company info) throws SQLException {
        getDataHelper(context).getCompanyDao().createOrUpdate(info);
    }

    /**
     * replace
     *
     * @throws SQLException
     */
    public static void replaceUserInfo(Context context, GLUserInfo info) throws SQLException {
        getDataHelper(context).getUserInfoDao().delete(info);
        getDataHelper(context).getUserInfoDao().createOrUpdate(info);
    }

    public static void replaceRestaurant(Context context, GLRestaurant info) throws SQLException {
        getDataHelper(context).getRestaurantDao().delete(info);
        getDataHelper(context).getRestaurantDao().createOrUpdate(info);
    }

    public static void replaceAddress(Context context, GLDeliveryAddressModel info) throws SQLException {
        getDataHelper(context).getAddressDao().delete(info);
        getDataHelper(context).getAddressDao().createOrUpdate(info);
    }

    public static void replaceOrder(Context context, Order info) throws SQLException {
        getDataHelper(context).getOrderDao().delete(info);
        getDataHelper(context).getOrderDao().createOrUpdate(info);
    }

    public static void replaceCompany(Context context, Company info) throws SQLException {
        getDataHelper(context).getCompanyDao().delete(info);
        getDataHelper(context).getCompanyDao().createOrUpdate(info);
    }

    /**
     * update
     */
    public static void updateUserInfo(Context context, GLUserInfo info) throws SQLException {
        getDataHelper(context).getUserInfoDao().update(info);
    }

    public static void updateRestaurant(Context context, GLRestaurant info) throws SQLException {
        getDataHelper(context).getRestaurantDao().update(info);
    }

    public static void updateAddress(Context context, GLDeliveryAddressModel info) throws SQLException {
        getDataHelper(context).getAddressDao().update(info);
    }

    public static void updateOrder(Context context, Order info) throws SQLException {
        getDataHelper(context).getOrderDao().update(info);
    }

    public static void updateCompany(Context context, Company info) throws SQLException {
        getDataHelper(context).getCompanyDao().update(info);
    }

    /**
     * delete
     */
    private static int delete(Dao<?, Integer> dao, String columnName, String value) throws SQLException {
        DeleteBuilder<?, Integer> deleteBuilder = dao.deleteBuilder();
        deleteBuilder.where().eq(columnName, value);
        return deleteBuilder.delete();
    }

    public static int deleteUserInfo(Context context, String columnName, String value) throws SQLException {
        return delete(getDataHelper(context).getUserInfoDao(), columnName, value);
    }

    public static int deleteRestaurant(Context context, String columnName, String value) throws SQLException {
        return delete(getDataHelper(context).getRestaurantDao(), columnName, value);
    }

    public static int deleteAddress(Context context, String columnName, String value) throws SQLException {
        return delete(getDataHelper(context).getAddressDao(), columnName, value);
    }

    public static int deleteOrder(Context context, String columnName, String value) throws SQLException {
        return delete(getDataHelper(context).getOrderDao(), columnName, value);
    }

    public static int deleteCompany(Context context, String columnName, String value) throws SQLException {
        return delete(getDataHelper(context).getCompanyDao(), columnName, value);
    }
}
