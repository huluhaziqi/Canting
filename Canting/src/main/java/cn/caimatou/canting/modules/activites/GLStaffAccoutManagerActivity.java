package cn.caimatou.canting.modules.activites;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.Staff;
import cn.caimatou.canting.callback.IGLOnListItemClickListener;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.modules.adapters.GLStaffAdapter;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Created by Rosicky on 15/8/31.
 */
public class GLStaffAccoutManagerActivity extends GLParentActivity implements IGLOnListItemClickListener{
    private View mView;
    private ListView staffList;
    private TextView left, right;

    private LayoutInflater inflater;
    private ArrayList<Staff> staffArrayList = new ArrayList<Staff>();
    private GLStaffAdapter adapter;

    private int deletePosition = -1;

    public static final int STAFF_ADD = 1;
    public static final int STAFF_VIEW = 2;

    @Override
    protected void initData() {
        mContext = this;
        inflater = LayoutInflater.from(mContext);
        fetchStaff();
        adapter = new GLStaffAdapter(mContext, staffArrayList, this);
    }

    private void fetchStaff() {
        // TODO 请求获取员工信息
        Staff s1 = new Staff();
        s1.setStaffName("张三");
        s1.setStaffPhone("15555555555");
        s1.setStaffAddress("杭州市西湖区");
        s1.setStaffBirth("1980-09-02");
        s1.setStaffPost("管理员");
        s1.setIsActive(true);
        Staff s2 = new Staff();
        s2.setStaffName("李四");
        s2.setStaffPhone("15512345555");
        s2.setStaffAddress("江西省修水县");
        s2.setStaffBirth("1945-09-02");
        s2.setStaffPost("采购员");
        s2.setIsActive(true);
        Staff s3 = new Staff();
        s3.setStaffName("王五");
        s3.setStaffPhone("15555558055");
        s3.setStaffAddress("深圳市罗湖区");
        s3.setStaffBirth("1998-09-02");
        s3.setStaffPost("厨师长");
        s3.setIsActive(false);
        staffArrayList.add(s1);
        staffArrayList.add(s2);
        staffArrayList.add(s3);
    }

    @Override
    protected void initView() {
        mView = inflater.inflate(R.layout.activity_staff_accout_manager, null);
        left = (TextView) mView.findViewById(R.id.post_manager);
        right = (TextView) mView.findViewById(R.id.staff_add);
        staffList = (ListView) mView.findViewById(R.id.staff_list);
        staffList.setAdapter(adapter);
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    protected void setListener() {
        GLViewClickUtil.setNoFastClickListener(left, this);
        GLViewClickUtil.setNoFastClickListener(right, this);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.staff_add:
                Intent intent = new Intent(mContext, GLStaffAddActivity.class);
                GLViewManager.getIns().showActivity(mContext, intent, STAFF_ADD);
                break;
            case R.id.post_manager:
                GLViewManager.getIns().showActivity(mContext, GLStaffPostManagerActivity.class, false);
                break;
        }
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.staff_accout_manager));
    }

    @Override
    public void onClickItem(int position, View v) {
        Staff staff = adapter.getItem(position);
        deletePosition = position;
        Intent intent = new Intent(mContext, GLStaffInfoActivity.class);
        intent.putExtra(GLConst.INTENT_PARAM, staff);
        // 如果登录用户是管理员，则有删除权限，可删除员工;否则则无
        if (staff.getStaffPost().equals(GLResourcesUtil.getString(R.string.manager))) {
            GLViewManager.getIns().showActivity(mContext, intent, STAFF_VIEW);
        } else {
            GLViewManager.getIns().showActivity(mContext, intent, false);
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
        }
        return super.onItemSelectedListener(viewId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == STAFF_ADD) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Staff userInfo = (Staff) data.getSerializableExtra(GLConst.INTENT_PARAM);
                    adapter.append(userInfo);
                }
            }
        } else if (requestCode == STAFF_VIEW) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    if (data.getBooleanExtra(GLConst.INTENT_PARAM, false)) {
                        staffArrayList.remove(deletePosition);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }
}
