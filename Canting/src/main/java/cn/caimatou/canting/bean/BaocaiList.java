package cn.caimatou.canting.bean;

import java.io.Serializable;

/**
 * Created by Rosicky on 15/9/1.
 */
public class BaocaiList implements Serializable {
    public final static int DONE_LIST = 0;
    public final static int DOING_LIST = 1;
    public final static int CANCEL_LIST = 2;

    private String shopName;
    private int buyCount;
    private int listStatus;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public int getListStatus() {
        return listStatus;
    }

    public void setListStatus(int listStatus) {
        this.listStatus = listStatus;
    }
}
