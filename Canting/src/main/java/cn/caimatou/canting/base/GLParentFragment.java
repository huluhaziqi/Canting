package cn.caimatou.canting.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import cn.caimatou.canting.callback.GLToolbarStateCallback;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.utils.GLEditTextUtil;
import cn.caimatou.canting.utils.GLLog;
import cn.caimatou.canting.utils.GLPixelsUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;
import cn.caimatou.canting.widget.GLNavigationBar;


/**
 * 可按照项目需求，将无冲突的通用函数，虚化到父类
 * Created by Fu on 2015/5/5.
 */
public abstract class GLParentFragment extends Fragment implements GLToolbarStateCallback, GLViewClickUtil.NoFastClickListener {
    private static final String TAG = "GLParentFragment";

    public int mAppWidth = GLConst.NEGATIVE;
    public int mAppHeight = GLConst.NEGATIVE;
    public float mScaleDensity = GLConst.ONE;
    public Point mDeviceSizePoint = null;

    public Context mContext = null;
    protected GLParentActivity mParentActivity = null;

    private InputMethodManager mIMM = null;

    private GLNavigationBar mNavigationBar = null;//导航条

    private NotificationBroadcastReceiver mReceiver = null;//广播通知接收
    private NotificationBroadcastReceiver mStickyReceiver = null;

    /**
     * Initiation of the data
     */
    protected abstract void initData();

    /**
     * Initiation of the components
     */
    protected abstract void initView();

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

    protected void setListener() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();//获得上下文
        mParentActivity = (GLParentActivity) getActivity();//获得activity

        // 加到栈中
        GLViewManager.getIns().push(getActivity());

        mDeviceSizePoint = GLPixelsUtil.getDeviceSize(getActivity());

        mAppWidth = mDeviceSizePoint.x;

        mIMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        int statebarHeight = GLPixelsUtil.dip2px(mContext, 25);
        mAppHeight = mDeviceSizePoint.y - statebarHeight;

        mScaleDensity = getResources().getDisplayMetrics().density;

        register(true);
        initData();
        initView();
        setListener();
        // 是否需要toolbar，加载toolbar
        if (hasToolbar() && isCreaterToolbar()) {
            setToolbarStyle(mNavigationBar);
        }
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
        mIMM.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        register(false);
        // 通用事务，不同处在子类实现
    }

    @Override
    public void onPause() {
        unregister(false);
        // 通用事务，不同处在子类实现
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        unregister(true);
        unregister(false);
        // 通用事务，不同处在子类实现
        GLViewManager.getIns().popWithoutFinish(getActivity());
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // 通用事务，不同处在子类实现
        super.onConfigurationChanged(newConfig);
    }

    protected <T extends View> T findView(View parent, int id) {
        if (null == parent) {
            throw new NullPointerException("parent is not null!");
        }
        return (T) parent.findViewById(id);
    }

    @Override
    public boolean hasToolbar() {
        return true;
    }

    private boolean isCreaterToolbar() {
        mNavigationBar = mParentActivity.mNavigationBar;
        if (null == mNavigationBar)
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

    @Override
    public void onNoFastClick(View v) {
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
                getActivity().unregisterReceiver(mStickyReceiver);
                mStickyReceiver = null;
            }
        } else {
            if (null != mReceiver) {
                getActivity().unregisterReceiver(mReceiver);
                mReceiver = null;
            }
        }
    }

    private void addBroadcastReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        try {
            getActivity().registerReceiver(receiver, filter);
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
