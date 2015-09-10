package cn.caimatou.canting.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import cn.caimatou.canting.base.GLBaseInfo;

/**
 * Description：餐厅注册
 * <br/><br/>Created by Fausgoal on 15/9/7.
 * <br/><br/>
 */
@DatabaseTable(tableName = "Restaurant")
public class GLRestaurant extends GLBaseInfo {
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
    private long id;
    @DatabaseField
    private long createTime;
    @DatabaseField
    private long modifyTime;
    @DatabaseField
    private long opVersion;
    @DatabaseField
    private long isValid;
    @DatabaseField
    private long companyType;
    @DatabaseField
    private String name;
    @DatabaseField
    private String addr;
    @DatabaseField
    private String linkMan;
    @DatabaseField
    private long certificationStatus;
    @DatabaseField
    private String logo;
    @DatabaseField
    private double longitude;
    @DatabaseField
    private double latitude;
    @DatabaseField
    private long userId;
    @DatabaseField
    private long isSeller;
    @DatabaseField
    private String detailAddr;
    @DatabaseField
    private String tel;
    @DatabaseField
    private long auditingStatus;
    @DatabaseField
    private long settlementStatus;
    @DatabaseField
    private long accountType;
    @DatabaseField
    private String accountNumber;
    @DatabaseField
    private String accountName;
    @DatabaseField
    private String bank;
    @DatabaseField
    private String memo;

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

    public long getCompanyType() {
        return companyType;
    }

    public void setCompanyType(long companyType) {
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

    public long getCertificationStatus() {
        return certificationStatus;
    }

    public void setCertificationStatus(long certificationStatus) {
        this.certificationStatus = certificationStatus;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getIsSeller() {
        return isSeller;
    }

    public void setIsSeller(long isSeller) {
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

    public long getAuditingStatus() {
        return auditingStatus;
    }

    public void setAuditingStatus(long auditingStatus) {
        this.auditingStatus = auditingStatus;
    }

    public long getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(long settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public long getAccountType() {
        return accountType;
    }

    public void setAccountType(long accountType) {
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
