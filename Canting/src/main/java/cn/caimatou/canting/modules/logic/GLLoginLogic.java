package cn.caimatou.canting.modules.logic;

import android.app.Activity;
import android.content.Context;

import cn.caimatou.canting.modules.activites.GLLoginActivity;
import cn.caimatou.canting.modules.activites.GLMainActivity;
import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.common.GLCommonVariables;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.manager.GLUserManager;
import cn.caimatou.canting.utils.GLProgressDialogUtil;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLStringUtil;
import cn.caimatou.canting.utils.MD5;
import cn.caimatou.canting.utils.http.GLApiHandler;

/**
 * Description：登录部分逻辑类
 * <br/><br/>Created by Fausgoal on 15/8/28.
 * <br/><br/>
 */
public class GLLoginLogic {
    private static final String TAG = "GLLoginLogic";
    /**
     * 如果有密码时，跳转到登录界面，用这个值代替密码显示
     */
    public static final String DEFAULT_PWD = "default_pwd";

    /**
     * 编辑餐厅信息回来的requestCode
     */
    public static final int REQUEST_EDIT_CANTING_INFO = 1;
    /**
     * 选择岗位的requestCode
     */
    public static final int REQUEST_POST_CODE = 2;

    /**
     * 编辑餐厅名称
     */
    public static final int EDIT_CANTING_NAME = 1;
    /**
     * 编辑省、市、区
     */
    public static final int EDIT_CANTING_CITY = 2;
    /**
     * 编辑餐厅地址
     */
    public static final int EDIT_CANTING_ADDRESS = 3;
    /**
     * 编辑联系人
     */
    public static final int EDIT_CANTING_CONTACT = 4;
    /**
     * 编辑餐厅电话
     */
    public static final int EDIT_CANTING_PHONE = 5;
    /**
     * 编辑在餐厅的岗位
     */
    public static final int EDIT_CANTING_QUARTERS = 6;
    /**
     * 编辑用户名称nickName
     */
    public static final int EDIT_USER_NAME = 7;
    /**
     * 编辑餐厅简介
     */
    public static final int EDIT_CANTING_BRIEF = 8;
    /**
     * 编辑餐厅联系人
     */
    public static final int EDIT_CANTING_LINKMAN = 9;
    /**
     * 编辑联系人性别
     */
    public static final int EDIT_CANTING_SEX = 10;
    /**
     * 编辑联系人电话
     */
    public static final int EDIT_CANTING_LINKPHONE = 11;


    private final Context mContext;

    public GLLoginLogic(Context context) {
        mContext = context;
    }

    /**
     * 获取编辑餐厅信息的标题
     *
     * @param editType type
     * @return title
     */
    public String getEditCantingTitle(int editType) {
        int resId;
        switch (editType) {
            case EDIT_USER_NAME:
                resId = R.string.user_nickname;
                break;
            case EDIT_CANTING_NAME:
                resId = R.string.canting_name;
                break;
            case EDIT_CANTING_CITY:
                resId = R.string.address;
                break;
            case EDIT_CANTING_ADDRESS:
                resId = R.string.detail_address;
                break;
            case EDIT_CANTING_CONTACT:
                resId = R.string.contact;
                break;
            case EDIT_CANTING_PHONE:
                resId = R.string.contact_mobile_phone;
                break;
            case EDIT_CANTING_QUARTERS:
                resId = R.string.quarters;
                break;
            case EDIT_CANTING_BRIEF:
                resId = R.string.brief;
                break;
            case EDIT_CANTING_LINKMAN:
                resId = R.string.linkman;
                break;
            case EDIT_CANTING_SEX:
                resId = R.string.user_sex;
                break;
            case EDIT_CANTING_LINKPHONE:
                resId = R.string.linkman_tel;
                break;
            default:
                resId = 0;
                break;
        }
        return GLResourcesUtil.getString(resId);
    }

    public void login(final String account, String password) {
        GLProgressDialogUtil.showProgress(mContext, GLResourcesUtil.getString(R.string.start_login));
        boolean isMd5 = true;
        // 如果密码是本地保存的密码，就不用md5了直接使用
        if (DEFAULT_PWD.equals(password)) {
            isMd5 = false;
            // 取本地的密码
            password = (String) GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.PASSWORD);
        }
        String tempPwd = password;
        if (isMd5) {
            tempPwd = MD5.md5(password);
        }
        GLUserManager.onLogin(mContext, account, tempPwd, new LoginHandler(mContext, false));
    }

    public static class LoginHandler extends GLApiHandler {
        private final boolean isAutoLogin;

        public LoginHandler(Context context, boolean isAutoLogin) {
            super(context);
            this.isAutoLogin = isAutoLogin;
        }

        @Override
        public void onSuccess(GLBaseInfo baseInfo) {
            super.onSuccess(baseInfo);
            GLProgressDialogUtil.dismissProgress(mContext);
            if (isAutoLogin) {
                GLViewManager.getIns().showActivity(mContext,
                        GLMainActivity.class, true,
                        R.anim.gl_fade_in, R.anim.gl_scale_fade_out);
            } else {
                GLViewManager.getIns().popWithoutFinishThisActivity((Activity) mContext);
                GLViewManager.getIns().showActivity(mContext, GLMainActivity.class, true);
            }
        }

        @Override
        public void onFailure(GLBaseInfo baseInfo) {
            super.onFailure(baseInfo);
            GLProgressDialogUtil.dismissProgress(mContext);
            if (null != baseInfo && GLStringUtil.isNotEmpty(baseInfo.getMessage())) {
                GLCommonManager.makeText(mContext, baseInfo.getMessage());
            }

            // 如果是自动登录，登录失败要跳转到登录界面
            if (isAutoLogin) {
                GLViewManager.getIns().showActivity(mContext,
                        GLLoginActivity.class, true,
                        R.anim.gl_fade_in, R.anim.gl_scale_fade_out);
            }
        }
    }
}
