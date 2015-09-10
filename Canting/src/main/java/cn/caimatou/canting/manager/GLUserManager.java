package cn.caimatou.canting.manager;

import android.app.Activity;
import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.bean.GLUserInfo;
import cn.caimatou.canting.common.GLCommonVariables;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.modules.activites.GLLoginActivity;
import cn.caimatou.canting.modules.activites.GLMainActivity;
import cn.caimatou.canting.modules.activites.GLUserInfoActivity;
import cn.caimatou.canting.response.GLBooleanResp;
import cn.caimatou.canting.response.GLUserInfoResp;
import cn.caimatou.canting.utils.GLListUtil;
import cn.caimatou.canting.utils.GLNumberUtils;
import cn.caimatou.canting.utils.GLProgressDialogUtil;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLStringUtil;
import cn.caimatou.canting.utils.MD5;
import cn.caimatou.canting.utils.http.GLApiHandler;
import cn.caimatou.canting.utils.http.GLHttpRequestHelper;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 2015/8/31.
 * <br/><br/>
 */
public class GLUserManager {
    private static boolean isLogin = false;

    /**
     * 登录状态
     *
     * @return true 已登录，false 未登录
     */
    public static boolean isLogin() {
        return isLogin;
    }

    private GLUserManager() {
    }

    /**
     * 调用API实现网络登录
     *
     * @param context  context
     * @param account  account
     * @param password password
     * @param handler  handler
     */
    public static void onLogin(final Context context, final String account, final String password, final GLApiHandler handler) {
        GLHttpRequestHelper.login(context, account, password, new GLApiHandler(context) {
            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);

                GLUserInfoResp userInfoResp = (GLUserInfoResp) baseInfo;
                final GLUserInfo info = userInfoResp.getResult();
                saveLoginInfo(mContext, info, password);

                isLogin = true;
                handler.onSuccess(baseInfo);
            }

            @Override
            public void onFailure(GLBaseInfo baseInfo) {
                super.onFailure(baseInfo);
                isLogin = false;
                clearLoginInfo();
                handler.onFailure(baseInfo);
            }
        });
    }

    /**
     * 调用本地保存的账号密码，实现自动登录
     *
     * @param context context
     * @param handler handler
     */
    public static void onLogin(final Context context, final GLApiHandler handler) {
        String account = (String) GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.ACCOUNT);
        String password = (String) GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.PASSWORD);
        onLogin(context, account, password, handler);
    }

    /**
     * 退出登录
     *
     * @param context context
     */
    public static void logout(final Context context) {
        GLProgressDialogUtil.showProgress(context, GLResourcesUtil.getString(R.string.logouting));

        GLHttpRequestHelper.logout(context, new GLApiHandler(context) {
            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);

                clearLoginInfo();

                GLProgressDialogUtil.dismissProgress(mContext);

                isLogin = false;
                GLViewManager.getIns().popWithoutFinishThisActivity((Activity) context);
                GLViewManager.getIns().showActivity(mContext, GLLoginActivity.class, false);

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GLViewManager.getIns().pop(GLMainActivity.class);
                    }
                }, 10);
            }

            @Override
            public void onFailure(GLBaseInfo baseInfo) {
                super.onFailure(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);
            }
        });
    }

    /**
     * 更新用户密码
     *
     * @param context
     * @param oldPassword
     * @param newPassword
     */
    public static void updatePassword(final Context context, String oldPassword, String newPassword) {
        GLProgressDialogUtil.showProgress(context, GLResourcesUtil.getString(R.string.password_changing));

        GLHttpRequestHelper.updatePassword(context, MD5.md5(oldPassword), MD5.md5(newPassword), new GLApiHandler(context) {
            @Override
            public void onFailure(GLBaseInfo baseInfo) {
                super.onFailure(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);
                GLCommonManager.makeText(mContext, baseInfo.getMessage());
            }

            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);
                boolean isChanged = ((GLBooleanResp) baseInfo).getResult();
                if (isChanged) {
                    GLCommonManager.makeText(mContext, baseInfo.getMessage());
                    ((GLUserInfoActivity) context).setShowPager(0);
                } else {
                    GLCommonManager.makeText(mContext, baseInfo.getMessage());
                }
            }
        });
    }

    public static void updateUserInfo(final Context context, long userId, final String nickName, final int sex, final String birthday) {
        GLProgressDialogUtil.showProgress(context, GLResourcesUtil.getString(R.string.userinfo_updating));

        GLHttpRequestHelper.updateUserInfo(context, userId, nickName, sex, birthday, new GLApiHandler(context) {
            @Override
            public void onFailure(GLBaseInfo baseInfo) {
                super.onFailure(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);
//                GLCommonManager.makeText(mContext, baseInfo.getMessage());
            }

            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);
                boolean isChanged = ((GLBooleanResp) baseInfo).getResult();
                if (isChanged) {
                    GLUserInfo userInfo = (GLUserInfo) GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.USER);
                    userInfo.setNickname(nickName);
                    userInfo.setSex(sex);
                    userInfo.setBirthday(birthday);
                    GLCommonVariables.getIns().saveObject(GLCommonVariables.Keys.USER, userInfo);
                    ((GLUserInfoActivity) context).setShowPager(0);
                } else {
                    GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.update_info_failure));
                }
            }
        });
    }

    public static void saveLoginInfo(Context context, GLUserInfo info, String password) {
        GLCommonVariables.getIns().saveObject(GLCommonVariables.Keys.ACCOUNT, info.getLoginName());
        GLCommonVariables.getIns().saveObject(GLCommonVariables.Keys.PASSWORD, password);
        GLCommonVariables.getIns().saveObject(GLCommonVariables.Keys.TOKEN, info.getToken());
        GLCommonVariables.getIns().saveObject(GLCommonVariables.Keys.USER, info);
        GLCommonVariables.getIns().saveObject(GLCommonVariables.Keys.USERID, info.getId());

        // 将用户信息保存
        GLUserInfo locUserInfo = getUserInfo(context, info.getId(), info.getLoginName());
        if (null != locUserInfo) {
            info.set_id(locUserInfo.get_id());
        }

        try {
            GLDatabaseManager.replaceUserInfo(context, info);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clearLoginInfo() {
        GLCommonVariables.getIns().saveObject(GLCommonVariables.Keys.PASSWORD, "");
        GLCommonVariables.getIns().saveObject(GLCommonVariables.Keys.USER, "");
        GLCommonVariables.getIns().saveObject(GLCommonVariables.Keys.TOKEN, "");
        GLCommonVariables.getIns().saveObject(GLCommonVariables.Keys.USERID, "");
    }

    public static GLUserInfo getUserInfo(Context context, long id, String loginName) {
        try {
            List<GLUserInfo> userInfos = (List<GLUserInfo>) GLDatabaseManager.queryUserInfo(context, new String[]{"id", "loginName"}, new Object[]{id, loginName});
            if (!GLListUtil.isEmpty(userInfos)) {
                return userInfos.get(GLConst.NONE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static GLUserInfo getUserInfo(Context context, String loginName) {
        try {
            List<GLUserInfo> userInfos = (List<GLUserInfo>) GLDatabaseManager.queryUserInfo(context, new String[]{"loginName"}, new Object[]{loginName});
            if (!GLListUtil.isEmpty(userInfos)) {
                return userInfos.get(GLConst.NONE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getToken(Context context) {
        String token = (String) GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.TOKEN);
        if (GLStringUtil.isEmpty(token)) {
            String loginName = (String) GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.ACCOUNT);
            if (GLStringUtil.isNotEmpty(loginName)) {
                GLUserInfo userInfo = getUserInfo(context, loginName);
                if (null != userInfo) {
                    token = userInfo.getToken();
                }
            }
        }
        return token;
    }

    public static long getUserId(Context context) {
        long userId = GLConst.NONE;
        Object obj = GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.USERID);
        if (null == obj) {
            String loginName = (String) GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.ACCOUNT);
            if (GLStringUtil.isNotEmpty(loginName)) {
                GLUserInfo userInfo = getUserInfo(context, loginName);
                if (null != userInfo) {
                    userId = userInfo.getId();
                }
            }
        } else {
            userId = GLNumberUtils.stringToLong(obj.toString());
        }
        return userId;
    }

}
