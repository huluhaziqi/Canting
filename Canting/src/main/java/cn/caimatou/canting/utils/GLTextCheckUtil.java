package cn.caimatou.canting.utils;

import cn.caimatou.canting.R;
import cn.caimatou.canting.common.GLCommonVariables;
import cn.caimatou.canting.common.GLConst;

/**
 * Description：相关验证的工具类
 * <br/><br/>Created by Fausgoal on 15/8/28.
 * <br/><br/>
 */
public class GLTextCheckUtil {
    /**
     * 验证码的长度
     */
    private static final int LENGTH_V_CODE = 4;
    private static final int LENGTH_MIN_PWD = 6;
    private static final int LENGTH_MAX_PWD = 16;

    /**
     * 验证结果返回类
     */
    public static class GLCheckResult {
        private boolean isSucessed = true;

        public String mFailMsg = "";

        public boolean isSuccess() {
            return isSucessed;
        }
    }

    private GLTextCheckUtil() {
    }

    /**
     * 验证登录账号、密码
     *
     * @param account  账号
     * @param password 密码
     * @return result
     */
    public static GLCheckResult checkLogin(CharSequence account, CharSequence password) {
        GLCheckResult result = new GLCheckResult();
        if (GLStringUtil.isEmpty(account)) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_account);
            return result;
        } else if (account.length() != GLConst.NONE && account.toString().trim().length() <= GLConst.NONE) {
            // 长度不为0，去掉空格后为0，表示输入的是空格
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.account_isnotSpace);
            return result;
        }

        if (GLStringUtil.isEmpty(password)) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_login_password);
            return result;
        } else if (password.length() != GLConst.NONE && password.toString().trim().length() <= GLConst.NONE) {
            // 长度不为0，去掉空格后为0，表示输入的是空格
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.password_isnotSpace);
            return result;
        }
        return result;
    }

    /**
     * 判断密码是否为用户当前密码
     *
     * @param password
     * @return
     */
    public static GLCheckResult checkPassword(CharSequence password) {
        GLCheckResult result = new GLCheckResult();
        String pwd = (String) GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.PASSWORD);
        if (GLStringUtil.isEmpty(password)) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_login_password);
            return result;
        } else if (password.length() != GLConst.NONE && password.toString().trim().length() <= GLConst.NONE) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.password_isnotSpace);
            return result;
        } else if (!(MD5.md5(String.valueOf(password))).equals(pwd)) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.password_wrong);
            return result;
        }
        return result;
    }

    /**
     * 判断输入密码格式是否正确
     *
     * @param password
     * @return
     */
    public static GLCheckResult checkPasswordStyle(CharSequence password) {
        GLCheckResult result = new GLCheckResult();
        if (GLStringUtil.isEmpty(password)) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_login_password);
            return result;
        } else if (password.length() != GLConst.NONE && password.toString().trim().length() <= GLConst.NONE) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.password_isnotSpace);
            return result;
        }
        return result;
    }

    /**
     * 验证是否为手机号
     *
     * @param phoneNo 手机号
     * @return 验证结果
     */
    public static GLCheckResult checkMobilePhone(CharSequence phoneNo) {
        GLCheckResult result = new GLCheckResult();
        if (GLStringUtil.isEmpty(phoneNo)) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_mobile_phone);
            return result;
        }
        if (!GLRegexUtil.isMobilePhone(phoneNo)) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.mobile_phone_is_error);
            return result;
        }
        return result;
    }

    /**
     * 验证 验证码是否正确
     *
     * @param code 验证码
     * @return result
     */
    public static GLCheckResult checkVCode(CharSequence code) {
        GLCheckResult result = new GLCheckResult();
        if (GLStringUtil.isEmpty(code)) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_code);
            return result;
        } else if (code.length() < LENGTH_V_CODE) {
            String tips = GLResourcesUtil.getString(R.string.vcode_length_error);
            result.isSucessed = false;
            result.mFailMsg = String.format(tips, LENGTH_V_CODE);
            return result;
        }
        return result;
    }

    public static GLCheckResult checkPassword(CharSequence newPwd, CharSequence confirmPwd) {
        GLCheckResult result = new GLCheckResult();
        if (GLStringUtil.isEmpty(newPwd)
                || (newPwd.length() != GLConst.NONE && newPwd.toString().trim().length() <= GLConst.NONE)) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_new_password);
            return result;
        } else if (newPwd.length() < LENGTH_MIN_PWD || newPwd.length() > LENGTH_MAX_PWD) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.password_length_error);
            return result;
        }
        if (GLStringUtil.isEmpty(confirmPwd)
                || (confirmPwd.length() != GLConst.NONE && confirmPwd.toString().trim().length() <= GLConst.NONE)) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_confirm_password);
            return result;
        }
        if (!newPwd.toString().equals(confirmPwd.toString())) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_confirm_password_again);
            return result;
        }
        return result;
    }

    public static GLCheckResult checkCantingInfo(CharSequence cantingName, CharSequence cantingCity,
                                                 CharSequence cantingAddress, CharSequence cantingContact,
                                                 CharSequence cantingPhone, CharSequence cantingQuarters) {
        GLCheckResult result = new GLCheckResult();
        if (GLStringUtil.isEmpty(cantingName)) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_canting_name);
            return result;
        }

        if (GLStringUtil.isEmpty(cantingCity)) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_canting_city);
            return result;
        }
        if (GLStringUtil.isEmpty(cantingAddress)) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_detail_address);
            return result;
        }
        if (GLStringUtil.isEmpty(cantingContact)) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_contact);
            return result;
        }
        if (GLStringUtil.isEmpty(cantingPhone)) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_contact_phone);
            return result;
        }
        if (GLStringUtil.isEmpty(cantingQuarters)) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_quarters);
            return result;
        }
        return result;
    }

    public static GLCheckResult checkAddCaipin(CharSequence caiName, CharSequence caiCount, CharSequence caiUnit, CharSequence otherUnit) {
        GLCheckResult result = new GLCheckResult();
        if (GLStringUtil.isEmpty(caiName) || GLStringUtil.isEmpty(caiName.toString().trim())) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_cai_name);
            return result;
        }
