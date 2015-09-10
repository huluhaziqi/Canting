package cn.caimatou.canting.modules.activites;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.Staff;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLTextCheckUtil;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Created by Rosicky on 15/8/31.
 */
public class GLStaffInfoActivity extends GLParentActivity {


    private View mView;
    private TextView staffInfoName, staffInfoPhone, staffInfoPost, staffInfoTel, staffInfoAddress, staffInfoBirth;
    private ImageView chosePost;
    private RelativeLayout staffInfoRelativeLayout;
    private LayoutInflater inflater;

    private Staff staff;
    private boolean delete = false;

    @Override
    protected void initData() {
        mContext = this;
        inflater = LayoutInflater.from(mContext);
        staff = (Staff) getIntent().getSerializableExtra(GLConst.INTENT_PARAM);
    }

    @Override
    protected void initView() {
        mView = inflater.inflate(R.layout.activity_staff_info, null);
        staffInfoRelativeLayout = (RelativeLayout) mView.findViewById(R.id.staff_info_post_rel);
        staffInfoName = (TextView) mView.findViewById(R.id.staff_info_name);
        staffInfoPhone = (TextView) mView.findViewById(R.id.staff_info_phone);
        staffInfoPost = (TextView) mView.findViewById(R.id.staff_info_post);
        staffInfoTel = (TextView) mView.findViewById(R.id.staff_info_tel);
        staffInfoAddress = (TextView) mView.findViewById(R.id.staff_info_address);
        staffInfoBirth = (TextView) mView.findViewById(R.id.staff_info_birth);

        chosePost = (ImageView) mView.findViewById(R.id.staff_info_post_next);
        initViewData();
    }

    private void initViewData() {
        if (staff.getStaffPost().equals(GLResourcesUtil.getString(R.string.manager))) {
            chosePost.setVisibility(View.VISIBLE);
            staffInfoRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, GLStaffChoosePostActivity.class);
                    GLViewManager.getIns().showActivity(mContext, intent, 1);
                }
            });
        } else {
            chosePost.setVisibility(View.GONE);
        }
        staffInfoName.setText(staff.getStaffName());
        staffInfoPhone.setText(staff.getStaffPhone());
        staffInfoTel.setText(staff.getStaffPhone());
        staffInfoAddress.setText(staff.getStaffAddress());
        staffInfoBirth.setText(staff.getStaffBirth());
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    protected void setListener() {
        super.setListener();
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.staff_info));
        if (staff.getStaffPost().equals(GLResourcesUtil.getString(R.string.manager))) {
            navBar.setRightBtn(GLResourcesUtil.getString(R.string.delete));
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
        Intent intent = new Intent();
        if (viewId == R.id.ivNavLeft) {
            delete = false;
            intent.putExtra(GLConst.INTENT_PARAM, delete);
        } else if (viewId == R.id.tvNavRight) {
            delete = true;
            intent = new Intent();
            intent.putExtra(GLConst.INTENT_PARAM, delete);
        }
        GLStaffInfoActivity.this.setResult(RESULT_OK, intent);
        onBack();
        return super.onItemSelectedListener(viewId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data != null) {
                String post = data.getStringExtra(GLStaffChoosePostActivity.SELECT_POST);
                if (post != null && post.length() != 0) {
                    // 如果post有值，说明岗位信息上传成功，更新本地已经staff信息
                    staffInfoPost.setText(post);
                    staff.setStaffPost(post);
                }
            }
        }
    }
}
