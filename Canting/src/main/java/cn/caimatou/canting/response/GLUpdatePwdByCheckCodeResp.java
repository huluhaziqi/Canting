package cn.caimatou.canting.response;

import cn.caimatou.canting.base.GLBaseInfo;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 15/9/6.
 * <br/><br/>
 */
public class GLUpdatePwdByCheckCodeResp extends GLBaseInfo {
    private boolean result;

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
