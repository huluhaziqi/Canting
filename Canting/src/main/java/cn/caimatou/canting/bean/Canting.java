package cn.caimatou.canting.bean;

/**
 * Created by Rosicky on 15/9/1.
 */
public class Canting {
    private String logo;
    private String name;
    private String address;
    private String brief;
    private Staff staff;
    private String gender;
    private String phone;
    private boolean isIdentify;

    public boolean isIdentify() {
        return isIdentify;
    }

    public void setIsIdentify(boolean isIdentify) {
        this.isIdentify = isIdentify;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
