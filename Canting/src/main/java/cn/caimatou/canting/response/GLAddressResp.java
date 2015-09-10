package cn.caimatou.canting.response;

import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.bean.GLDeliveryAddressModel;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 15/9/8.
 * <br/><br/>
 */
public class GLAddressResp extends GLBaseInfo {
    private GLDeliveryAddressModel result;

    public GLDeliveryAddressModel getResult() {
        return result;
    }

    public void setResult(GLDeliveryAddressModel result) {
        this.result = result;
    }
}
