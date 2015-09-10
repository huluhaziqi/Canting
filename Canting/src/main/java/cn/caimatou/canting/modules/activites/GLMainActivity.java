package cn.caimatou.canting.modules.activites;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLCustomBroadcast;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.modules.fragments.GLBaocaiFragment;
import cn.caimatou.canting.modules.fragments.GLHistoryBaocaiFragment;
import cn.caimatou.canting.modules.fragments.GLMineFragment;
import cn.caimatou.canting.modules.fragments.GLProviderFragment;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;
import cn.caimatou.canting.widget.GLCustomBottomTabar;


public class GLMainActivity extends GLParentActivity implements ViewPager.OnPageChangeListener,
        GLViewClickUtil.NoFastClickListener, GLCustomBottomTabar.IGLCustomTabarCallback {

    private static final String TAG = "GLMainActivity";

    private ViewPager vpViewPager = null;
    private GLCustomBottomTabar mCustomBottomTabar = null;

    // tabar显示的标题
    private String[] mTitles = new String[]{
            GLResourcesUtil.getString(R.string.gl_listVege),
            GLResourcesUtil.getString(R.string.gl_historyListVege),
            GLResourcesUtil.getString(R.string.gl_provider),
            GLResourcesUtil.getString(R.string.gl_mine)};

    private List<Fragment> mFragments = null;

    @Override
    protected void initData() {
        mContext = this;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main_layout);

        vpViewPager = findView(R.id.vpViewPager);
        mCustomBottomTabar = findView(R.id.customBottomTabar);
        vpViewPager.setOffscreenPageLimit(4);

        mCustomBottomTabar.setCustomTabarCallback(this);
        // 默认显示的图标
        int[] iconIds_nor = new int[]{
                R.mipmap.icon_baocai_unselected,
                R.mipmap.icon_history_baocai_unselected,
                R.mipmap.icon_provider_unselected,
                R.mipmap.icon_mine_unselected};
        // 选中显示的图标
        int[] iconIds_press = new int[]{
                R.mipmap.icon_baocai_selected,
                R.mipmap.icon_history_baocai_selected,
                R.mipmap.icon_provider_selected,
                R.mipmap.icon_mine_selected};
        mCustomBottomTabar.setIconIds_nor(iconIds_nor);
        mCustomBottomTabar.setIconIds_press(iconIds_press);
        mCustomBottomTabar.setTitles(mTitles);
        mCustomBottomTabar.initTabar();

        initFragments();
    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(new GLBaocaiFragment());
        mFragments.add(new GLHistoryBaocaiFragment());
        mFragments.add(new GLProviderFragment());
        mFragments.add(new GLMineFragment());
    }

    @Override
    protected void initToolbarView() {
    }

    @Override
    protected void setListener() {
    }

    /**
     * 填充viewPager
     */
    private void initViewPager(final int tabarCount) {
        vpViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (null == mFragments) {
                    mFragments = new ArrayList<>();
                    initFragments();
                }
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return tabarCount;
            }
        });
        vpViewPager.setOnPageChangeListener(this);

        // 内容都填充好了，设置默认选中的tab
        int defSelectedTab = GLConst.NONE;
        vpViewPager.setCurrentItem(defSelectedTab);
        mCustomBottomTabar.setSelectedTabView(defSelectedTab);
    }

    private int beforePostion = GLConst.NEGATIVE;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // 第一次进入时设置标题
        if (position == GLConst.NONE && beforePostion == GLConst.NEGATIVE) {
            setNavTitle(position);
            beforePostion = position;
        }
    }

    @Override
    public void onPageSelected(int position) {
        mCustomBottomTabar.resetTabView();
        mCustomBottomTabar.setSelectedTabView(position);
        setNavTitle(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onClickSelectedItem(int position) {
        // 点击tabar item的回调
        vpViewPager.setCurrentItem(position, false);
        setNavTitle(position);
    }

    /**
     * 设置显示导航栏
     *
     * @param position 当前选中的tab
     */
    private void setNavTitle(int position) {
        if (null != mNavigationBar) {
            mNavigationBar.clear();
            switch (position) {
                case 0:
                    mNavigationBar.setNavLogo(R.mipmap.icon_baocai);
                    mNavigationBar.setNavRightIcon(R.mipmap.icon_message, true);
                    break;
                case 1:
                    mNavigationBar.setNavRightIcon(R.drawable.btn_search_selector);
                    break;
                case 2:
                    mNavigationBar.setNavRightIcon(R.drawable.btn_search_selector);
                    break;
                case 3:
                    break;
            }

            if (null != mTitles && mTitles.length >= position) {
                String title = mTitles[position];
                mNavigationBar.setNavTitle(title);
            }
        }
    }

    @Override
    public void getTabCount(int tabarCount, String[] titles) {
        // 初始化viewPager填充tab界面内容
        initViewPager(tabarCount);
    }

    @Override
    public void onNoFastClick(View v) {
    }

    @Override
    public boolean isUseDefaultLayoutToolbar() {
        return false;
    }

    @Override
    public boolean onItemSelectedListener(int viewId) {
        switch (viewId) {
            case R.id.tvNavLeft:
                switch (vpViewPager.getCurrentItem()) {
                    case 0:
                        break;
                }
                break;
            case R.id.ivNavRight:
                Intent intent;
                switch (vpViewPager.getCurrentItem()) {
                    case 0:
                        GLCommonManager.makeText(this, "点击消息");
                        break;
                    case 1:
                        intent = new Intent(mContext, GLSearchActivity.class);
                        intent.putExtra(GLConst.INTENT_PARAM, GLSearchActivity.SEARCH_BAOCAI_REPORT);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(mContext, GLSearchActivity.class);
                        intent.putExtra(GLConst.INTENT_PARAM, GLSearchActivity.SEARCH_PROVIDER);
                        startActivity(intent);
                        break;
                }
                break;
        }
        return true;
    }

    @Override
    protected IntentFilter getStickyIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(GLCustomBroadcast.ON_CHANGE_TO_PROVIDER_ACTION);
        return filter;
    }

    @Override
    protected void onStickyNotify(Context context, Intent intent) {
        String action = intent.getAction();
        // 切换到供应商界面
        if (GLCustomBroadcast.ON_CHANGE_TO_PROVIDER_ACTION.equals(action)) {
            vpViewPager.setCurrentItem(2, false);
        }
    }
}
