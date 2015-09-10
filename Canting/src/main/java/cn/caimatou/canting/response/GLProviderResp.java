package cn.caimatou.canting.response;

import java.util.List;

import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.bean.Company;

/**
 * Created by Rosicky on 15/9/6.
 */
public class GLProviderResp extends GLBaseInfo {
    private List<Company> result;

    public List<Company> getResult() {
        return result;
    }

    public void setResult(List<Company> result) {
        this.result = result;
    }
}
