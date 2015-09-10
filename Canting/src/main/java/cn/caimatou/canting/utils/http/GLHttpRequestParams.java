package cn.caimatou.canting.utils.http;


import cn.caimatou.canting.BuildConfig;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 2015/5/8.
 * <br/><br/>
 */
public class GLHttpRequestParams {

    /**
     * HTTP API URL 切换环境时会自动切换
     */
    public static final String HTTP_API_URL = BuildConfig.API_URL;

    public static final String APP_KEY = "87f904e741e94e1b9a325d0993f7bf85";
    public static final String SECRET_KEY = "74e0b7d3efc147b18ea97b9aaa74363b";
    public static final String ANDROID = "android";
    /**
     * 系统类型 1:供应商端；2:餐厅端
     */
    public static final int APP_KIND = 2;

    /**
     * 接口参数类型
     */
    public static final String CONTENT_TYPE = "application/json";

    /**
     * 接口版本
     */
    public static final int API_VERSION = 2;

    /**
     * 登录
     */
    public static final String LOGIN = "/api/user/v1/login";
    /**
     * 登出
     */
    public static final String LOGOUT = "/api//user/v1/logout";
    /**
     * 校验手机号,发送验证码
     */
    public static final String STEP1 = "/api/signup/v1/step1";
    /**
     * 检验验证码
     */
    public static final String STEP2 = "/api/signup/v1/step2";
    /**
     * 用户注册
     */
    public static final String STEP3 = "/api/signup/v1/step3";
    /**
     * 获取供应商列表
     */
    public static final String GETSUPPLIERS = "/api/rest/v1/suppliers";
    /**
     * 获取我的供应商列表
     */
    public static final String GET_MYSUPPLIERS = "/api/rest/v1/mysupplier/list";
    /**
     * 更新用户密码
     */
    public static final String UPDATEPWD = "/api/user/v1/update_password";
    /**
     * 校验更换绑定手机验证码
     */
    public static final String SEND_CHECKCODE = "/api/user/v1/send_checkcode";
    /**
     * 使用手机校验码登录
     */
    public static final String LOGIN_BY_CHECK_CODE = "/api/user/v1/login_by_checkcode";
    /**
     * 根据校验码修改密码
     */
    public static final String UPDATE_PASSWORD_BY_CHECK_CODE = "/api/user/v1/update_password_by_checkcode";
    /**
     * 更新用户信息
     */
    public static final String UPDATE_USER_INFO = "/api/user/v1/update_info";
    /**
     * 餐厅注册
     */
    public static final String RESTAURANT = "/api/comp_signup/v1/restaurant";
    /**
     * 添加收货地址
     */
    public static final String ADDRESS_ADD = "/api/rest/v1/address/add";
    /**
     * 修改收货地址
     */
    public static final String ADDRESS_EDIT = "/api/rest/v1/address/update";
    /**
     * 更改默认收货地址
     */
    public static final String SET_ADDRESS_DEFAULT = "/api/rest/v1/address/default";
    /**
     * 移除收货地址
     */
    public static final String ADDRESS_REMOVE = "/api/rest/v1/address/remove";
    /**
     * 列出餐厅的收货地址
     */
    public static final String ADDRESS_COMP_LIST = "/api/rest/v1/address/comp_list";
    /**
     * 我的历史订单
     */
    public static final String HIS_ORDERS = "/api/order/v1/rest/my_his_orders";
    /**
     * 我的订单
     */
    public static final String MY_ORDERS = "/api/order/v1/rest/my_orders";
    /**
     * 餐厅确认收货
     */
    public static final String CONFIRM_DELIVERY = "/api/order/v1/rest/confirm_delivery";
    /**
     * 餐厅申请取消订单
     */
    public static final String ORDER_CANCEL = "/api/order/v1/rest/cancel";
    /**
     * 订单详情
     */
    public static final String ORDER_INFO = "/api/order/v1/order_info";
    /**
     * 添加到我的供应商
     */
    public static final String MYSUPPLIERS_ADD = "/api/get/rest/v1/mysupplier/add";
    /**
     * 餐厅下单
     */
    public static final String ORDER_SUBMIT = "/api/order/v1/rest/submit";

}
