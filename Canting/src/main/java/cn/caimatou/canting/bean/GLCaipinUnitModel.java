package cn.caimatou.canting.bean;

import cn.caimatou.canting.base.GLBaseInfo;

/**
 * Description：菜品单位
 * <br/><br/>Created by Fausgoal on 15/9/1.
 * <br/><br/>
 */
public class GLCaipinUnitModel extends GLBaseInfo {
    private int id;
    private String unit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
