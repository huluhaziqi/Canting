package cn.caimatou.canting.response;

import java.util.List;

import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.bean.Order;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 15/9/9.
 * <br/><br/>
 */
public class GLMyOrdersResp extends GLBaseInfo {
    private List<Order> result;

    public List<Order> getResult() {
        return result;
    }

    public void setResult(List<Order> result) {
        this.result = result;
    }
}
