package cn.caimatou.canting.modules.activites;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.Company;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.modules.fragments.GLProviderInfoFragment;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Created by mac on 15/8/28.
 */
public class GLProviderInfoActivity extends GLParentActivity {
    private View mView;

    private LayoutInflater inflater;
    private Company shopInfo;
    private FragmentManager fragmentManager;
    private GLProviderInfoFragment fragment;

    @Override
    protected void initData() {
        mContext = this;
        inflater = LayoutInflater.from(mContext);
        fragmentManager = getSupportFragmentManager();
        fragment = new GLProviderInfoFragment();
        initShopInfo();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GLConst.INTENT_PARAM, shopInfo);
        fragment.setArguments(bundle);
    }

    private void initShopInfo() {
        // TODO GET SHOPINFO
        shopInfo = (Company) getIntent().getSerializableExtra(GLConst.INTENT_PARAM);
        if (shopInfo == null) {
            shopInfo = new Company();
        }
    }

    @Override
    protected void initView() {
        mView = inflater.inflate(R.layout.activity_vegeinfo, null, false);
        fragmentManager.beginTransaction().replace(R.id.vegeinfo_frgment, fragment).commit();
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.shop_information));
    }

    private void onBack() {
        GLViewManager.getIns().pop(this);
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

}
