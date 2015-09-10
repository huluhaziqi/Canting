package cn.caimatou.canting.response;

import java.util.List;

import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.bean.GLDeliveryAddressModel;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 15/9/8.
 * <br/><br/>
 */
public class GLAddressCompList extends GLBaseInfo {
    private List<GLDeliveryAddressModel> result;

    public List<GLDeliveryAddressModel> getResult() {
        return result;
    }

    public void setResult(List<GLDeliveryAddressModel> result) {
        this.result = result;
    }
}
