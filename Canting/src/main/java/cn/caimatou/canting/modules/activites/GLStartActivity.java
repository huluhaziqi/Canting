package cn.caimatou.canting.modules.activites;

import android.os.Handler;
import android.os.Message;

import java.util.concurrent.TimeUnit;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.common.GLCommonVariables;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLUserManager;
import cn.caimatou.canting.modules.GLCantingApp;
import cn.caimatou.canting.modules.logic.GLLoginLogic;
import cn.caimatou.canting.utils.GLStringUtil;

/**
 * Description：启动页、登录页
 * <br/><br/>Created by Fausgoal on 15/8/28.
 * <br/><br/>
 */
public class GLStartActivity extends GLParentActivity implements Runnable {

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_start_layout);
        // NOTICE 判断第一次进来要加载欢迎页，后面启动时未登录到登录界面，已登录要执行登录操作
        String account = (String) GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.ACCOUNT);
        String password = (String) GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.PASSWORD);
        if (GLStringUtil.isEmpty(account) || GLStringUtil.isEmpty(password)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    GLViewManager.getIns().showActivity(mContext,
                            GLLoginActivity.class, true,
                            R.anim.gl_no_anim, R.anim.gl_no_anim);
                    overridePendingTransition(R.anim.gl_no_anim, R.anim.gl_no_anim);
                }
            }, 500);
        } else {
            localLogin();
        }
    }

    @Override
    protected void initToolbarView() {
    }

    @Override
    protected boolean isUseDefaultLayoutToolbar() {
        return false;
    }

    private void localLogin() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mHandler.sendEmptyMessage(GLConst.NONE);
        }
    }

    private Handler mHandler = new Handler(GLCantingApp.getIns().getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GLConst.NONE:
                    // 执行自动登录
                    GLUserManager.onLogin(mContext, new GLLoginLogic.LoginHandler(mContext, true));
                    break;
            }
        }
    };
}
