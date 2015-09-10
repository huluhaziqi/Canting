package cn.caimatou.canting.base;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import cn.caimatou.canting.modules.GLCantingApp;
import cn.caimatou.canting.modules.activites.GLStartActivity;
import cn.caimatou.canting.R;
import cn.caimatou.canting.callback.GLToolbarStateCallback;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.SystemBarTintManager;
import cn.caimatou.canting.utils.GLEditTextUtil;
import cn.caimatou.canting.utils.GLEventAnalysisUtil;
import cn.caimatou.canting.utils.GLLog;
import cn.caimatou.canting.utils.GLPixelsUtil;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * 可按照项目需求，将无冲突的通用函数，虚化到父类
 * Created by Fausgoal on 2015/5/5.
 */
public abstract class GLParentActivity extends AppCompatActivity implements GLToolbarStateCallback, GLViewClickUtil.NoFastClickListener {
    private static final String TAG = "GLParentActivity";

    public int mAppWidth = GLConst.NEGATIVE;
    public int mAppHeight = GLConst.NEGATIVE;
    public float mScaleDensity = GLConst.ONE;
    public Point mDeviceSizePoint = null;

    protected GLCantingApp mApp = null;
    public Context mContext = null;
    protected LayoutInflater mInflater = null;
    private InputMethodManager mIMM = null;

    private NotificationBroadcastReceiver mReceiver = null;
    private NotificationBroadcastReceiver mStickyReceiver = null;

    public Toolbar mToolbar = null;
    public LinearLayout mLlParentRoot = null;
    public LinearLayout mLlContent = null;
    public GLNavigationBar mNavigationBar = null;

    /**
     * Initiation of the data
     */
    protected abstract void initData();

    /**
     * Initiation of the components
     */
    protected abstract void initView();

    /**
     * 如果要使用toolbar，或使用默认的toolbar布局，一定要在这里初始化控件
     */
    protected abstract void initToolbarView();

    /**
     * 设置事件监听
     */
    protected void setListener() {
    }

    /**
     * notification broadcast
     *
     * @param context 上下文
     * @param intent  通知内容
     */
    protected void onNotify(Context context, Intent intent) {
    }

    /**
     * init broadcast intent filter
     *
     * @return IntentFilter
     */
    protected IntentFilter getIntentFilter() {
        return null;
    }

    /**
     * 常驻广播通知回调
     *
     * @param context 上下文
     * @param intent  通知内容
     */
    protected void onStickyNotify(Context context, Intent intent) {
    }

    /**
     * 初始化常驻广播过滤器
     *
     * @return IntentFilter
     */
    protected IntentFilter getStickyIntentFilter() {
        return null;
    }

    @Override
    public void onNoFastClick(View v) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mApp = GLCantingApp.getIns();
        mInflater = LayoutInflater.from(this);
        // 加到栈中
        GLViewManager.getIns().push(this);

        // 启动页不设置状态栏颜色
        if (!GLStartActivity.class.getName().equals(this.getClass().getName())) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setStatuBar();
            }
        }

        mDeviceSizePoint = GLPixelsUtil.getDeviceSize(this);

        mAppWidth = mDeviceSizePoint.x;

        int statebarHeight = GLPixelsUtil.dip2px(this, 25);
        mAppHeight = mDeviceSizePoint.y - statebarHeight;

        mScaleDensity = getResources().getDisplayMetrics().density;

        mIMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        register(true);
        initData();
        initView();
        // 是否需要toolbar，加载toolbar
        initToolbar();
        initToolbarView();
        setListener();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setStatuBar() {
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);
        // 设置一个颜色给系统栏
