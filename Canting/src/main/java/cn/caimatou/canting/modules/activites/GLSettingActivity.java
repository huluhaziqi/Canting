package cn.caimatou.canting.modules.activites;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.common.GLCommonVariables;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Created by Rosicky on 15/9/1.
 */
public class GLSettingActivity extends GLParentActivity {
    private View mView;
    private LayoutInflater inflater;
    private ToggleButton messageNotice;

    private boolean isChecked;


    @Override
    protected void initData() {
        mContext = this;
        inflater = LayoutInflater.from(mContext);
        if (GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.MESSAGE_ENABLE) != null) {
            isChecked = (boolean) GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.MESSAGE_ENABLE);
        } else {
            isChecked = true;
        }

    }

    @Override
    protected void initView() {
        mView = inflater.inflate(R.layout.activity_setting, null);
        messageNotice = (ToggleButton) mView.findViewById(R.id.message_btn);
        messageNotice.setChecked(isChecked);
        ((TextView) mView.findViewById(R.id.check_update).findViewById(R.id.common_text1)).setText(GLResourcesUtil.getString(R.string.check_update));
        mView.findViewById(R.id.check_update).findViewById(R.id.common_text2).setVisibility(View.INVISIBLE);
        ((TextView) mView.findViewById(R.id.about_us).findViewById(R.id.common_text1)).setText(GLResourcesUtil.getString(R.string.about_us));
        mView.findViewById(R.id.about_us).findViewById(R.id.common_text2).setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    protected void setListener() {
        messageNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 开关消息提醒
                isChecked = !isChecked;
                GLCommonVariables.getIns().saveObject(GLCommonVariables.Keys.MESSAGE_ENABLE, isChecked);
            }
        });
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.setting));
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
