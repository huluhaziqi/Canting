package cn.caimatou.canting.response;

import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.bean.GLUserInfo;

/**
 * Description：用户信息结果
 * <br/><br/>Created by Fausgoal on 15/9/5.
 * <br/><br/>
 */
public class GLUserInfoResp extends GLBaseInfo {
    private GLUserInfo result;

    public GLUserInfo getResult() {
        return result;
    }

    public void setResult(GLUserInfo result) {
        this.result = result;
    }
}
