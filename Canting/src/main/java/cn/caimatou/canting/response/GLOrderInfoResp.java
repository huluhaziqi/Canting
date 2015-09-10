package cn.caimatou.canting.response;

import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.bean.OrderDetail;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 15/9/9.
 * <br/><br/>
 */
public class GLOrderInfoResp extends GLBaseInfo {
    private OrderDetail result;

    public OrderDetail getResult() {
        return result;
    }

    public void setResult(OrderDetail result) {
        this.result = result;
    }
}
