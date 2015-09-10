package cn.caimatou.canting.response;

import cn.caimatou.canting.base.GLBaseInfo;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 15/9/5.
 * <br/><br/>
 */
public class GLLogoutResp extends GLBaseInfo {
    private boolean result;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
