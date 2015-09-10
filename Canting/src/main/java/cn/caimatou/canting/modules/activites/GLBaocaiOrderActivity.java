package cn.caimatou.canting.modules.activites;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.base.GLParentFragment;
import cn.caimatou.canting.bean.Company;
import cn.caimatou.canting.bean.Order;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLDialogManager;
import cn.caimatou.canting.modules.fragments.GLBaocaiOrderFragment;
import cn.caimatou.canting.modules.fragments.GLHistoryBaocaiFragment;
import cn.caimatou.canting.modules.fragments.GLProviderInfoFragment;
import cn.caimatou.canting.modules.logic.GLBaocaiLogic;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.widget.GLNavigationBar;
import cn.caimatou.canting.widget.SlidingTabLayout;

/**
 * Description：报菜订单界面
 * <br/><br/>Created by Fausgoal on 15/8/30.
 * <br/><br/>
 */
public class GLBaocaiOrderActivity extends GLParentActivity {
    private static final String TAG = "GLBaocaiOrderActivity";

    private Company mShopInfo = null;
    private Order mOrder = null;
    private int mEnterType;

    private View mView = null;

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mEnterType = bundle.getInt(GLConst.INTENT_PARAM1);
            if (mEnterType == GLBaocaiLogic.ENTER_ORDER_FOR_MY_ORDER) {
                mOrder = (Order) bundle.get(GLConst.INTENT_PARAM);
                // NOTICE 从本地根据供应商id查询供应商信息
                mShopInfo = new Company();
                // todo
            } else if (mEnterType == GLBaocaiLogic.ENTER_ORDER_FOR_PROVIDER) {
                mShopInfo = (Company) bundle.get(GLConst.INTENT_PARAM);
            }
        }
    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.activity_baocai_order_layout, null, false);
        SlidingTabLayout stlSlidingTabs = findView(mView, R.id.stlSlidingTabs);
        ViewPager vpViewPager = findView(mView, R.id.vpViewPager);
        vpViewPager.setOffscreenPageLimit(3);

        String[] titles = new String[]{
                GLResourcesUtil.getString(R.string.gl_listVege),
                GLResourcesUtil.getString(R.string.baocai_report),
                GLResourcesUtil.getString(R.string.provider_detail)};
        vpViewPager.setAdapter(new GLSlidingTabsViewPagerAdapter(getSupportFragmentManager(), titles));

        stlSlidingTabs.setDistributeEvenly(true);
        stlSlidingTabs.setSelectedIndicatorColors(GLResourcesUtil.getColor(R.color.green));
        stlSlidingTabs.setSelectedIndicatorColors(GLResourcesUtil.getColor(R.color.green));
        stlSlidingTabs.setTextStyle(16, GLResourcesUtil.getColor(R.color.black1), GLResourcesUtil.getColor(R.color.green));
        stlSlidingTabs.setBackgroundColor(GLResourcesUtil.getColor(R.color.white));
        stlSlidingTabs.setViewPager(vpViewPager);
        stlSlidingTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return GLResourcesUtil.getColor(R.color.green);
            }
        });
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        if (null != mShopInfo) {
            navBar.setNavTitle(mShopInfo.getName());
        } else {
            navBar.setNavTitle(GLResourcesUtil.getString(R.string.provider_name));
        }
        navBar.setNavRightIcon(R.drawable.btn_call_selector);
    }

    private void onBack() {
        GLViewManager.getIns().pop(this);
    }

    @Override
    public boolean onItemSelectedListener(int viewId) {
        if (viewId == R.id.ivNavLeft) {
            onBack();
            return false;
        } else if (viewId == R.id.ivNavRight) {
            String msg = GLResourcesUtil.getString(R.string.call_phone_text);
            msg = String.format(msg, mShopInfo.getTel());
            GLDialogManager.onAffirm(mContext, msg, GLResourcesUtil.getString(R.string.call), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                    try {
                        // 呼叫
                        Intent intent = new Intent();
                        // 系统默认的action，用来打开默认的电话界面
                        // Intent.ACTION_CALL直接拨打
                        intent.setAction(Intent.ACTION_DIAL);// 调用拨打界面，而不直接拨打
                        // 需要拨打的号码
                        intent.setData(Uri.parse("tel:" + mShopInfo.getTel()));
                        mContext.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return false;
        }
        return super.onItemSelectedListener(viewId);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class GLSlidingTabsViewPagerAdapter extends FragmentPagerAdapter {

        private final String titles[];

        public GLSlidingTabsViewPagerAdapter(FragmentManager fm, String[] titles) {
            super(fm);
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            GLParentFragment f = null;
            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    if (mEnterType == GLBaocaiLogic.ENTER_ORDER_FOR_MY_ORDER) {
                        bundle.putLong(GLConst.INTENT_PARAM, mOrder.getSupplierCompanyId());
                    } else if (mEnterType == GLBaocaiLogic.ENTER_ORDER_FOR_PROVIDER) {
                        bundle.putLong(GLConst.INTENT_PARAM, mShopInfo.getId());
                    }
                    f = new GLBaocaiOrderFragment();
                    f.setArguments(bundle);
                    break;
                case 1:
                    if (mEnterType == GLBaocaiLogic.ENTER_ORDER_FOR_MY_ORDER) {
                        bundle.putLong(GLConst.INTENT_PARAM, mOrder.getSupplierCompanyId());
                    } else if (mEnterType == GLBaocaiLogic.ENTER_ORDER_FOR_PROVIDER) {
                        bundle.putLong(GLConst.INTENT_PARAM, mShopInfo.getId());
                    }
//                    f = new GLOrderDetailFragment();
                    f = new GLHistoryBaocaiFragment();
                    f.setArguments(bundle);
                    break;
                case 2:
                    bundle.putSerializable(GLConst.INTENT_PARAM, mShopInfo);
                    f = new GLProviderInfoFragment();
                    f.setArguments(bundle);
                    break;
                default:
                    break;
            }
            return f;
        }

        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }
}
