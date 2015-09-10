package cn.caimatou.canting.manager;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import cn.caimatou.canting.bean.GLDeliveryAddressModel;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.utils.GLListUtil;

/**
 * Description：地址管理
 * <br/><br/>Created by Fausgoal on 15/9/8.
 * <br/><br/>
 */
public class GLAddressManager {
    private GLAddressManager() {
    }

    public static void saveAddress(Context context, GLDeliveryAddressModel addressModel) {
        if (null == addressModel) {
            return;
        }

        GLDeliveryAddressModel locAddressModel = getAddress(context, addressModel.getId());
        if (null != locAddressModel) {
            addressModel.set_id(locAddressModel.get_id());
        }
        try {
            GLDatabaseManager.replaceAddress(context, addressModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveAddress(Context context, List<GLDeliveryAddressModel> addressModels) {
        if (GLListUtil.isEmpty(addressModels)) {
            return;
        }

        for (GLDeliveryAddressModel addressModel : addressModels) {
            saveAddress(context, addressModel);
        }
    }

    public static GLDeliveryAddressModel getAddress(Context context, long id) {
        try {
            List<GLDeliveryAddressModel> addressModels = (List<GLDeliveryAddressModel>) GLDatabaseManager.queryAddress(context, new String[]{"id"}, new Object[]{id});
            if (!GLListUtil.isEmpty(addressModels)) {
                return addressModels.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<GLDeliveryAddressModel> getAllAddress(Context context) {
        long userId = GLUserManager.getUserId(context);
        try {
            return (List<GLDeliveryAddressModel>) GLDatabaseManager.queryAddress(context, new String[]{"userId"}, new Object[]{userId});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static GLDeliveryAddressModel getDefaultAddress(Context context) {
        long userId = GLUserManager.getUserId(context);
        try {
            List<GLDeliveryAddressModel> addressModels = (List<GLDeliveryAddressModel>) GLDatabaseManager.queryAddress(context, new String[]{"userId", "isDefault"}, new Object[]{userId, 1});
            if (!GLListUtil.isEmpty(addressModels)) {
                return addressModels.get(GLConst.NONE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateAddress(Context context, GLDeliveryAddressModel addressModel) {
        if (null == addressModel) {
            return;
        }
        GLDeliveryAddressModel locAddressModel = getAddress(context, addressModel.getId());
        if (null != locAddressModel) {
            addressModel.set_id(locAddressModel.get_id());
            try {
                GLDatabaseManager.updateAddress(context, addressModel);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                GLDatabaseManager.createAddress(context, addressModel);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void delAddress(Context context, long id) {
        try {
            GLDatabaseManager.deleteAddress(context, "id", id + "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
