package cn.caimatou.canting.bean;

import cn.caimatou.canting.base.GLBaseInfo;

/**
 * Description：配送时间
 * <br/><br/>Created by Fausgoal on 15/9/1.
 * <br/><br/>
 */
public class GLDeliveryTimeModel extends GLBaseInfo {
    private int id;
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
