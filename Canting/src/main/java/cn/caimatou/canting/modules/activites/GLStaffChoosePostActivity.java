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
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.modules.adapters.GLChoosePostAdapter;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Created by Rosicky on 15/8/31.
 */
public class GLStaffChoosePostActivity extends GLParentActivity {
    /**
     * 从注册时进入的选择岗位
     */
    public static final int ENTERY_TYPE_REGIST = 1;
    public static final String SELECT_POST = "selected-post";

    private View mView;
    private LayoutInflater inflater;
    private ExpandableListView listView;
    private GLChoosePostAdapter adapter;
    private ArrayList<Post> list = new ArrayList<Post>();
    private String chooseStaffPost;

    @Override
    protected void initData() {
        mContext = this;
        inflater = LayoutInflater.from(mContext);
        fetchStaffPostInfo();
        adapter = new GLChoosePostAdapter(mContext, list, null);
    }

    private void fetchStaffPostInfo() {
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
        mView = inflater.inflate(R.layout.activity_choose_post, null);
        listView = (ExpandableListView) mView.findViewById(R.id.choose_post_list);
        listView.setGroupIndicator(null);
        listView.setAdapter(adapter);
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                for (int position = 0; position < adapter.getGroupCount(); position++) {
                    if (position != i) {
                        listView.collapseGroup(position);
                    }
                }
                chooseStaffPost = adapter.getGroup(i);
            }
        });
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    protected void setListener() {
        mView.findViewById(R.id.choose_post_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 向服务端发送新岗位 chooseStaffPost 为已经选择的岗位
                // 如果提交成功，返回上级activity并设置post
                Intent intent = new Intent();
                intent.putExtra(GLConst.INTENT_PARAM, chooseStaffPost);
                setResult(RESULT_OK, intent);
                GLViewManager.getIns().pop(GLStaffChoosePostActivity.this);
            }
        });
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.choose_post));
        navBar.setRightBtn(GLResourcesUtil.getString(R.string.edit));
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
