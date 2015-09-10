package cn.caimatou.canting.modules.activites;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.Company;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Created by Rosicky on 15/9/1.
 */
public class GLAptitudeIdentifyActivity extends GLParentActivity {
    private View mView;

    private LayoutInflater inflater;
    private Company shopInfo;

    @Override
    protected void initData() {
        mContext = this;
        inflater = LayoutInflater.from(mContext);
        fetchShopInfo();
    }

    private void fetchShopInfo() {
        // 获取供应商数据
        if (shopInfo == null) {
            shopInfo = new Company();
            shopInfo.setCertificationStatus(1);
        }
    }

    @Override
    protected void initView() {
        mView = inflater.inflate(R.layout.activity_aptitude, null);
        ((TextView) mView.findViewById(R.id.aptitude_basic_info).findViewById(R.id.common_text1)).setText(GLResourcesUtil.getString(R.string.canting_basic_info));
        ((TextView) mView.findViewById(R.id.aptitude_certificate).findViewById(R.id.common_text1)).setText(GLResourcesUtil.getString(R.string.aptitude_certificate));
        if (shopInfo.getCertificationStatus() == 1) {
            ((TextView) mView.findViewById(R.id.aptitude_basic_info).findViewById(R.id.common_text2)).setText(GLResourcesUtil.getString(R.string.verify_passed));
            ((TextView) mView.findViewById(R.id.aptitude_basic_info).findViewById(R.id.common_text2)).setTextColor(GLResourcesUtil.getColor(R.color.green));
            ((TextView) mView.findViewById(R.id.aptitude_certificate).findViewById(R.id.common_text2)).setText(GLResourcesUtil.getString(R.string.uploaded));
            ((TextView) mView.findViewById(R.id.aptitude_certificate).findViewById(R.id.common_text2)).setTextColor(GLResourcesUtil.getColor(R.color.green));
        } else {
            ((TextView) mView.findViewById(R.id.aptitude_basic_info).findViewById(R.id.common_text2)).setText(GLResourcesUtil.getString(R.string.verify_not_passed));
            ((TextView) mView.findViewById(R.id.aptitude_basic_info).findViewById(R.id.common_text2)).setTextColor(GLResourcesUtil.getColor(R.color.gray1));
            ((TextView) mView.findViewById(R.id.aptitude_certificate).findViewById(R.id.common_text2)).setText(GLResourcesUtil.getString(R.string.verify_not_passed));
            ((TextView) mView.findViewById(R.id.aptitude_certificate).findViewById(R.id.common_text2)).setTextColor(GLResourcesUtil.getColor(R.color.gray1));
        }
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    protected void setListener() {
        mView.findViewById(R.id.aptitude_basic_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, GLEditCantingInfoActivity.class);
                intent.putExtra(GLConst.INTENT_PARAM, "");
                intent.putExtra(GLConst.INTENT_PARAM1, GLEditCantingInfoActivity.ENTERY_TYPE_EDIT);
                GLViewManager.getIns().showActivity(mContext, intent, false);
            }
        });
        mView.findViewById(R.id.aptitude_certificate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GLViewManager.getIns().showActivity(mContext, GLCertificationUploadActivity.class, false);
            }
        });
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.aptitude_identify));
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
        }
        return super.onItemSelectedListener(viewId);
    }
}
