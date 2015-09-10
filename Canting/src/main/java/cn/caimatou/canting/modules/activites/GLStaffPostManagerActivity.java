package cn.caimatou.canting.modules.activites;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.Post;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.modules.adapters.GLManagerPostAdapter;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Created by Rosicky on 15/8/31.
 */
public class GLStaffPostManagerActivity extends GLParentActivity {
    private View mView;
    private LayoutInflater inflater;
    private ExpandableListView postManagerListView;

    private GLManagerPostAdapter adapter;
    private ArrayList<Post> list = new ArrayList<Post>();

    @Override
    protected void initData() {
        mContext = this;
        inflater = LayoutInflater.from(mContext);
        fetchListData();
        adapter = new GLManagerPostAdapter(mContext, list, null);
    }

    private void fetchListData() {
        // TODO 获取岗位信息
        Post s1 = new Post();
        s1.setPostName("厨师长");
        s1.setPostBaocai(true);
        s1.setPostViewReport(false);
        Post s2 = new Post();
        s2.setPostName("采购");
        s2.setPostBaocai(true);
        s2.setPostViewReport(true);
        list.add(s1);
        list.add(s2);
    }

    @Override
    protected void initView() {
        mView = inflater.inflate(R.layout.activity_post_manager, null);
        postManagerListView = (ExpandableListView) mView.findViewById(R.id.post_manager_list);
        postManagerListView.setAdapter(adapter);
        postManagerListView.setGroupIndicator(null);
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    protected void setListener() {
        mView.findViewById(R.id.post_manager_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, GLStaffPostAddActivity.class);
                GLViewManager.getIns().showActivity(mContext, intent, 1);
            }
        });
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.staff_post_manager));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data != null) {
                Post post = (Post) data.getSerializableExtra(Post.STAFF_POST);
                if (post != null) {
                    adapter.addStaffPost(post);
                }
            }
        }
    }
}
