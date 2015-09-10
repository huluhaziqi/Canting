package cn.caimatou.canting.utils.http;

import android.content.Context;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import cn.caimatou.canting.bean.GLCaipinModel;
import cn.caimatou.canting.bean.GLLocation;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.manager.GLApiManager;
import cn.caimatou.canting.manager.GLLocationManager;
import cn.caimatou.canting.manager.GLUserManager;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 2015/5/8.
 * <br/><br/>
 */
public class GLHttpRequestHelper {
    private static final String TAG = "HttpRequestHelper";

    /**
     * 登录
     *
     * @param context
     * @param account
     * @param password
     * @param handler
     */
    public static void login(Context context, String account, String password, GLApiHandler handler) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.USER_NAME, account);
        params.put(GLHttpRequestField.PASSWORD, password);
        params.put(GLHttpRequestField.KIND, GLHttpRequestParams.APP_KIND);
        GLApiManager.getInitialize(context).login(GLHttpRequestParams.LOGIN, params, handler);
    }

    /**
     * 校验手机号,发送验证码
     *
     * @param context
     * @param phone
     * @param handler
     */
    public static void step1(Context context, String phone, GLApiHandler handler) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.PHONE_CODE, phone);
        GLApiManager.getInitialize(context).step1(GLHttpRequestParams.STEP1, params, handler);
    }

    /**
     * 检验验证码
     *
     * @param context
     * @param phone
     * @param handler
     */
    public static void step2(Context context, String phone, String checkCode, GLApiHandler handler) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.PHONE_CODE, phone);
        params.put(GLHttpRequestField.CHECK_CODE, checkCode);
        GLApiManager.getInitialize(context).step2(GLHttpRequestParams.STEP2, params, handler);
    }

    /**
     * 检验验证码
     *
     * @param context
     * @param phone
     * @param handler
     */
    public static void step3(Context context, String signToken, String phone, String password, String nickname, int sex, double longitude, double latitude, GLApiHandler handler) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.SIGN_TOKEN, signToken);
        params.put(GLHttpRequestField.PHONE_CODE, phone);
        params.put(GLHttpRequestField.PASSWORD, password);
        params.put(GLHttpRequestField.NICK_NAME, nickname);
        params.put(GLHttpRequestField.SEX, sex);
        params.put(GLHttpRequestField.LONGITUDE, longitude);
        params.put(GLHttpRequestField.LATITUDE, latitude);
        GLApiManager.getInitialize(context).step3(GLHttpRequestParams.STEP3, params, handler);
    }

    /**
     * 使用手机校验码登录
     *
     * @param context
     * @param username
     * @param phone_check_code
     * @param handler
     */
    public static void loginByCheckCode(Context context, String username, String phone_check_code, GLApiHandler handler) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.USER_NAME, username);
        params.put(GLHttpRequestField.PHONE_CHECK_CODE, phone_check_code);
        params.put(GLHttpRequestField.KIND, GLHttpRequestParams.APP_KIND);
        GLApiManager.getInitialize(context).loginByCheckCode(GLHttpRequestParams.LOGIN_BY_CHECK_CODE, params, handler);
    }

    /**
     * 根据校验码修改密码
     *
     * @param context
     * @param username
     * @param phone_check_code
     * @param newPassword
     * @param handler
     */
    public static void updatePasswordByCheckCode(Context context, String username, String phone_check_code, String newPassword, GLApiHandler handler) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.USER_NAME, username);
        params.put(GLHttpRequestField.PHONE_CHECK_CODE, phone_check_code);
        params.put(GLHttpRequestField.NEWPWD, newPassword);
        GLApiManager.getInitialize(context).updatePasswordByCheckCode(GLHttpRequestParams.UPDATE_PASSWORD_BY_CHECK_CODE, params, handler);
    }

    /**
     * 登出
     *
     * @param context
     * @param handler
     */
    public static void logout(Context context, GLApiHandler handler) {
        String token = GLUserManager.getToken(context);
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.TOKEN, token);
        GLApiManager.getInitialize(context).logout(GLHttpRequestParams.LOGOUT, params, handler);
    }

    /**
     * 获取供应商列表
     *
     * @param context
     * @param handler
     */
    public static void fetchSuppliers(Context context, GLApiHandler handler) {
        String token = GLUserManager.getToken(context);
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.TOKEN, token);
        GLApiManager.getInitialize(context).fetchSuppliers(GLHttpRequestParams.GETSUPPLIERS, params, handler);
    }

    /**
     * 获取我的供应商列表
     *
     * @param context
     * @param handler
     */
    public static void fetchMySuppliers(Context context, GLApiHandler handler) {
        String token = GLUserManager.getToken(context);
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.TOKEN, token);
        GLApiManager.getInitialize(context).fetchMySuppliers(GLHttpRequestParams.GET_MYSUPPLIERS, params, handler);
    }

    /**
     * 更新用户密码
     *
     * @param context
     * @param oldPwd
     * @param newPwd
     * @param handler
     */
    public static void updatePassword(Context context, String oldPwd, String newPwd, GLApiHandler handler) {
        long userId = GLUserManager.getUserId(context);
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.USERID, userId);
        params.put(GLHttpRequestField.OLDPWD, oldPwd);
        params.put(GLHttpRequestField.NEWPWD, newPwd);
        GLApiManager.getInitialize(context).updatePassword(GLHttpRequestParams.UPDATEPWD, params, handler);
    }

    /**
     * 更换手机验证
     *
     * @param context
     * @param phone
     * @param code
     * @param handler
     */
    public static void sendCheckCode(Context context, String phone, String code, GLApiHandler handler) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.USER_NAME, phone);
        params.put(GLHttpRequestField.CHECK_CODE, code);
        GLApiManager.getInitialize(context).sendCheckCode(GLHttpRequestParams.SEND_CHECKCODE, params, handler);
    }

    /**
     * 更新用户信息
     *
     * @param context
     * @param userId
     * @param nickName
     * @param sex
     * @param birthday
     * @param handler
     */
    public static void updateUserInfo(Context context, long userId, String nickName, int sex, String birthday, GLApiHandler handler) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        userId = GLUserManager.getUserId(context);
        params.put(GLHttpRequestField.USERID, userId);
        params.put(GLHttpRequestField.NICK_NAME, nickName);
        params.put(GLHttpRequestField.SEX, sex);
        params.put(GLHttpRequestField.BIRTHDAY, birthday);
        GLApiManager.getInitialize(context).updateUserInfo(GLHttpRequestParams.UPDATE_USER_INFO, params, handler);
    }

    /**
     * 餐厅注册
     *
     * @param context
     * @param name
     * @param link_man
     * @param tel
     * @param addr
     * @param detail_addr
     * @param memo
     * @param handler
     */
    public static void restaurant(Context context, String name, String link_man, String tel, String addr,
                                  String detail_addr, String memo, GLApiHandler handler) {
        String token = GLUserManager.getToken(context);
        GLLocation location = GLLocationManager.getLocation();
        double longitude = GLConst.NONE;
        double latitude = GLConst.NONE;
        if (null != location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.TOKEN, token);
        params.put(GLHttpRequestField.NAME, name);
        params.put(GLHttpRequestField.LINK_MAIN, link_man);
        params.put(GLHttpRequestField.TEL, tel);
        params.put(GLHttpRequestField.ADDR, addr);
        params.put(GLHttpRequestField.DETAIL_ADDR, detail_addr);
        params.put(GLHttpRequestField.MEMO, memo);
        params.put(GLHttpRequestField.LONGITUDE, longitude);
        params.put(GLHttpRequestField.LATITUDE, latitude);
        GLApiManager.getInitialize(context).restaurant(GLHttpRequestParams.RESTAURANT, params, handler);
    }

    /**
     * 添加地址
     *
     * @param context
     * @param addr
     * @param detail_addr
     * @param link_man
     * @param tel
     * @param is_default
     * @param handler
     */
    public static void addressAdd(Context context, String addr, String detail_addr, String link_man, String tel, int is_default
            , String province, String town, String region, GLApiHandler handler) {
        String token = GLUserManager.getToken(context);
        GLLocation location = GLLocationManager.getLocation();
        double longitude = GLConst.NONE;
        double latitude = GLConst.NONE;
        if (null != location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.TOKEN, token);
        params.put(GLHttpRequestField.KIND, 1); //类型,1:企业用；2：个人用
        params.put(GLHttpRequestField.PROVINCE, province);
        params.put(GLHttpRequestField.REGION, region);
        params.put(GLHttpRequestField.TOWN, town);
        params.put(GLHttpRequestField.ADDR, addr);
        params.put(GLHttpRequestField.DETAIL_ADDR, detail_addr);
        params.put(GLHttpRequestField.LINK_MAIN, link_man);
        params.put(GLHttpRequestField.TEL, tel);
        params.put(GLHttpRequestField.IS_DEFAULT, is_default);
        params.put(GLHttpRequestField.LONGITUDE, longitude);
        params.put(GLHttpRequestField.LATITUDE, latitude);
        GLApiManager.getInitialize(context).addressAdd(GLHttpRequestParams.ADDRESS_ADD, params, handler);
    }

    /**
     * 修改地址
     *
     * @param context
     * @param addr
     * @param detail_addr
     * @param link_man
     * @param tel
     * @param is_default
     * @param handler
     */
    public static void addressEdit(Context context, long address_id, String addr, String detail_addr, String link_man, String tel, int is_default,
                                   String province, String town, String region, GLApiHandler handler) {
        String token = GLUserManager.getToken(context);
        GLLocation location = GLLocationManager.getLocation();
        double longitude = GLConst.NONE;
        double latitude = GLConst.NONE;
        if (null != location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.TOKEN, token);
        params.put(GLHttpRequestField.ADDRESS_ID, address_id);
        params.put(GLHttpRequestField.PROVINCE, province);
        params.put(GLHttpRequestField.REGION, region);
        params.put(GLHttpRequestField.TOWN, town);
        params.put(GLHttpRequestField.ADDR, addr);
        params.put(GLHttpRequestField.DETAIL_ADDR, detail_addr);
        params.put(GLHttpRequestField.LINK_MAIN, link_man);
        params.put(GLHttpRequestField.TEL, tel);
        params.put(GLHttpRequestField.IS_DEFAULT, is_default);
        params.put(GLHttpRequestField.LONGITUDE, longitude);
        params.put(GLHttpRequestField.LATITUDE, latitude);
        GLApiManager.getInitialize(context).addressEdit(GLHttpRequestParams.ADDRESS_EDIT, params, handler);
    }

    /**
     * 设置为默认
     *
     * @param context
     * @param address_id
     * @param handler
     */
    public static void setAddressDefault(Context context, long address_id, GLApiHandler handler) {
        String token = GLUserManager.getToken(context);
        GLLocation location = GLLocationManager.getLocation();
        double longitude = GLConst.NONE;
        double latitude = GLConst.NONE;
        if (null != location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.TOKEN, token);
        params.put(GLHttpRequestField.ADDRESS_ID, address_id);
        params.put(GLHttpRequestField.LONGITUDE, longitude);
        params.put(GLHttpRequestField.LATITUDE, latitude);
        GLApiManager.getInitialize(context).setAddressDefault(GLHttpRequestParams.SET_ADDRESS_DEFAULT, params, handler);
    }

    /**
     * 删除地址
     *
     * @param context
     * @param address_id
     * @param handler
     */
    public static void addressRemove(Context context, long address_id, GLApiHandler handler) {
        String token = GLUserManager.getToken(context);
        GLLocation location = GLLocationManager.getLocation();
        double longitude = GLConst.NONE;
        double latitude = GLConst.NONE;
        if (null != location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.TOKEN, token);
        params.put(GLHttpRequestField.ADDRESS_ID, address_id);
        params.put(GLHttpRequestField.LONGITUDE, longitude);
        params.put(GLHttpRequestField.LATITUDE, latitude);
        GLApiManager.getInitialize(context).addressRemove(GLHttpRequestParams.ADDRESS_REMOVE, params, handler);
    }

    /**
     * 列出餐厅的收货地址
     *
     * @param context
     * @param handler
     */
    public static void addressCompList(Context context, GLApiHandler handler) {
        String token = GLUserManager.getToken(context);
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.TOKEN, token);
        GLApiManager.getInitialize(context).addressCompList(GLHttpRequestParams.ADDRESS_COMP_LIST, params, handler);
    }

    /**
     * 我的历史订单
     *
     * @param context
     * @param handler
     */
    public static void hisOrders(Context context, GLApiHandler handler) {
        String token = GLUserManager.getToken(context);
        GLLocation location = GLLocationManager.getLocation();
        double longitude = GLConst.NONE;
        double latitude = GLConst.NONE;
        if (null != location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.TOKEN, token);
        params.put(GLHttpRequestField.LONGITUDE, longitude);
        params.put(GLHttpRequestField.LATITUDE, latitude);

        GLApiManager.getInitialize(context).hisOrders(GLHttpRequestParams.HIS_ORDERS, params, handler);
    }

    /**
     * 我的订单
     *
     * @param context
     * @param handler
     */
    public static void myOrders(Context context, GLApiHandler handler) {
        String token = GLUserManager.getToken(context);
        GLLocation location = GLLocationManager.getLocation();
        double longitude = GLConst.NONE;
        double latitude = GLConst.NONE;
        if (null != location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.TOKEN, token);
        params.put(GLHttpRequestField.LONGITUDE, longitude);
        params.put(GLHttpRequestField.LATITUDE, latitude);

        GLApiManager.getInitialize(context).myOrders(GLHttpRequestParams.MY_ORDERS, params, handler);
    }

    /**
     * 餐厅确认收货
     *
     * @param context
     * @param handler
     */
    public static void confirmDelivery(Context context, long order_id, GLApiHandler handler) {
        String token = GLUserManager.getToken(context);
        GLLocation location = GLLocationManager.getLocation();
        double longitude = GLConst.NONE;
        double latitude = GLConst.NONE;
        if (null != location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.TOKEN, token);
        params.put(GLHttpRequestField.ORDER_ID, order_id);
        params.put(GLHttpRequestField.LONGITUDE, longitude);
        params.put(GLHttpRequestField.LATITUDE, latitude);

        GLApiManager.getInitialize(context).confirmDelivery(GLHttpRequestParams.CONFIRM_DELIVERY, params, handler);
    }

    /**
     * 餐厅申请取消订单
     *
     * @param context
     * @param handler
     */
    public static void orderCancel(Context context, long order_id, GLApiHandler handler) {
        String token = GLUserManager.getToken(context);
        GLLocation location = GLLocationManager.getLocation();
        double longitude = GLConst.NONE;
        double latitude = GLConst.NONE;
        if (null != location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.TOKEN, token);
        params.put(GLHttpRequestField.ORDER_ID, order_id);
        params.put(GLHttpRequestField.LONGITUDE, longitude);
        params.put(GLHttpRequestField.LATITUDE, latitude);

        GLApiManager.getInitialize(context).orderCancel(GLHttpRequestParams.ORDER_CANCEL, params, handler);
    }

    /**
     * 餐厅申请取消订单
     *
     * @param context
     * @param handler
     */
    public static void orderInfo(Context context, long order_id, GLApiHandler handler) {
        String token = GLUserManager.getToken(context);
        GLLocation location = GLLocationManager.getLocation();
        double longitude = GLConst.NONE;
        double latitude = GLConst.NONE;
        if (null != location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.TOKEN, token);
        params.put(GLHttpRequestField.ORDER_ID, order_id);
        params.put(GLHttpRequestField.LONGITUDE, longitude);
        params.put(GLHttpRequestField.LATITUDE, latitude);

        GLApiManager.getInitialize(context).orderInfo(GLHttpRequestParams.ORDER_INFO, params, handler);
    }

    /**
     * 添加到我的供应商
     * @param context
     * @param companyId
     * @param handler
     */
    public static void addToMySupplier(Context context, long companyId, GLApiHandler handler) {
        String token = GLUserManager.getToken(context);
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.TOKEN, token);
        params.put(GLHttpRequestField.COMPANY_ID, companyId);

        GLApiManager.getInitialize(context).addToMySupplier(GLHttpRequestParams.MYSUPPLIERS_ADD, params, handler);
    }
        /**
         * 餐厅下单
         *
         * @param context
         * @param handler
         */
    public static void orderSubmit(Context context, long provider_id, long address_id, long receipt_time_kind,
                                   List<GLCaipinModel> caipinModels, String receipt_time_detail, String memo, GLApiHandler handler) {
        String token = GLUserManager.getToken(context);
        GLLocation location = GLLocationManager.getLocation();
        double longitude = GLConst.NONE;
        double latitude = GLConst.NONE;
        if (null != location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        String unique_token = UUID.randomUUID().toString();

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(GLHttpRequestField.TOKEN, token);
        params.put(GLHttpRequestField.UNIQUE_TOKEN, unique_token);
        params.put(GLHttpRequestField.ORDER_KIND, 2); // 订单类型；1，企业订单；2,个人订单
        params.put(GLHttpRequestField.PROVIDER_ID, provider_id);
        params.put(GLHttpRequestField.ADDRESS_ID, address_id);
        params.put(GLHttpRequestField.RECEIPT_TIIME_KIND, receipt_time_kind);
        params.put(GLHttpRequestField.FOODS, caipinModels);
        params.put(GLHttpRequestField.RECEIPT_TIME_DETAIL, receipt_time_detail);
        params.put(GLHttpRequestField.MEMO, memo);
        params.put(GLHttpRequestField.LONGITUDE, longitude);
        params.put(GLHttpRequestField.LATITUDE, latitude);

        GLApiManager.getInitialize(context).orderSubmit(GLHttpRequestParams.ORDER_SUBMIT, params, handler);
    }

}
