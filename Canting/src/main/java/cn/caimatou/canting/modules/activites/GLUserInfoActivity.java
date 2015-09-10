package cn.caimatou.canting.modules.activites;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.GLUserInfo;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.modules.fragments.GLUserInfoFragment;
import cn.caimatou.canting.modules.logic.GLLoginLogic;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.widget.GLNavigationBar;
import cn.caimatou.canting.widget.NoSlideViewPage;

/**
 * Created by Rosicky on 15/8/30.
 */
public class GLUserInfoActivity extends GLParentActivity implements ViewPager.OnPageChangeListener {
    private View mView;
    private LayoutInflater inflater;
    private NoSlideViewPage viewPager = null;
    private MyPagerAdapter adapter = null;
    private int position;
    private GLUserInfo userInfo = null;

    private String[] mTitles = new String[]{
            GLResourcesUtil.getString(R.string.user_center),
            GLResourcesUtil.getString(R.string.edit_userinfo),
            GLResourcesUtil.getString(R.string.edit_password),
            GLResourcesUtil.getString(R.string.edit_phone)};

    @Override
    protected void initData() {
        mContext = this;
        inflater = LayoutInflater.from(mContext);
        userInfo = (GLUserInfo) getIntent().getSerializableExtra("userInfo");
        adapter = new MyPagerAdapter(getSupportFragmentManager());
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        private GLUserInfoFragment fragment;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return GLUserInfoFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            fragment = (GLUserInfoFragment) object;
            super.setPrimaryItem(container, position, object);
        }

        public GLUserInfoFragment getFragment() {
            return fragment;
        }
    }

    @Override
    protected void initView() {
        mView = inflater.inflate(R.layout.activity_user, null);
        viewPager = (NoSlideViewPage) mView.findViewById(R.id.user_viewpager);
        initViewPager(4);
    }

    private void initViewPager(final int pagers) {
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);

        // 内容填充好，默认显示第一页
        int defSelectedTab = GLConst.NONE;
        viewPager.setCurrentItem(defSelectedTab);
        viewPager.setOffscreenPageLimit(4);
    }

    public GLUserInfo getUserInfo() {
        return userInfo;
    }

    public void setShowPager(int position) {
        viewPager.setCurrentItem(position);
        setNavTitle(mTitles[position]);
    }

    public void setNavTitle(String title) {
        mNavigationBar.setNavTitle(title);
    }


    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.user_center));
    }

    private void onBack() {
        switch (position) {
            case 0:
                GLViewManager.getIns().pop(this);
                break;
            case 1:
                setShowPager(0);
                break;
            case 2:
                setShowPager(0);
                break;
            case 3:
                setShowPager(0);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onItemSelectedListener(int viewId) {
        if (viewId == R.id.ivNavLeft) {
            onBack();
            return false;
        }
        return super.onItemSelectedListener(viewId);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.position = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GLLoginLogic.REQUEST_EDIT_CANTING_INFO) {
            if (resultCode == Activity.RESULT_OK) {
                int editType = data.getExtras().getInt(GLConst.INTENT_PARAM1);
                String content = data.getExtras().getString(GLConst.INTENT_PARAM);
                if (editType == GLLoginLogic.EDIT_USER_NAME) {
                    GLUserInfoFragment f = adapter.getFragment();
                    f.refreshName(content);
                }
            }
        }
    }
}
