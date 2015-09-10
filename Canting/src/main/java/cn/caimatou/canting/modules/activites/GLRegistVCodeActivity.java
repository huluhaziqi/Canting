package cn.caimatou.canting.modules.activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.modules.GLCantingApp;
import cn.caimatou.canting.response.GLStep2Resp;
import cn.caimatou.canting.utils.GLProgressDialogUtil;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLStringUtil;
import cn.caimatou.canting.utils.GLTextCheckUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;
import cn.caimatou.canting.utils.http.GLApiHandler;
import cn.caimatou.canting.utils.http.GLHttpRequestHelper;
import cn.caimatou.canting.widget.GLNavigationBar;
import cn.caimatou.canting.widget.GLVerifyCodeView;

/**
 * Description：注册时填写验证码界面
 * <br/><br/>Created by Fausgoal on 15/8/29.
 * <br/><br/>
 */
public class GLRegistVCodeActivity extends GLParentActivity {
    private static final String TAG = "GLRegistVCodeActivity";

    private View mView = null;
    private GLVerifyCodeView etVCodeView = null;
    private Button btnNext = null;
    private TextView tvResendCode = null;

    private String mMobilePhone = "";

    /**
     * 标识是否在倒计时
     */
    private boolean isTimeDown = false;

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mMobilePhone = bundle.getString(GLConst.INTENT_PARAM);
        }
    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.activity_vcode_layout, null, false);
        TextView tvMobilePhone = findView(mView, R.id.tvMobilePhone);
        etVCodeView = findView(mView, R.id.etVCodeView);
        btnNext = findView(mView, R.id.btnNext);
        tvResendCode = findView(mView, R.id.tvResendCode);

        tvMobilePhone.setText(mMobilePhone);

        setBtnNextEnabled(false);
        sendTimeDown();

        etVCodeView.setTag("code");

        new ShowClearButton(etVCodeView);

        showInputByEnter(etVCodeView);
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
        GLViewClickUtil.setNoFastClickListener(btnNext, this);
        GLViewClickUtil.setNoFastClickListener(tvResendCode, this);
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.free_settled));
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideInput(btnNext);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                // 验证验证码
                GLTextCheckUtil.GLCheckResult result = GLTextCheckUtil.checkVCode(etVCodeView.getText());
                if (!result.isSuccess()) {
                    GLCommonManager.makeText(mContext, result.mFailMsg);
                    etVCodeView.setText("");
                    etVCodeView.invalidate();
                } else {
                    hideInput(etVCodeView);
                    hideInput(btnNext);
                    fetchCheckVerifyCode(mMobilePhone, etVCodeView.getText().toString());
                }
                break;
            case R.id.tvResendCode:
                fetchCode();
                break;
        }
    }

    private void fetchCheckVerifyCode(final String mobilePhone, final String code) {
        // NOTICE 调用API验证是否正确
        GLProgressDialogUtil.showProgress(mContext);

        GLHttpRequestHelper.step2(mContext, mobilePhone, code, new GLApiHandler(mContext) {
            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);
                GLStep2Resp step2Resp = (GLStep2Resp) baseInfo;
                String signToken = "";
                if (null != step2Resp) {
                    signToken = step2Resp.getResult();
                }
                // 验证正确，设置密码
                Intent intent = new Intent(mContext, GLSetPasswordActivity.class);
                intent.putExtra(GLConst.INTENT_PARAM, mobilePhone);
                intent.putExtra(GLConst.INTENT_PARAM1, signToken);
                GLViewManager.getIns().showActivity(mContext, intent, false);
            }

            @Override
            public void onFailure(GLBaseInfo baseInfo) {
                super.onFailure(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);
                // 不正确
                etVCodeView.setText("");
                etVCodeView.invalidate();
                if (null != baseInfo && GLStringUtil.isNotEmpty(baseInfo)) {
                    GLCommonManager.makeText(mContext, baseInfo.getMessage());
                }
            }
        });
    }

    private void sendTimeDown() {
        tvResendCode.setEnabled(false);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(), 2);
    }

    private void fetchCode() {
        // NOTICE 先调用接口发送验证短信，发送成功再执行倒计时
        GLProgressDialogUtil.showProgress(mContext);

        GLHttpRequestHelper.step1(mContext, mMobilePhone, new GLApiHandler(mContext) {
            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);

                // 验证码发送成功
                GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.send_code_success));
                sendTimeDown();
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
                    String resend = GLResourcesUtil.getString(R.string.resend_code);
                    if (mTime > 0) {
                        isTimeDown = true;
                        tvResendCode.setTextColor(GLResourcesUtil.getColor(R.color.gray1));
                        tvResendCode.setText(resend + "（ " + mTime + " ）");
                        tvResendCode.setEnabled(false);
                        mTime--;
                        Message message = new Message();
                        mHandler.sendMessageDelayed(message, 1000);
                    } else {
                        tvResendCode.setTextColor(GLResourcesUtil.getColor(R.color.blue));
                        tvResendCode.setText(resend);
                        tvResendCode.setEnabled(true);
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

                // 验证码
                if (tag.compareTo("code") == GLConst.NONE) {
                    // 输入手机和验证码后启用登录按钮
                    if (length != GLConst.NONE) {
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
