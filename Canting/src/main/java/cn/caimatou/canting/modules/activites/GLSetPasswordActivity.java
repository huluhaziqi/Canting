package cn.caimatou.canting.modules.activites;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.GLLocation;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.manager.GLLocationManager;
import cn.caimatou.canting.manager.GLUserManager;
import cn.caimatou.canting.response.GLUserInfoResp;
import cn.caimatou.canting.utils.GLProgressDialogUtil;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLStringUtil;
import cn.caimatou.canting.utils.GLTextCheckUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;
import cn.caimatou.canting.utils.MD5;
import cn.caimatou.canting.utils.http.GLApiHandler;
import cn.caimatou.canting.utils.http.GLHttpRequestHelper;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Description：设置密码界面
 * <br/><br/>Created by Fausgoal on 15/8/29.
 * <br/><br/>
 */
public class GLSetPasswordActivity extends GLParentActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "GLSetPasswordActivity";

    private View mView = null;
    private EditText etNewPassword = null;
    private ImageView ivClearNewPwd = null;
    private EditText etConfirmPassword = null;
    private ImageView ivClearConfirmPwd = null;
    private LinearLayout llShowPassword = null;
    private RadioButton rbCheck = null;
    private TextView tvShowPassword = null;
    private Button btnNext = null;

    private String mMobilePahone = null;
    private String mSignToken = "";

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mMobilePahone = bundle.getString(GLConst.INTENT_PARAM);
            mSignToken = bundle.getString(GLConst.INTENT_PARAM1);
        }
    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.activity_set_password_layout, null, false);
        etNewPassword = findView(mView, R.id.etNewPassword);
        ivClearNewPwd = findView(mView, R.id.ivClearNewPwd);
        etConfirmPassword = findView(mView, R.id.etConfirmPassword);
        ivClearConfirmPwd = findView(mView, R.id.ivClearConfirmPwd);
        llShowPassword = findView(mView, R.id.llShowPassword);
        rbCheck = findView(mView, R.id.rbCheck);
        tvShowPassword = findView(mView, R.id.tvShowPassword);
        btnNext = findView(mView, R.id.btnNext);

        setBtnSaveEnabled(false);

        etNewPassword.setTag("newPwd");
        etConfirmPassword.setTag("conPwd");

        new ShowClearButton(etNewPassword);
        new ShowClearButton(etConfirmPassword);

        showInputByEnter(etNewPassword);
    }

    private void setBtnSaveEnabled(boolean isEnabled) {
        btnNext.setEnabled(isEnabled);
        if (isEnabled) {
            // 启用
            btnNext.setBackgroundResource(R.drawable.btn_green_selector);
            btnNext.setTextColor(GLResourcesUtil.getColor(R.color.white));
        } else {
            // 禁用
            btnNext.setBackgroundColor(GLResourcesUtil.getColor(R.color.gray5));
            btnNext.setTextColor(GLResourcesUtil.getColor(R.color.gray));
        }
    }

    @Override
    protected void setListener() {
        GLViewClickUtil.setNoFastClickListener(etNewPassword, this);
        GLViewClickUtil.setNoFastClickListener(ivClearNewPwd, this);
        GLViewClickUtil.setNoFastClickListener(etConfirmPassword, this);
        GLViewClickUtil.setNoFastClickListener(ivClearConfirmPwd, this);
        GLViewClickUtil.setNoFastClickListener(llShowPassword, this);
        GLViewClickUtil.setNoFastClickListener(rbCheck, this);
        GLViewClickUtil.setNoFastClickListener(tvShowPassword, this);
        GLViewClickUtil.setNoFastClickListener(btnNext, this);

        rbCheck.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        super.setToolbarStyle(navBar);
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.set_password));
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            //如果选中，显示密码
            etNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            //否则隐藏密码
            etNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        setPasswordSelection();
    }

    private void setPasswordSelection() {
        int newPwdLen = etNewPassword.getText().length();
        int conPwdLen = etConfirmPassword.getText().length();
        if ((newPwdLen != GLConst.NONE && conPwdLen != GLConst.NONE)
                || (newPwdLen == GLConst.NONE && conPwdLen != GLConst.NONE)) {
            etConfirmPassword.requestFocus();
            etConfirmPassword.requestFocusFromTouch();
            etConfirmPassword.setSelection(conPwdLen);
        } else if (newPwdLen != GLConst.NONE) {
            etNewPassword.setSelection(conPwdLen);
        }
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.etNewPassword:
                String newPwd = etNewPassword.getText().toString().trim();
                if (newPwd.length() != GLConst.NONE) {
                    ivClearNewPwd.setVisibility(View.VISIBLE);
                } else {
                    ivClearNewPwd.setVisibility(View.GONE);
                }
                break;
            case R.id.ivClearNewPwd:
                etNewPassword.setText("");
                break;
            case R.id.etConfirmPassword:
                String conPwd = etConfirmPassword.getText().toString().trim();
                if (conPwd.length() != GLConst.NONE) {
                    ivClearConfirmPwd.setVisibility(View.VISIBLE);
                } else {
                    ivClearConfirmPwd.setVisibility(View.GONE);
                }
                break;
            case R.id.ivClearConfirmPwd:
                etConfirmPassword.setText("");
                break;
            case R.id.rbCheck:
            case R.id.tvShowPassword:
            case R.id.llShowPassword:
                rbCheck.setChecked(!rbCheck.isChecked());
                break;
            case R.id.btnNext:
                GLTextCheckUtil.GLCheckResult vResult = GLTextCheckUtil.checkPassword(etNewPassword.getText(), etConfirmPassword.getText());
                if (!vResult.isSuccess()) {
                    GLCommonManager.makeText(mContext, vResult.mFailMsg);
                } else {
                    hideInput(etNewPassword);
                    hideInput(etConfirmPassword);
                    hideInput(btnNext);
                    setPassword(etConfirmPassword.getText().toString().trim());
                }
                break;
        }
    }

    private void setPassword(final String password) {
        // NOTICE 调用API设置密码
        GLProgressDialogUtil.showProgress(mContext);

        // 密码md5加密
        final String md5Password = MD5.md5(password);

        String nickname = "";
        int sex = GLConst.NONE;
        double longitude = GLConst.NONE;
        double latitude = GLConst.NONE;
        // 位置信息
        GLLocation location = GLLocationManager.getLocation();
        if (null != location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        GLHttpRequestHelper.step3(mContext, mSignToken, mMobilePahone, md5Password, nickname, sex,
                longitude, latitude, new GLApiHandler(mContext) {
                    @Override
                    public void onSuccess(GLBaseInfo baseInfo) {
                        super.onSuccess(baseInfo);
                        GLProgressDialogUtil.dismissProgress(mContext);

                        GLUserInfoResp userInfoResp = (GLUserInfoResp) baseInfo;
                        GLUserManager.saveLoginInfo(mContext, userInfoResp.getResult(), md5Password);

                        // 设置成功，填写餐厅信息
                        Intent intent = new Intent(mContext, GLEditCantingInfoActivity.class);
                        intent.putExtra(GLConst.INTENT_PARAM, mMobilePahone);
                        intent.putExtra(GLConst.INTENT_PARAM1, GLEditCantingInfoActivity.ENTERY_TYPE_REGISTE);
                        GLViewManager.getIns().showActivity(mContext, intent, false);
                    }

                    @Override
                    public void onFailure(GLBaseInfo baseInfo) {
                        super.onFailure(baseInfo);
                        GLProgressDialogUtil.dismissProgress(mContext);
                        if (null != baseInfo && GLStringUtil.isNotEmpty(baseInfo.getMessage())) {
                            GLCommonManager.makeText(mContext, baseInfo.getMessage());
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideInput(btnNext);
        ivClearNewPwd.setVisibility(View.GONE);
        ivClearConfirmPwd.setVisibility(View.GONE);
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

    /**
     * EditText的内容改变事件
     */
    private class ShowClearButton {
        private EditText mEditText = null;

        public ShowClearButton(EditText editText) {
            this.mEditText = editText;
            if (null != editText)
                editText.addTextChangedListener(watcher);
        }

        private TextWatcher watcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null == mEditText || null == mEditText.getTag())
                    return;

                String content = mEditText.getText().toString();
                int length = content.length();

                String tag = mEditText.getTag().toString();

                if (tag.compareTo("newPwd") == GLConst.NONE) {
                    if (length != GLConst.NONE) {
                        ivClearNewPwd.setVisibility(View.VISIBLE);
                        ivClearConfirmPwd.setVisibility(View.GONE);
                    } else {
                        ivClearNewPwd.setVisibility(View.GONE);
                        ivClearConfirmPwd.setVisibility(View.GONE);
                    }

                    CharSequence conPwd = etConfirmPassword.getText();
                    if (length != GLConst.NONE && conPwd.length() != GLConst.NONE) {
                        setBtnSaveEnabled(true);
                    } else {
                        setBtnSaveEnabled(false);
                    }
                } else if (tag.compareTo("conPwd") == GLConst.NONE) {
                    if (length != GLConst.NONE) {
                        ivClearNewPwd.setVisibility(View.GONE);
                        ivClearConfirmPwd.setVisibility(View.VISIBLE);
                    } else {
                        ivClearNewPwd.setVisibility(View.GONE);
                        ivClearConfirmPwd.setVisibility(View.GONE);
                    }

                    CharSequence newPwd = etNewPassword.getText();
                    if (length != GLConst.NONE && newPwd.length() != GLConst.NONE) {
                        setBtnSaveEnabled(true);
                    } else {
                        setBtnSaveEnabled(false);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }
}
