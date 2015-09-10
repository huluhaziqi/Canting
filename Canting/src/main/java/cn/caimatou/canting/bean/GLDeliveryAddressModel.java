package cn.caimatou.canting.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import cn.caimatou.canting.base.GLBaseInfo;

/**
 * Description：配送地址
 * <br/><br/>Created by Fausgoal on 15/9/2.
 * <br/><br/>
 */
@DatabaseTable(tableName = "Address")
public class GLDeliveryAddressModel extends GLBaseInfo {
    private static final long serialVersionUID = 1L;
    @DatabaseField(generatedId = true)
    private Integer _id;

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
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
    private long opUserId;
    @DatabaseField
    private long kind;
    @DatabaseField
    private long companyId;
    @DatabaseField
    private long userId;
    @DatabaseField
    private String addr;
    @DatabaseField
    private String detailAddr;
    @DatabaseField
    private String linkMan;
    @DatabaseField
    private String tel;
    @DatabaseField
    private double longitude;
    @DatabaseField
    private double latitude;
    @DatabaseField
    private int isDefault;
    @DatabaseField
    private String province;
    @DatabaseField
    private String town;
    @DatabaseField
    private String region;

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

    public long getKind() {
        return kind;
    }

    public void setKind(long kind) {
        this.kind = kind;
    }

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

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getDetailAddr() {
        return detailAddr;
    }

    public void setDetailAddr(String detailAddr) {
        this.detailAddr = detailAddr;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
