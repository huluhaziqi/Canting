package cn.caimatou.canting.bean;

import cn.caimatou.canting.base.GLBaseInfo;

/**
 * Created by Rosicky on 15/8/31.
 */
public class Post extends GLBaseInfo {

    public static final String STAFF_POST = "staff_post";

    private String postName;
    private boolean postBaocai;
    private boolean postViewReport;

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public boolean isPostBaocai() {
        return postBaocai;
    }

    public void setPostBaocai(boolean postBaocai) {
        this.postBaocai = postBaocai;
    }

    public boolean isPostViewReport() {
        return postViewReport;
    }

    public void setPostViewReport(boolean postViewReport) {
        this.postViewReport = postViewReport;
    }
}
