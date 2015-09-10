package cn.caimatou.canting.modules.activites;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Created by Rosicky on 15/8/31.
 */
public class GLStaffManagerActivity extends GLParentActivity {
    private View mView;
    private LayoutInflater inflater;

    @Override
    protected void initData() {
        mContext = this;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    protected void initView() {
        mView = inflater.inflate(R.layout.activity_staff, null);
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    protected void setListener() {
        mView.findViewById(R.id.staff_account_manager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GLViewManager.getIns().showActivity(mContext, GLStaffAccoutManagerActivity.class, false);
            }
        });
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.staff_manager));
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
