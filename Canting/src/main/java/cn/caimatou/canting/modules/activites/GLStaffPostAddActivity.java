package cn.caimatou.canting.modules.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.Post;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Created by Rosicky on 15/9/1.
 */
public class GLStaffPostAddActivity extends GLParentActivity {

    private View mView;
    private ToggleButton toggleButtonBaocai, toggleButtonViewReport;
    private EditText postEditText;
    private TextView postSave;

    private LayoutInflater inflater;
    private String postName;
    boolean canBaocai = false, canViewReport = false;

    @Override
    protected void initData() {
        mContext = this;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    protected void initView() {
        mView = inflater.inflate(R.layout.activity_post_add, null);
        postEditText = (EditText) mView.findViewById(R.id.post_add_edit);
        toggleButtonBaocai = (ToggleButton) mView.findViewById(R.id.baocai_post_add_toggle);
        toggleButtonViewReport = (ToggleButton) mView.findViewById(R.id.view_report_post_add_toggle);
        postSave = (TextView) mView.findViewById(R.id.post_add_save);
    }



    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.post_add));
    }

    @Override
    protected void setListener() {
        toggleButtonBaocai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canBaocai = !canBaocai;
            }
        });
        toggleButtonViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canViewReport = !canViewReport;
            }
        });
        postSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (postEditText.getText() == null || postEditText.getText().toString().trim().equals("")) {
                    // TODO 判断岗位名称，进行错误提醒
                    GLCommonManager.makeText(mContext, "请输入岗位名称");
                    return ;
                } else {
                    // TODO 保存岗位至服务器
                    // 如果返回成功，则返回上级activity并设置result
                    Post post = new Post();
                    post.setPostName(postEditText.getText().toString());
                    post.setPostBaocai(canBaocai);
                    post.setPostViewReport(canViewReport);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Post.STAFF_POST, post);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    GLStaffPostAddActivity.this.setResult(RESULT_OK, intent);
                    onBack();
                }
            }
        });
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
