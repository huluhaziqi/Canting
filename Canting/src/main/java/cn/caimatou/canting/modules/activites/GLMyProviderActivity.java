package cn.caimatou.canting.modules.activites;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.base.MyProviderViewAdapter;
import cn.caimatou.canting.bean.Company;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.modules.GLCantingApp;
import cn.caimatou.canting.modules.logic.GLBaocaiLogic;
import cn.caimatou.canting.modules.logic.GLProviderLogic;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.widget.GLNavigationBar;
import cn.caimatou.canting.widget.refresh.PullToRefreshLayout;
import cn.caimatou.canting.widget.refresh.PullableListView;

/**
 * Created by linxiaobin on 2015/8/30.
 */
public class GLMyProviderActivity extends GLParentActivity implements PullToRefreshLayout.OnRefreshListener {
    private View mView;
    private LayoutInflater inflater;
    private ViewPager viewPager = null;
    private PullableListView shopListView;
    private List<Company> companylist = new ArrayList<Company>();
    private MyProviderViewAdapter adapter;
    private PullToRefreshLayout prlMyProviderRefresh = null;

    private GLProviderLogic mProviderLogic = null;

    @Override
    protected void initData() {
        mContext = this;
        inflater = LayoutInflater.from(mContext);
        mProviderLogic = new GLProviderLogic(mContext);

        companylist.clear();
        Company company1 = new Company();
        company1.setName("供应商名称");
        company1.setMemo("主营:时令蔬菜、干货");
        company1.setAddr("杭州市西湖区文二西路");
        company1.setTel("0571-88888888");
        company1.setCertificationStatus(0);
        Company company2 = new Company();
        company2.setName("供应商名称");
        company2.setMemo("主营:时令蔬菜、干货");
        company2.setAddr("杭州市西湖区文二西路");
        company2.setTel("0571-88888888");
        company2.setCertificationStatus(0);
        Company company3 = new Company();
        company3.setName("供应商名称");
        company3.setMemo("主营:时令蔬菜、干货");
        company3.setAddr("杭州市西湖区文二西路");
        company3.setTel("0571-88888888");
        company3.setCertificationStatus(0);
        Company company4 = new Company();
        company4.setName("供应商名称");
        company4.setMemo("主营:时令蔬菜、干货");
        company4.setAddr("杭州市西湖区文二西路");
        company4.setTel("0571-88888888");
        company4.setCertificationStatus(0);
        Company company5 = new Company();
        company5.setName("供应商名称");
        company5.setMemo("主营:时令蔬菜、干货");
        company5.setAddr("杭州市西湖区文二西路");
        company5.setTel("0571-88888888");
        company5.setCertificationStatus(0);

        companylist.add(company1);
        companylist.add(company2);
        companylist.add(company3);
        companylist.add(company4);
        companylist.add(company5);

        adapter = new MyProviderViewAdapter(companylist, GLMyProviderActivity.this);
        Intent intent = new Intent();
        intent.putExtra("numOfProvider", String.valueOf(adapter.getCount()));
        setResult(10, intent);
    }

    public MyProviderViewAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected void initView() {
        mView = inflater.inflate(R.layout.activity_my_provider, null);
        shopListView = (PullableListView) mView.findViewById(R.id.myProviderList);
        shopListView.setAdapter(adapter);
        prlMyProviderRefresh = findView(mView, R.id.prlMyProviderRefresh);
        prlMyProviderRefresh.autoRefresh();
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    public void onPause() {
        super.onPause();
        prlMyProviderRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
        prlMyProviderRefresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.my_provider));
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
    protected void setListener() {
        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(GLMyProviderActivity.this, GLProviderInfoActivity.class);
                intent.putExtra(GLConst.INTENT_PARAM, adapter.getItem(i));
                GLViewManager.getIns().showActivity(mContext, intent, false);
            }
        });
        prlMyProviderRefresh.setOnRefreshListener(this);
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
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        // 下拉刷新操作
        mProviderLogic.fetchMySuppliers(mContext);
        mHandler.sendEmptyMessageDelayed(GLBaocaiLogic.REFRESH_SUCCESS, 5000);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        // 加载操作
        mHandler.sendEmptyMessageDelayed(GLBaocaiLogic.LOADED_SUCCESS, 5000);
    }

    private Handler mHandler = new Handler(GLCantingApp.getIns().getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GLBaocaiLogic.REFRESH_SUCCESS:
                    // 告诉控件刷新完毕了
                    adapter = (MyProviderViewAdapter) shopListView.getAdapter();
                    if (mProviderLogic.getMyProviders() != null) {
                        companylist.clear();
                        companylist = mProviderLogic.getMyProviders();
                        adapter.setCompanyList(companylist);
                    }
                    prlMyProviderRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
                    break;
                case GLBaocaiLogic.LOADED_SUCCESS:
                    // 告诉控件加载完毕了
                    prlMyProviderRefresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    break;
            }
        }
    };

}


