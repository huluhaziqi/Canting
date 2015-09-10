package cn.caimatou.canting.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import cn.caimatou.canting.base.GLBaseInfo;

/**
 * Created by Rosicky on 15/9/6.
 */
@DatabaseTable(tableName = "Company")
public class Company extends GLBaseInfo {
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
    private long id;                        //  id
    @DatabaseField
    private long createTime;                //  创建时间
    @DatabaseField
    private long modifyTime;                //  修改时间
    @DatabaseField
    private int opVersion;                  //  版本号
    @DatabaseField
    private int isValid;                    //  是否有效
    @DatabaseField
    private int companyType;                //  企业类型
    @DatabaseField
    private String name;                    //  企业名称
    @DatabaseField
    private String addr;                    //  省地市区
    @DatabaseField
    private String linkMan;                 //  联系人
    @DatabaseField
    private int certificationStatus;        //  资质认证状态
    @DatabaseField
    private String logo;                    //  企业log
    @DatabaseField
    private String longitude;               //  经度
    @DatabaseField
    private String latitude;                //  纬度
    @DatabaseField
    private long userId;                    //  负责人
    @DatabaseField
    private int isSeller;                   //  是否卖家
    @DatabaseField
    private String detailAddr;              //  详细地址
    @DatabaseField
    private String tel;                     //  联系电话
    @DatabaseField
    private int auditingStatus;             //  审核状态
    @DatabaseField
    private int settlementStatus;           //  结算认证状态
    @DatabaseField
    private int accountType;                //  账户类型
    @DatabaseField
    private String accountNumber;           //  账户号
    @DatabaseField
    private String accountName;             //  账户名
    @DatabaseField
    private String bank;                    //  银行
    @DatabaseField
    private String memo;                    //  说明

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

    public int getOpVersion() {
        return opVersion;
    }

    public void setOpVersion(int opVersion) {
        this.opVersion = opVersion;
    }

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }

    public int getCompanyType() {
        return companyType;
    }

    public void setCompanyType(int companyType) {
        this.companyType = companyType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public int getCertificationStatus() {
        return certificationStatus;
    }

    public void setCertificationStatus(int certificationStatus) {
        this.certificationStatus = certificationStatus;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getIsSeller() {
        return isSeller;
    }

    public void setIsSeller(int isSeller) {
        this.isSeller = isSeller;
    }

    public String getDetailAddr() {
        return detailAddr;
    }

    public void setDetailAddr(String detailAddr) {
        this.detailAddr = detailAddr;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getAuditingStatus() {
        return auditingStatus;
    }

    public void setAuditingStatus(int auditingStatus) {
        this.auditingStatus = auditingStatus;
    }

    public int getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(int settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
