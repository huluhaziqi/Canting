package cn.caimatou.canting.bean;

import java.util.List;

import cn.caimatou.canting.base.GLBaseInfo;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 15/9/9.
 * <br/><br/>
 */
public class OrderDetail extends GLBaseInfo {
    private String supplierName;
    private String restName;
    private String username;

    private OrderInfo orderInfo;

    private List<Foods> foods;

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public List<Foods> getFoods() {
        return foods;
    }

    public void setFoods(List<Foods> foods) {
        this.foods = foods;
    }
}
