package cn.caimatou.canting.modules.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.caimatou.canting.modules.logic.GLLoginLogic;
import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLStringUtil;
import cn.caimatou.canting.utils.GLTextCheckUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Description：界面餐厅信息界面
 * <br/><br/>Created by Fausgoal on 15/8/30.
 * <br/><br/>
 */
public class GLEditInfoActivity extends GLParentActivity {
    private int mEditType = GLConst.NEGATIVE;
    private String mEditContent = "";

    private View mView = null;
    private EditText etContent = null;
    private Button btnSave = null;
    private GLLoginLogic mLoginLogic = null;

    @Override
    protected void initData() {
        mLoginLogic = new GLLoginLogic(mContext);

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mEditType = bundle.getInt(GLConst.INTENT_PARAM);
            mEditContent = bundle.getString(GLConst.INTENT_PARAM1);
        }
    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.activity_edit_info_layout, null, false);
        etContent = findView(mView, R.id.etContent);
        btnSave = findView(mView, R.id.btnSave);

        etContent.setText(mEditContent);
        // 如果是要输入手机号，做相应的控件
        if (mEditType == GLLoginLogic.EDIT_CANTING_PHONE) {
            etContent.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
            etContent.setKeyListener(DigitsKeyListener.getInstance("1234567890"));
            etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        }
        etContent.setSelection(etContent.getText().length());
        showInputByEnter(etContent);
    }

    @Override
    protected void setListener() {
        GLViewClickUtil.setNoFastClickListener(btnSave, this);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                save();
                break;
        }
    }

    private void save() {
        String content = etContent.getText().toString().trim();
        switch (mEditType) {
            case GLLoginLogic.EDIT_USER_NAME:
                if (GLStringUtil.isEmpty(content)) {
                    GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.enter_user_nickname));
                }
            case GLLoginLogic.EDIT_CANTING_NAME:
                if (GLStringUtil.isEmpty(content)) {
                    GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.enter_canting_name));
                    return;
                }
                break;
            case GLLoginLogic.EDIT_CANTING_CITY:
                if (GLStringUtil.isEmpty(content)) {
                    GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.enter_canting_city));
                    return;
                }
                break;
            case GLLoginLogic.EDIT_CANTING_ADDRESS:
                if (GLStringUtil.isEmpty(content)) {
                    GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.enter_detail_address));
                    return;
                }
                break;
            case GLLoginLogic.EDIT_CANTING_CONTACT:
                if (GLStringUtil.isEmpty(content)) {
                    GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.enter_contact));
                    return;
                }
                break;
            case GLLoginLogic.EDIT_CANTING_PHONE:
                if (GLStringUtil.isEmpty(content)) {
                    GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.enter_contact_phone));
                    return;
                } else {
                    GLTextCheckUtil.GLCheckResult result = GLTextCheckUtil.checkMobilePhone(content);
                    if (!result.isSuccess()) {
                        GLCommonManager.makeText(mContext, result.mFailMsg);
                        return;
                    }
                }
                break;
            case GLLoginLogic.EDIT_CANTING_QUARTERS:
                if (GLStringUtil.isEmpty(content)) {
                    GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.enter_quarters));
                    return;
                }
                break;
            default:
                break;
        }

        Intent data = new Intent();
        data.putExtra(GLConst.INTENT_PARAM, content);
        data.putExtra(GLConst.INTENT_PARAM1, mEditType);
        setResult(Activity.RESULT_OK, data);
        onBack();
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(mLoginLogic.getEditCantingTitle(mEditType));
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideInput(btnSave);
    }

    private void onBack() {
        GLViewManager.getIns().pop(this);
    }

    @Override
    public boolean onItemSelectedListener(int viewId) {
        if (viewId == R.id.ivNavLeft) {
            if (etContent.getText().length() != 0) {
                save();
            } else {
                onBack();
            }
            return false;
        }
        return super.onItemSelectedListener(viewId);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (etContent.getText().length() != 0) {
                save();
            } else {
                onBack();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
