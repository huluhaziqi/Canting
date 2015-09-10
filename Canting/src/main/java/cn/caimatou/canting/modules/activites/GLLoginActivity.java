package cn.caimatou.canting.modules.activites;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.common.GLCommonVariables;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.modules.logic.GLLoginLogic;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLStringUtil;
import cn.caimatou.canting.utils.GLTextCheckUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Description：登录界面
 * <br/><br/>Created by Fausgoal on 15/8/27.
 * <br/><br/>
 */
public class GLLoginActivity extends GLParentActivity {
    private static final String TAG = "GLLoginActivity";

    private View mView = null;

    private EditText etLoginAccount = null;
    private ImageView ivClearAccount = null;
    private EditText etLoginPassword = null;
    private ImageView ivClearPassword = null;
    private Button btnLogin = null;
    private TextView tvFogotLoginPwd = null;
    private TextView tvFreeSettled = null;

    private GLLoginLogic mLoginLogic = null;

    private boolean mIsLocalLoginEnable = false;
    private boolean mRedirectOnSuccess = false;

    @Override
    protected void initData() {
        boolean apiLoginRequired = getIntent().getBooleanExtra(GLConst.INTENT_PARAM, false);
        if (apiLoginRequired) {
            mRedirectOnSuccess = false;
            mIsLocalLoginEnable = false;
        } else {
            mRedirectOnSuccess = true;
            mIsLocalLoginEnable = true;
        }

        mLoginLogic = new GLLoginLogic(mContext);
    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.activity_login_layout, null, false);

        etLoginAccount = findView(mView, R.id.etLoginAccount);
        ivClearAccount = findView(mView, R.id.ivClearAccount);
        etLoginPassword = findView(mView, R.id.etLoginPassword);
        ivClearPassword = findView(mView, R.id.ivClearPassword);
        btnLogin = findView(mView, R.id.btnLogin);
        tvFogotLoginPwd = findView(mView, R.id.tvFogotLoginPwd);
        tvFreeSettled = findView(mView, R.id.tvFreeSettled);

        setBtnLoginEnabled(false);

        etLoginAccount.setTag("account");
        etLoginPassword.setTag("pwd");

        new ShowClearButton(etLoginAccount);
        new ShowClearButton(etLoginPassword);

        String account = (String) GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.ACCOUNT);
        String password = (String) GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.PASSWORD);
        boolean hasAccount = false;
        boolean hasPassword = false;
        if (GLStringUtil.isNotEmpty(account)) {
            etLoginAccount.setText(account);
            etLoginAccount.setSelection(etLoginAccount.getText().length());
            hasAccount = true;
        }

        if (GLStringUtil.isNotEmpty(password)) {
            etLoginPassword.setText(GLLoginLogic.DEFAULT_PWD);
            etLoginPassword.setSelection(etLoginPassword.getText().length());
            hasPassword = true;
        }

        if (!hasAccount) {
            showInputByEnter(etLoginAccount);
        } else {
            if (!hasPassword) {
                showInputByEnter(etLoginPassword);
            }
        }
    }

    private void setBtnLoginEnabled(boolean isEnabled) {
        btnLogin.setEnabled(isEnabled);
        if (isEnabled) {
            // 启用
            btnLogin.setBackgroundResource(R.drawable.btn_green_selector);
            btnLogin.setTextColor(GLResourcesUtil.getColor(R.color.white));
        } else {
            // 禁用
            btnLogin.setBackgroundColor(GLResourcesUtil.getColor(R.color.gray5));
            btnLogin.setTextColor(GLResourcesUtil.getColor(R.color.gray));
        }
    }

    @Override
    protected void setListener() {
        GLViewClickUtil.setNoFastClickListener(etLoginAccount, this);
        GLViewClickUtil.setNoFastClickListener(ivClearAccount, this);
        GLViewClickUtil.setNoFastClickListener(etLoginPassword, this);
        GLViewClickUtil.setNoFastClickListener(ivClearPassword, this);
        GLViewClickUtil.setNoFastClickListener(btnLogin, this);
        GLViewClickUtil.setNoFastClickListener(tvFogotLoginPwd, this);
        GLViewClickUtil.setNoFastClickListener(tvFreeSettled, this);
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        super.setToolbarStyle(navBar);
        navBar.setNavTitle(R.string.activity_login);
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideInput(btnLogin);
        ivClearAccount.setVisibility(View.GONE);
        ivClearPassword.setVisibility(View.GONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.etLoginAccount:
                String content = etLoginAccount.getText().toString().trim();
                if (content.length() != GLConst.NONE) {
                    ivClearAccount.setVisibility(View.VISIBLE);
                    ivClearPassword.setVisibility(View.GONE);
                } else {
                    ivClearAccount.setVisibility(View.GONE);
                    ivClearPassword.setVisibility(View.GONE);
                }
                break;
            case R.id.etLoginPassword:
                String pwd = etLoginPassword.getText().toString().trim();
                if (pwd.length() != GLConst.NONE) {
                    ivClearAccount.setVisibility(View.GONE);
                    ivClearPassword.setVisibility(View.VISIBLE);
                } else {
                    ivClearAccount.setVisibility(View.GONE);
                    ivClearPassword.setVisibility(View.GONE);
                }
                break;
            case R.id.ivClearAccount:
                etLoginAccount.setText("");
                break;
            case R.id.ivClearPassword:
                etLoginPassword.setText("");
                break;
            case R.id.btnLogin:
                CharSequence account = etLoginAccount.getText();
                CharSequence password = etLoginPassword.getText();
                GLTextCheckUtil.GLCheckResult result = GLTextCheckUtil.checkLogin(account, password);
                if (!result.isSuccess()) {
                    GLCommonManager.makeText(mContext, result.mFailMsg);
                } else {
                    hideInput(etLoginAccount);
                    hideInput(etLoginPassword);
                    hideInput(btnLogin);
                    // Notice 验证成功，执行连网登录
                    mLoginLogic.login(account.toString().trim(), password.toString().trim());
                }
                break;
            case R.id.tvFogotLoginPwd:
                GLViewManager.getIns().showActivity(mContext, GLFoundPasswordActivity.class, false);
                break;
            case R.id.tvFreeSettled:
                GLViewManager.getIns().showActivity(mContext, GLRegistActivity.class, false);
                break;
        }
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

                // 账号
                if (tag.compareTo("account") == GLConst.NONE) {
                    if (length != GLConst.NONE) {
                        ivClearAccount.setVisibility(View.VISIBLE);
                        ivClearPassword.setVisibility(View.GONE);
                    } else {
                        ivClearAccount.setVisibility(View.GONE);
                        ivClearPassword.setVisibility(View.GONE);
                    }

                    CharSequence password = etLoginPassword.getText();
                    if (length != GLConst.NONE && password.length() != GLConst.NONE) {
                        setBtnLoginEnabled(true);
                    } else {
                        setBtnLoginEnabled(false);
                    }
                }
                // 密码
                else if (tag.compareTo("pwd") == GLConst.NONE) {
                    // 输入密码后启用登录按钮
                    if (length != GLConst.NONE) {
                        ivClearAccount.setVisibility(View.GONE);
                        ivClearPassword.setVisibility(View.VISIBLE);

                    } else {
                        ivClearAccount.setVisibility(View.GONE);
                        ivClearPassword.setVisibility(View.GONE);
                    }

                    CharSequence account = etLoginAccount.getText();
                    if (length != GLConst.NONE && account.length() != GLConst.NONE) {
                        setBtnLoginEnabled(true);
                    } else {
                        setBtnLoginEnabled(false);
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
