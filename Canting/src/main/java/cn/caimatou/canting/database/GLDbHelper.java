package cn.caimatou.canting.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import cn.caimatou.canting.bean.Company;
import cn.caimatou.canting.bean.GLDeliveryAddressModel;
import cn.caimatou.canting.bean.GLRestaurant;
import cn.caimatou.canting.bean.GLUserInfo;
import cn.caimatou.canting.bean.Order;
import cn.caimatou.canting.utils.GLLog;

/**
 * Description：
 * <br/><br/>Created by Fausogl on 2015/8/31.
 * <br/><br/>
 */
public class GLDbHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = "GLDbHelper";

    private static final String DATABASE_NAME = "canting.db";
    private static final int DATABASE_VERSION = 1;

    public GLDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, GLUserInfo.class);
            TableUtils.createTable(connectionSource, GLRestaurant.class);
            TableUtils.createTable(connectionSource, GLDeliveryAddressModel.class);
            TableUtils.createTable(connectionSource, Order.class);
            TableUtils.createTable(connectionSource, Company.class);
        } catch (java.sql.SQLException e) {
            GLLog.trace(TAG, "创建数据库失败", e);
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, GLUserInfo.class, true);
            TableUtils.dropTable(connectionSource, GLRestaurant.class, true);
            TableUtils.dropTable(connectionSource, GLDeliveryAddressModel.class, true);
            TableUtils.dropTable(connectionSource, Order.class, true);
            TableUtils.dropTable(connectionSource, Company.class, true);
        } catch (java.sql.SQLException e) {
            GLLog.trace(TAG, "更新数据库失败", e);
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        super.close();
    }

    public Dao<GLUserInfo, Integer> getUserInfoDao() throws SQLException {
        return DaoManager.createDao(connectionSource, GLUserInfo.class);
    }

    public Dao<GLRestaurant, Integer> getRestaurantDao() throws SQLException {
        return DaoManager.createDao(connectionSource, GLRestaurant.class);
    }

    public Dao<GLDeliveryAddressModel, Integer> getAddressDao() throws SQLException {
        return DaoManager.createDao(connectionSource, GLDeliveryAddressModel.class);
    }

    public Dao<Order, Integer> getOrderDao() throws SQLException {
        return DaoManager.createDao(connectionSource, Order.class);
    }

    public Dao<Company, Integer> getCompanyDao() throws SQLException {
        return DaoManager.createDao(connectionSource, Company.class);
    }
}
