package cn.caimatou.canting.bean;

import cn.caimatou.canting.base.GLBaseInfo;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 15/9/9.
 * <br/><br/>
 */
public class OrderInfo extends GLBaseInfo {
    private long id;
    private long createTime;
    private long modifyTime;
    private long opVersion;
    private long isValid;
    private long opUserId;
    private long userId;
    private long companyId;
    private long providerId;
    private String addr;
    private String receiptTime;
    private long kindNum;
    private long num;
    private long amount;
    private long providerKindNum;
    private long providerNum;
    private long providerAmount;
    private String address;
    private double longitude;
    private double latitude;
    private long status;
    private long orderUserId;
    private long code;
    private long kind;
    private long linkMan;
    private String tel;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public long getOpVersion() {
        return opVersion;
    }

    public void setOpVersion(long opVersion) {
        this.opVersion = opVersion;
    }

    public long getIsValid() {
        return isValid;
    }

    public void setIsValid(long isValid) {
        this.isValid = isValid;
    }

    public long getOpUserId() {
        return opUserId;
    }

    public void setOpUserId(long opUserId) {
        this.opUserId = opUserId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getProviderId() {
        return providerId;
    }

    public void setProviderId(long providerId) {
        this.providerId = providerId;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(String receiptTime) {
        this.receiptTime = receiptTime;
    }

    public long getKindNum() {
        return kindNum;
    }

    public void setKindNum(long kindNum) {
        this.kindNum = kindNum;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getProviderKindNum() {
        return providerKindNum;
    }

    public void setProviderKindNum(long providerKindNum) {
        this.providerKindNum = providerKindNum;
    }

    public long getProviderNum() {
        return providerNum;
    }

    public void setProviderNum(long providerNum) {
        this.providerNum = providerNum;
    }

    public long getProviderAmount() {
        return providerAmount;
    }

    public void setProviderAmount(long providerAmount) {
        this.providerAmount = providerAmount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getOrderUserId() {
        return orderUserId;
    }

    public void setOrderUserId(long orderUserId) {
        this.orderUserId = orderUserId;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public long getKind() {
        return kind;
    }

    public void setKind(long kind) {
        this.kind = kind;
    }

    public long getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(long linkMan) {
        this.linkMan = linkMan;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