//        else if (!GLRegexUtil.isNumAndChaterAndChiness(caiName)) {
//            result.isSucessed = false;
//            result.mFailMsg = GLResourcesUtil.getString(R.string.cai_name_is_error);
//            return result;
//        }

        if (GLStringUtil.isEmpty(caiCount) || GLStringUtil.isEmpty(caiCount.toString().trim())) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_cai_count);
            return result;
        } else {
            int count = GLNumberUtils.stringToInt(caiCount.toString());
            if (count <= GLConst.NONE) {
                result.isSucessed = false;
                result.mFailMsg = GLResourcesUtil.getString(R.string.cai_count_is_error);
                return result;
            }
        }
        if (GLStringUtil.isEmpty(caiUnit) || GLStringUtil.isEmpty(caiUnit.toString().trim())) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.select_unit);
            return result;
        }
        if (GLResourcesUtil.getString(R.string.other).equals(caiUnit)) {
            if (GLStringUtil.isEmpty(otherUnit) || GLStringUtil.isEmpty(otherUnit.toString().trim())) {
                result.isSucessed = false;
                result.mFailMsg = GLResourcesUtil.getString(R.string.enter_other_unit);
                return result;
            }
        }
        return result;
    }

    public static GLCheckResult checkCommitOrder(CharSequence deliveryTime, CharSequence deliveryAddress) {
        GLCheckResult result = new GLCheckResult();
        if (GLStringUtil.isEmpty(deliveryTime) || GLStringUtil.isEmpty(deliveryTime.toString().trim())) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.selected_delivery_time);
            return result;
        }

        if (GLStringUtil.isEmpty(deliveryAddress) || GLStringUtil.isEmpty(deliveryAddress.toString().trim())) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.selected_delivery_address);
            return result;
        }
        return result;
    }

    public static GLCheckResult checkAddress(CharSequence cityAndDistrict, CharSequence address, CharSequence cantingName, CharSequence linkMan, CharSequence tel) {
        GLCheckResult result = new GLCheckResult();
        if (GLStringUtil.isEmpty(cityAndDistrict) || GLStringUtil.isEmpty(cityAndDistrict.toString().trim())) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.selected_city_and_district);
            return result;
        }

        if (GLStringUtil.isEmpty(address) || GLStringUtil.isEmpty(address.toString().trim())) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_detail_address);
            return result;
        }

//        if (GLStringUtil.isEmpty(cantingName) || GLStringUtil.isEmpty(cantingName.toString().trim())) {
//            result.isSucessed = false;
//            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_canting_name);
//            return result;
//        }

        if (GLStringUtil.isEmpty(linkMan) || GLStringUtil.isEmpty(linkMan.toString().trim())) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_contact);
            return result;
        }

        if (GLStringUtil.isEmpty(tel) || GLStringUtil.isEmpty(tel.toString().trim())) {
            result.isSucessed = false;
            result.mFailMsg = GLResourcesUtil.getString(R.string.enter_contact_phone);
            return result;
        } else {
            result = checkMobilePhone(tel);
        }
        return result;
    }

}
