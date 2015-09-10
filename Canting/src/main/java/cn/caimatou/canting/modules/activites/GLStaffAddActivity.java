package cn.caimatou.canting.modules.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.Staff;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Created by Rosicky on 15/8/31.
 */
public class GLStaffAddActivity extends GLParentActivity {

    private View mView;
    private EditText staffName, staffPhone, staffAddress, staffBirth;

    private LayoutInflater inflater;

    @Override
    protected void initData() {
        mContext = this;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    protected void initView() {
        mView = inflater.inflate(R.layout.activity_staff_add, null);
        staffName = (EditText) mView.findViewById(R.id.staff_add_name);
        staffPhone = (EditText) mView.findViewById(R.id.staff_add_phone);
        staffAddress = (EditText) mView.findViewById(R.id.staff_add_address);
        staffBirth = (EditText) mView.findViewById(R.id.staff_add_birth);
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    protected void setListener() {
        mView.findViewById(R.id.staff_add_icn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, GLStaffTelListActivity.class);
                GLViewManager.getIns().showActivity(mContext, intent, 1);
            }
        });
        mView.findViewById(R.id.staff_add_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (staffName.getText() == null || staffName.getText().equals("")) {
                    Toast.makeText(GLStaffAddActivity.this, "请输入姓名", Toast.LENGTH_SHORT);
                    return;
                }
                if (staffPhone.getText() == null || staffPhone.getText().equals("")) {
                    Toast.makeText(GLStaffAddActivity.this, "请输入手机号码", Toast.LENGTH_SHORT);
                    return;
                }
                // TODO 请求网络，上传新员工
                Staff staff = new Staff();
                staff.setStaffName(staffName.getText().toString());
                staff.setStaffPhone(staffPhone.getText().toString());
                staff.setIsActive(false);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(GLConst.INTENT_PARAM, staff);
                intent.putExtras(bundle);
                GLStaffAddActivity.this.setResult(RESULT_OK, intent);
                GLViewManager.getIns().pop(GLStaffAddActivity.this);
            }
        });
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.staff_add));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String[] ans = data.getStringArrayExtra("staff");
            if (ans != null) {
                staffName.setText(ans[0]);
                staffPhone.setText(ans[1]);
            }
        }
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
        } else if (viewId == R.id.tvNavRight) {
            GLViewManager.getIns().showActivity(mContext, GLStaffPostManagerActivity.class, false);
        }
        return super.onItemSelectedListener(viewId);
    }
}
