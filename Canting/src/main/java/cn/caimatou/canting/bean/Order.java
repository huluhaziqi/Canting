package cn.caimatou.canting.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import cn.caimatou.canting.base.GLBaseInfo;

/**
 * Description：我的订单
 * <br/><br/>Created by Fausgoal on 15/9/9.
 * <br/><br/>
 */
@DatabaseTable(tableName = "Order")
public class Order extends GLBaseInfo {
    private static final long serialVersionUID = 1L;
    @DatabaseField(generatedId = true)
    private Integer _id;

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    @DatabaseField(unique = true)
    private long orderId;
    @DatabaseField
    private long userId;
    @DatabaseField
    private String username;
    @DatabaseField
    private String nickname;
    @DatabaseField
    private long restCompanyId;
    @DatabaseField
    private String restCompany;
    @DatabaseField
    private String restAddr;
    @DatabaseField
    private String restAddrDetail;
    @DatabaseField
    private String restTel;
    @DatabaseField
    private long supplierCompanyId;
    @DatabaseField
    private String supplierCompany;
    @DatabaseField
    private String supplierTel;
    @DatabaseField
    private String supplierAddr;
    @DatabaseField
    private String supplierAddrDetail;
    @DatabaseField
    private String supplierBusiness;
    @DatabaseField
    private long status;
    @DatabaseField
    private String date;
    @DatabaseField
    private String code;
    @DatabaseField
    private long num;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getRestCompanyId() {
        return restCompanyId;
    }

    public void setRestCompanyId(long restCompanyId) {
        this.restCompanyId = restCompanyId;
    }

    public String getRestCompany() {
        return restCompany;
    }

    public void setRestCompany(String restCompany) {
        this.restCompany = restCompany;
    }

    public String getRestAddr() {
        return restAddr;
    }

    public void setRestAddr(String restAddr) {
        this.restAddr = restAddr;
    }

    public String getRestAddrDetail() {
        return restAddrDetail;
    }

    public void setRestAddrDetail(String restAddrDetail) {
        this.restAddrDetail = restAddrDetail;
    }

    public String getRestTel() {
        return restTel;
    }

    public void setRestTel(String restTel) {
        this.restTel = restTel;
    }

    public long getSupplierCompanyId() {
        return supplierCompanyId;
    }

    public void setSupplierCompanyId(long supplierCompanyId) {
        this.supplierCompanyId = supplierCompanyId;
    }

    public String getSupplierCompany() {
        return supplierCompany;
    }

    public void setSupplierCompany(String supplierCompany) {
        this.supplierCompany = supplierCompany;
    }

    public String getSupplierTel() {
        return supplierTel;
    }

    public void setSupplierTel(String supplierTel) {
        this.supplierTel = supplierTel;
    }

    public String getSupplierAddr() {
        return supplierAddr;
    }

    public void setSupplierAddr(String supplierAddr) {
        this.supplierAddr = supplierAddr;
    }

    public String getSupplierAddrDetail() {
        return supplierAddrDetail;
    }

    public void setSupplierAddrDetail(String supplierAddrDetail) {
        this.supplierAddrDetail = supplierAddrDetail;
    }

    public String getSupplierBusiness() {
        return supplierBusiness;
    }

    public void setSupplierBusiness(String supplierBusiness) {
        this.supplierBusiness = supplierBusiness;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }
}
