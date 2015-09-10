package cn.caimatou.canting.modules.activites;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.GLUserInfo;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.manager.GLUserManager;
import cn.caimatou.canting.modules.GLCantingApp;
import cn.caimatou.canting.response.GLUserInfoResp;
import cn.caimatou.canting.utils.GLProgressDialogUtil;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLStringUtil;
import cn.caimatou.canting.utils.GLTextCheckUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;
import cn.caimatou.canting.utils.http.GLApiHandler;
import cn.caimatou.canting.utils.http.GLHttpRequestHelper;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Description：找回密码界面
 * <br/><br/>Created by Fausgoal on 15/8/29.
 * <br/><br/>
 */
public class GLFoundPasswordActivity extends GLParentActivity {
    private static final String TAG = "GLFoundPasswordActivity";

    private View mView = null;
    private EditText etMobilePhone = null;
    private ImageView ivClearMobilePhone = null;
    private EditText etCode = null;
    private Button btnSendCode = null;
    private Button btnNext = null;

    /**
     * 标识是否在倒计时
     */
    private boolean isTimeDown = false;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.activity_foundpwd_layout, null, false);
        etMobilePhone = findView(mView, R.id.etMobilePhone);
        ivClearMobilePhone = findView(mView, R.id.ivClearMobilePhone);
        etCode = findView(mView, R.id.etCode);
        btnSendCode = findView(mView, R.id.btnSendCode);
        btnNext = findView(mView, R.id.btnNext);

        setBtnNextEnabled(false);
        btnSendCode.setEnabled(false);

        etMobilePhone.setTag("mobile");
        etCode.setTag("code");

        new ShowClearButton(etMobilePhone);
        new ShowClearButton(etCode);
        showInputByEnter(etMobilePhone);
    }

    private void setBtnNextEnabled(boolean isEnabled) {
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
        GLViewClickUtil.setNoFastClickListener(etMobilePhone, this);
        GLViewClickUtil.setNoFastClickListener(ivClearMobilePhone, this);
        GLViewClickUtil.setNoFastClickListener(btnSendCode, this);
        GLViewClickUtil.setNoFastClickListener(btnNext, this);
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.found_password));
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideInput(etMobilePhone);
        hideInput(btnNext);
        ivClearMobilePhone.setVisibility(View.GONE);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.etMobilePhone:
                String content = etMobilePhone.getText().toString().trim();
                if (content.length() != GLConst.NONE) {
                    ivClearMobilePhone.setVisibility(View.VISIBLE);
                } else {
                    ivClearMobilePhone.setVisibility(View.GONE);
                }
                break;
            case R.id.ivClearMobilePhone:
                etMobilePhone.setText("");
                break;
            case R.id.btnSendCode:
                ivClearMobilePhone.setVisibility(View.GONE);
                GLTextCheckUtil.GLCheckResult result = GLTextCheckUtil.checkMobilePhone(etMobilePhone.getText());
                if (!result.isSuccess()) {
                    etMobilePhone.setText("");
                    GLCommonManager.makeText(mContext, result.mFailMsg);
                } else {
                    fetchCode(etMobilePhone.getText().toString());
                }
                break;
            case R.id.btnNext:
                // 点击下一步再验证手机号
                GLTextCheckUtil.GLCheckResult vResult = GLTextCheckUtil.checkMobilePhone(etMobilePhone.getText());
                if (!vResult.isSuccess()) {
                    etMobilePhone.setText("");
                    GLCommonManager.makeText(mContext, vResult.mFailMsg);
                } else {
                    // 验证验证码
                    vResult = GLTextCheckUtil.checkVCode(etCode.getText());
                    if (!vResult.isSuccess()) {
                        GLCommonManager.makeText(mContext, vResult.mFailMsg);
                        etCode.setText("");
                    } else {
                        hideInput(etMobilePhone);
                        hideInput(btnNext);

                        fetchCheckVerifyCode(etMobilePhone.getText().toString(), etCode.getText().toString());
                    }
                }
                break;
        }
    }

    private void fetchCode(final String mobilePhone) {
        // NOTICE 先调用接口发送验证短信，发送成功再执行倒计时
        GLProgressDialogUtil.showProgress(mContext);

        GLHttpRequestHelper.sendCheckCode(mContext, mobilePhone, "", new GLApiHandler(mContext) {
            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);

                GLProgressDialogUtil.dismissProgress(mContext);
                GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.send_code_success));

                btnSendCode.setEnabled(false);
                mHandler.sendMessageDelayed(mHandler.obtainMessage(), 2);
            }

            @Override
            public void onFailure(GLBaseInfo baseInfo) {
                super.onFailure(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);
                if (null != baseInfo && GLStringUtil.isNotEmpty(baseInfo.getMessage())) {
                    GLCommonManager.makeText(mContext, baseInfo.getMessage());
                } else {
                    GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.send_vcode_error));
                }
            }
        });
    }

    private void fetchCheckVerifyCode(final String mobilePhone, final String code) {
        // NOTICE 调用API验证是否正确
        GLProgressDialogUtil.showProgress(mContext);

        GLHttpRequestHelper.loginByCheckCode(mContext, mobilePhone, code, new GLApiHandler(mContext) {
            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                GLUserInfoResp userInfoResp = (GLUserInfoResp) baseInfo;
                final GLUserInfo info = userInfoResp.getResult();
                GLUserManager.saveLoginInfo(mContext, info, "");

                GLProgressDialogUtil.dismissProgress(mContext);

                // 验证正确，设置密码
                Intent intent = new Intent(mContext, GLPasswordActivity.class);
                intent.putExtra(GLConst.INTENT_PARAM, mobilePhone);
                intent.putExtra(GLConst.INTENT_PARAM1, code);
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

    private Handler mHandler = new Handler(GLCantingApp.getIns().getMainLooper()) {
        private long mTime = 59;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GLConst.NONE:
                    String resend = GLResourcesUtil.getString(R.string.resend);
                    if (mTime > 0) {
                        isTimeDown = true;
                        btnSendCode.setText(resend + "(" + mTime + ")");
                        btnSendCode.setEnabled(false);
                        mTime--;
                        Message message = new Message();
                        mHandler.sendMessageDelayed(message, 1000);
                    } else {
                        btnSendCode.setText(resend);
                        btnSendCode.setEnabled(true);
                        mTime = 60;
                        isTimeDown = false;
                    }
                    break;
            }
        }
    };

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

                // 手机号
                if (tag.compareTo("mobile") == GLConst.NONE) {
                    if (length != GLConst.NONE) {
                        ivClearMobilePhone.setVisibility(View.VISIBLE);
                        if (isTimeDown) {
                            btnSendCode.setEnabled(false);
                        } else {
                            btnSendCode.setEnabled(true);
                        }
                    } else {
                        ivClearMobilePhone.setVisibility(View.GONE);
                        btnSendCode.setEnabled(false);
                    }

                    CharSequence code = etCode.getText();
                    if (length != GLConst.NONE && code.length() != GLConst.NONE) {
                        setBtnNextEnabled(true);
                    } else {
                        setBtnNextEnabled(false);
                    }
                }
                // 验证码
                else if (tag.compareTo("code") == GLConst.NONE) {
                    // 输入手机和验证码后启用登录按钮
                    CharSequence account = etMobilePhone.getText();
                    if (length != GLConst.NONE && account.length() != GLConst.NONE) {
                        setBtnNextEnabled(true);
                    } else {
                        setBtnNextEnabled(false);
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
