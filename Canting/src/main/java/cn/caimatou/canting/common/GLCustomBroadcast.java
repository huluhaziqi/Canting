package cn.caimatou.canting.common;

/**
 * Description：广播Action定义类
 * <br/><br/>Created by Fausgoal on 2015/5/20.
 * <br/><br/>
 */
public final class GLCustomBroadcast {
    /**
     * 添加菜品 action
     */
    public static final String ON_ADD_CAIPIN_ACTION = "cn.caimatou.canting.on_addcaipin_action";

    /**
     * 点击搜索按关键字 action
     */
    public static final String ON_SEARCH_KEYWORD_ACTION = "cn.caimatou.canting.on_search_keyword_action";

    /**
     * 点击搜索按日期 action
     */
    public static final String ON_SEARCH_DATE_ACTION = "cn.caimatou.canting.on_search_date_action";

    /**
     * 撤消报菜单 action
     */
    public static final String ON_ORDER_CANCELED_ACTION = "cn.caimatou.canting.on_order_canceled_action";
    /**
     * 没有报菜单，点击切换到供应商界面 action
     */
    public static final String ON_CHANGE_TO_PROVIDER_ACTION = "cn.caimatou.canting.on_change_to_provider_action";
}
