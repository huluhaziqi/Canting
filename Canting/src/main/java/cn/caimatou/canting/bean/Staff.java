package cn.caimatou.canting.bean;

import java.io.Serializable;

/**
 * Created by Rosicky on 15/8/31.
 */
public class Staff implements Serializable{
    private String staffName;
    private String staffAvaterUrl;
    private String staffPost;
    private boolean isActive;
    private String staffPhone;
    private String staffAddress;
    private String staffBirth;
    private boolean isAdd;

    public boolean isAdd() {
        return isAdd;
    }

    public void setIsAdd(boolean isAdd) {
        this.isAdd = isAdd;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffAvaterUrl() {
        return staffAvaterUrl;
    }

    public void setStaffAvaterUrl(String staffAvaterUrl) {
        this.staffAvaterUrl = staffAvaterUrl;
    }

    public String getStaffPost() {
        return staffPost;
    }

    public void setStaffPost(String staffPost) {
        this.staffPost = staffPost;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getStaffPhone() {
        return staffPhone;
    }

    public void setStaffPhone(String staffPhone) {
        this.staffPhone = staffPhone;
    }

    public String getStaffAddress() {
        return staffAddress;
    }

    public void setStaffAddress(String staffAddress) {
        this.staffAddress = staffAddress;
    }

    public String getStaffBirth() {
        return staffBirth;
    }

    public void setStaffBirth(String staffBirth) {
        this.staffBirth = staffBirth;
    }
}
