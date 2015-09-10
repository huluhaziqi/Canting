package cn.caimatou.canting.bean;

import cn.caimatou.canting.base.GLBaseInfo;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 15/9/9.
 * <br/><br/>
 */
public class Foods extends GLBaseInfo {
    private long id;
    private long createTime;
    private long modifyTime;
    private long opVersion;
    private long isValid;
    private long orderId;
    private long dishsId;
    private String name;
    private long num;
    private long cancelNum;
    private long realNum;
    private String memo;
    private String unit;
    private String cancelReason;
    private double price;
    private double amount;
    private long status;
    private long cancelUserId;

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

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getDishsId() {
        return dishsId;
    }

    public void setDishsId(long dishsId) {
        this.dishsId = dishsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public long getCancelNum() {
        return cancelNum;
    }

    public void setCancelNum(long cancelNum) {
        this.cancelNum = cancelNum;
    }

    public long getRealNum() {
        return realNum;
    }

    public void setRealNum(long realNum) {
        this.realNum = realNum;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getCancelUserId() {
        return cancelUserId;
    }

    public void setCancelUserId(long cancelUserId) {
        this.cancelUserId = cancelUserId;
    }
}
