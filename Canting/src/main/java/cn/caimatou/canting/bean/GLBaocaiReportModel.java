package cn.caimatou.canting.bean;

import java.util.List;

/**
 * Description：报菜单
 * <br/><br/>Created by Fausgoal on 15/9/4.
 * <br/><br/>
 */
public class GLBaocaiReportModel {
    private long mTime;
    private List<Company> mShopInfos;

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public List<Company> getShopInfos() {
        return mShopInfos;
    }

    public void setShopInfos(List<Company> shopInfos) {
        mShopInfos = shopInfos;
    }
}
