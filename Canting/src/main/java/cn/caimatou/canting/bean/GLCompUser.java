package cn.caimatou.canting.bean;

import java.util.Collection;

import cn.caimatou.canting.base.GLBaseInfo;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 15/9/6.
 * <br/><br/>
 */
public class GLCompUser extends GLBaseInfo {
    private long companyId;
    private long userId;
    private long postId;
    private String name;
    private String tel;
    private Collection<Object> permissions;
    private boolean supper;

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Collection<Object> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<Object> permissions) {
        this.permissions = permissions;
    }

    public boolean isSupper() {
        return supper;
    }

    public void setSupper(boolean supper) {
        this.supper = supper;
    }
}
