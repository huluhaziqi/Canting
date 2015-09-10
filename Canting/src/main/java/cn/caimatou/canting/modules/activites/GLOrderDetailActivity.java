package cn.caimatou.canting.modules.activites;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.Company;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.modules.fragments.GLOrderDetailFragment;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Description：报菜单详情界面
 * <br/><br/>Created by Fausgoal on 15/9/4.
 * <br/><br/>
 */
public class GLOrderDetailActivity extends GLParentActivity {

    private View mView = null;
    private Company mShopInfo = null;

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mShopInfo = (Company) bundle.get(GLConst.INTENT_PARAM);
        }
    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.activity_order_detail_layout, null, false);
        GLOrderDetailFragment fragment = new GLOrderDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GLConst.INTENT_PARAM, mShopInfo);
        fragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction().add(R.id.llContainer, fragment)
                .commit();
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
    }

    private void onBack() {
        GLViewManager.getIns().pop(this);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
