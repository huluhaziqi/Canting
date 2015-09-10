package cn.caimatou.canting.response;

import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.bean.GLRestaurant;

/**
 * Description：餐厅注册
 * <br/><br/>Created by Fausgoal on 15/9/7.
 * <br/><br/>
 */
public class GLRestaurantResp extends GLBaseInfo {
    private GLRestaurant result;

    public GLRestaurant getResult() {
        return result;
    }

    public void setResult(GLRestaurant result) {
        this.result = result;
    }
}
