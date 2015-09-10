package cn.caimatou.canting.bean;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import cn.caimatou.canting.base.GLBaseInfo;

/**
 * Description：用户 <br/><br/>Created by Fausgoal on 2015/8/31. <br/><br/>
 */
@DatabaseTable(tableName = "UserInfo")
public class GLUserInfo extends GLBaseInfo {
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
    private long id;                // id
    @DatabaseField
    private String loginName;       // 登录手机号
    @DatabaseField
    private String nickname;        // 昵称
    @DatabaseField
    private String face;            // 头像
    @DatabaseField
    private String code;            // 菜号
    @DatabaseField
    private int sex;                // 性别
    @DatabaseField
    private String token;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private GLCompUser compUser;
    @DatabaseField
    private String birthday;        // 生日

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public GLCompUser getCompUser() {
        return compUser;
    }

    public void setCompUser(GLCompUser compUser) {
        this.compUser = compUser;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


}