//        tintManager.setTintColor(GLResourcesUtil.getColor(R.color.green));
        tintManager.setStatusBarTintColor(GLResourcesUtil.getColor(R.color.green));
        tintManager.setNavigationBarTintColor(GLResourcesUtil.getColor(R.color.gray1));
    }

    private void initToolbar() {
        // 是否需要toolbar，加载toolbar
        if (hasToolbar()) {
            if (isUseDefaultLayoutToolbar()) {
                setContentView(R.layout.gl_toolbar_main);
            }
            if (isCreaterToolbar()) {
                mToolbar = findView(R.id.toolbar);
                mLlParentRoot = findView(R.id.llParentRoot);
                mLlContent = findView(R.id.llContent);
                mNavigationBar = new GLNavigationBar(this, mToolbar);
                mNavigationBar.setMenuitemClickListener(new GLNavigationBar.OnToolbarMenuItemClickListener() {
                    @Override
                    public boolean onViewClick(int viewId) {
                        return onItemSelectedListener(viewId);
                    }
                });
                setToolbarStyle(mNavigationBar);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        register(false);
        // 通用事务，不同处在子类实现
        GLEventAnalysisUtil.onResume(this);
    }

    @Override
    protected void onPause() {
        unregister(false);
        // 通用事务，不同处在子类实现
        super.onPause();
        GLEventAnalysisUtil.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unregister(true);
        unregister(false);
        // 通用事务，不同处在子类实现
        GLViewManager.getIns().popWithoutFinish(this);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // 通用事务，不同处在子类实现
        super.onConfigurationChanged(newConfig);
    }

    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    protected <T extends View> T findView(View parent, int id) {
        if (null == parent) {
            throw new NullPointerException("parent is not null!");
        }
        return (T) parent.findViewById(id);
    }

    protected void showInput(View view) {
        mIMM.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    protected void showInputByEnter(final EditText editText) {
        GLEditTextUtil.setEditTextFocus(editText, false);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showInput(editText);
            }
        }, 300);
    }

    protected void hideInput(View view) {
        mIMM.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected boolean isInputActive() {
        return mIMM.isActive();
    }

    @Override
    public boolean hasToolbar() {
        return true;
    }

    /**
     * 是否使用默认的toolbar布局，主界面不需要
     *
     * @return true 默认的，使用
     */
    protected boolean isUseDefaultLayoutToolbar() {
        return true;
    }

    private boolean isCreaterToolbar() {
        mToolbar = findView(R.id.toolbar);
        if (null == mToolbar)
            return false;
        return true;
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
    }

    @Override
    public boolean onItemSelectedListener(int viewId) {
        return false;
    }

    /**********************************************************************/
    /******************************broadcast receiver**********************/
    /**********************************************************************/

    /**
     * 消除常驻广播接收器
     */
    protected void detachStickyBroadcastReceiver() {
        unregister(true);
    }

    /**
     * 添加注册广播
     *
     * @param isSticky 是否常驻的广播
     */
    private void register(boolean isSticky) {
        IntentFilter filter;
        if (isSticky) {
            if (null == mStickyReceiver) {
                filter = getStickyIntentFilter();
                if (null != filter) {
                    mStickyReceiver = new NotificationBroadcastReceiver(true);
                    addBroadcastReceiver(mStickyReceiver, filter);
                }
            }
        } else {
            if (null == mReceiver) {
                filter = getIntentFilter();
                if (null != filter) {
                    mReceiver = new NotificationBroadcastReceiver(false);
                    addBroadcastReceiver(mReceiver, filter);
                }
            }
        }
    }

    /**
     * 注销广播
     *
     * @param isSticky 是否为常驻广播
     */
    private void unregister(boolean isSticky) {
        if (isSticky) {
            if (null != mStickyReceiver) {
                unregisterReceiver(mStickyReceiver);
                mStickyReceiver = null;
            }
        } else {
            if (null != mReceiver) {
                unregisterReceiver(mReceiver);
                mReceiver = null;
            }
        }
    }

    private void addBroadcastReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        try {
            registerReceiver(receiver, filter);
        } catch (Exception e) {
            GLLog.trace(TAG, "register exception " + e.getMessage());
        }
    }

    private class NotificationBroadcastReceiver extends BroadcastReceiver {
        private boolean isSticky = false;

        public NotificationBroadcastReceiver(boolean isSticky) {
            this.isSticky = isSticky;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isSticky) {
                onStickyNotify(context, intent);
            } else {
                onNotify(context, intent);
            }
        }
    }
}
