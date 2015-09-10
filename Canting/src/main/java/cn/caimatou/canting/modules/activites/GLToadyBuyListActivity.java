package cn.caimatou.canting.modules.activites;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.BaocaiList;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.modules.GLCantingApp;
import cn.caimatou.canting.modules.adapters.GLTodayBuyListAdapter;
import cn.caimatou.canting.modules.fragments.GLHistoryBaocaiFragment;
import cn.caimatou.canting.modules.logic.GLBaocaiLogic;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.widget.GLNavigationBar;
import cn.caimatou.canting.widget.refresh.PullToRefreshLayout;
import cn.caimatou.canting.widget.refresh.PullableListView;

/**
 * Created by Rosicky on 15/9/1.
 */
public class GLToadyBuyListActivity extends GLParentActivity implements PullToRefreshLayout.OnRefreshListener {
    private View mView;
//    private View headView;
//    private PullableListView listView;
//    private PullToRefreshLayout todayButListRefresh = null;

    private LayoutInflater inflater;
    private GLTodayBuyListAdapter adapter;
    private ArrayList<BaocaiList> list = new ArrayList<BaocaiList>();
    private FragmentManager fragmentManager;
    private GLHistoryBaocaiFragment historyBaocaiFragment;

    @Override
    protected void initData() {
        mContext = this;
        inflater = LayoutInflater.from(mContext);
        fragmentManager = getSupportFragmentManager();
        historyBaocaiFragment = new GLHistoryBaocaiFragment();
//        fetchBuyList();
//        adapter = new GLTodayBuyListAdapter(mContext, list, null);
    }

    private void fetchBuyList() {
        // TODO 获取今日报菜单
        BaocaiList baocai1 = new BaocaiList();
        BaocaiList baocai2 = new BaocaiList();
        BaocaiList baocai3 = new BaocaiList();
        baocai1.setShopName("新桥门店");
        baocai2.setShopName("北门店");
        baocai3.setShopName("东门店");
        baocai1.setBuyCount(3);
        baocai2.setBuyCount(4);
        baocai3.setBuyCount(5);
        baocai1.setListStatus(0);
        baocai2.setListStatus(1);
        baocai3.setListStatus(2);
        list.add(baocai1);
        list.add(baocai2);
        list.add(baocai3);
    }

    @Override
    protected void initView() {
        mView = inflater.inflate(R.layout.activity_today_buylist, null);
        fragmentManager.beginTransaction().replace(R.id.today_buy_fragment, historyBaocaiFragment).commit();
//        headView = inflater.inflate(R.layout.today_buylist_headview, null);
//        listView = (PullableListView) mView.findViewById(R.id.todayBuyList);
//        listView.addHeaderView(headView);
//        listView.setAdapter(adapter);
//        todayButListRefresh = findView(mView, R.id.todayButListRefresh);
//        todayButListRefresh.autoRefresh();
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    protected void setListener() {
//        todayButListRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        todayButListRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
//        todayButListRefresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.today_buy));
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
            return false;
        }
        return super.onItemSelectedListener(viewId);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        // 下拉刷新操作
//        mHandler.sendEmptyMessageDelayed(GLBaocaiLogic.REFRESH_SUCCESS, 5000);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        // 加载操作
//        mHandler.sendEmptyMessageDelayed(GLBaocaiLogic.LOADED_SUCCESS, 5000);
    }

//    private Handler mHandler = new Handler(GLCantingApp.getIns().getMainLooper()) {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case GLBaocaiLogic.REFRESH_SUCCESS:
//                    // 告诉控件刷新完毕了
//                    todayButListRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
//                    break;
//                case GLBaocaiLogic.LOADED_SUCCESS:
//                    // 告诉控件加载完毕了
//                    todayButListRefresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//                    break;
//            }
//        }
//    };
}
