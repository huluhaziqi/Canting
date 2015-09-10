package cn.caimatou.canting.modules.activites;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.utils.GLProgressDialogUtil;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLStringUtil;
import cn.caimatou.canting.utils.GLTextCheckUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;
import cn.caimatou.canting.utils.http.GLApiHandler;
import cn.caimatou.canting.utils.http.GLHttpRequestHelper;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Description：注册界面
 * <br/><br/>Created by Fausgoal on 15/8/29.
 * <br/><br/>
 */
public class GLRegistActivity extends GLParentActivity {
    private static final String TAG = "GLRegistActivity";

    private View mView = null;
    private EditText etMobilePhone = null;
    private ImageView ivClearMobilePhone = null;
    private Button btnNext = null;
    private TextView tvServicesAgreement = null;

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.activity_regist_layout, null, false);
        etMobilePhone = findView(mView, R.id.etMobilePhone);
        ivClearMobilePhone = findView(mView, R.id.ivClearMobilePhone);
        btnNext = findView(mView, R.id.btnNext);
        tvServicesAgreement = findView(mView, R.id.tvServicesAgreement);

        setBtnNextEnabled(false);

        etMobilePhone.setTag("mobile");

        new ShowClearButton(etMobilePhone);

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
        GLViewClickUtil.setNoFastClickListener(btnNext, this);
        GLViewClickUtil.setNoFastClickListener(tvServicesAgreement, this);
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
            case R.id.tvServicesAgreement:
                GLCommonManager.makeText(mContext, "点击服务协议");
                break;
            case R.id.btnNext:
                // 点击下一步再验证手机号
                GLTextCheckUtil.GLCheckResult vResult = GLTextCheckUtil.checkMobilePhone(etMobilePhone.getText());
                if (!vResult.isSuccess()) {
                    etMobilePhone.setText("");
                    GLCommonManager.makeText(mContext, vResult.mFailMsg);
                } else {
                    hideInput(etMobilePhone);
                    hideInput(btnNext);
                    fetchCode(etMobilePhone.getText().toString());
                }
                break;
        }
    }

    private void fetchCode(final String mobilePhone) {
        // NOTICE 先调用接口发送验证短信，发送成功再执行倒计时
        GLProgressDialogUtil.showProgress(mContext);

        GLHttpRequestHelper.step1(mContext, mobilePhone, new GLApiHandler(mContext) {
            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);

                GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.send_code_success));

                // 验证码发送成功
                Intent intent = new Intent(mContext, GLRegistVCodeActivity.class);
                intent.putExtra(GLConst.INTENT_PARAM, mobilePhone);
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
                        setBtnNextEnabled(true);
                    } else {
                        ivClearMobilePhone.setVisibility(View.GONE);
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
