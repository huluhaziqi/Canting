package cn.caimatou.canting.modules.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.base.GLParentFragment;
import cn.caimatou.canting.bean.Company;
import cn.caimatou.canting.bean.GLBaocaiReportModel;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLCustomBroadcast;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.modules.GLCantingApp;
import cn.caimatou.canting.modules.activites.GLOrderDetailActivity;
import cn.caimatou.canting.modules.activites.GLSearchActivity;
import cn.caimatou.canting.modules.adapters.GLBaociReportAdapter;
import cn.caimatou.canting.modules.logic.GLBaocaiLogic;
import cn.caimatou.canting.utils.GLProgressDialogUtil;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.http.GLApiHandler;
import cn.caimatou.canting.utils.http.GLHttpRequestHelper;
import cn.caimatou.canting.widget.refresh.PullToRefreshLayout;
import cn.caimatou.canting.widget.refresh.PullableExpandableListView;

/**
 * Description：历史报菜单
 * <br/><br/>Created by Fausgoal on 15/8/30.
 * <br/><br/>
 */
public class GLHistoryBaocaiFragment extends GLParentFragment implements PullToRefreshLayout.OnRefreshListener {

    private View mView = null;
    private LinearLayout llEmpty = null;
    private PullToRefreshLayout prlRefresh = null;
    private PullableExpandableListView elvList = null;
    private GLBaociReportAdapter mAdapter = null;

    private List<GLBaocaiReportModel> mGroups = null;
    private List<List<Company>> mChildList = null;

    /**
     * 供应商id
     */
    private long mProviderId = GLConst.NEGATIVE;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_history_baocai_layout, container, false);
        return mView;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (null != bundle) {
            mProviderId = bundle.getLong(GLConst.INTENT_PARAM, GLConst.NEGATIVE);
        }
    }

    @Override
    protected void initView() {
        llEmpty = findView(mView, R.id.llEmpty);
        prlRefresh = findView(mView, R.id.prlRefresh);
        elvList = findView(mView, R.id.elvList);

        mAdapter = new GLBaociReportAdapter(mContext, null, null);
        elvList.setAdapter(mAdapter);

        elvList.setGroupIndicator(null);

        prlRefresh.autoRefresh();
        fetchData();

        elvList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        elvList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Company shopInfo = mAdapter.getChild(groupPosition, childPosition);
                Intent intent = new Intent(mContext, GLOrderDetailActivity.class);
                intent.putExtra(GLConst.INTENT_PARAM, shopInfo);
                GLViewManager.getIns().showActivity(mContext, intent, false);
                return true;
            }
        });
    }

    @Override
    protected void setListener() {
        prlRefresh.setOnRefreshListener(this);
    }

    private void fetchData() {
        // NOTICE 先从本地加载数据
        List<GLBaocaiReportModel> baocaiReportModels = new ArrayList<>();
        GLBaocaiReportModel reportModel = new GLBaocaiReportModel();
        reportModel.setTime(System.currentTimeMillis());

        List<Company> shopInfos = new ArrayList<>();
        Company shopInfo = new Company();
        shopInfo.setTel("0571-88889999");
        shopInfo.setDetailAddr("杭州市西湖区文一路");
        shopInfo.setMemo("蔬菜类、生鲜类");
        shopInfo.setName("天下第一条");
//        shopInfo.setCount(5);
//        shopInfo.setState(1);
        shopInfos.add(shopInfo);

        shopInfo = new Company();
        shopInfo.setTel("0571-88889999");
        shopInfo.setDetailAddr("杭州市西湖区文一路11");
        shopInfo.setMemo("蔬菜类、生鲜类11");
        shopInfo.setName("天下第一条11");
//        shopInfo.setCount(10);
//        shopInfo.setState(0);
        shopInfos.add(shopInfo);

        shopInfo = new Company();
        shopInfo.setTel("0571-88889999");
        shopInfo.setDetailAddr("杭州市西湖区文一路22");
        shopInfo.setMemo("蔬菜类、生鲜类22");
        shopInfo.setName("天下第一条22");
//        shopInfo.setCount(15);
//        shopInfo.setState(2);
        shopInfos.add(shopInfo);

        reportModel.setShopInfos(shopInfos);
        baocaiReportModels.add(reportModel);

        reportModel = new GLBaocaiReportModel();
        reportModel.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000 * 1));
        shopInfos = new ArrayList<>();
        shopInfo = new Company();
        shopInfo.setTel("0571-88889999");
        shopInfo.setDetailAddr("杭州市西湖区文一路1");
        shopInfo.setMemo("蔬菜类、生鲜类1");
        shopInfo.setName("天下第一条1");
//        shopInfo.setCount(1);
//        shopInfo.setState(3);
        shopInfos.add(shopInfo);
        reportModel.setShopInfos(shopInfos);
        baocaiReportModels.add(reportModel);

        reportModel = new GLBaocaiReportModel();
        reportModel.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000 * 2));
        shopInfos = new ArrayList<>();
        shopInfo = new Company();
        shopInfo.setTel("0571-88889999");
        shopInfo.setDetailAddr("杭州市西湖区文一路3");
        shopInfo.setMemo("蔬菜类、生鲜类3");
        shopInfo.setName("天下第一条3");
//        shopInfo.setCount(50);
//        shopInfo.setState(1);
        shopInfos.add(shopInfo);
        reportModel.setShopInfos(shopInfos);
        baocaiReportModels.add(reportModel);

        reportModel = new GLBaocaiReportModel();
        reportModel.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000 * 3));
        shopInfos = new ArrayList<>();
        shopInfo = new Company();
        shopInfo.setTel("0571-88889999");
        shopInfo.setDetailAddr("杭州市西湖区文一路4");
        shopInfo.setMemo("蔬菜类、生鲜类4");
        shopInfo.setName("天下第一条4");
//        shopInfo.setCount(51);
//        shopInfo.setState(2);
        shopInfos.add(shopInfo);
        reportModel.setShopInfos(shopInfos);
        baocaiReportModels.add(reportModel);

        reportModel = new GLBaocaiReportModel();
        reportModel.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000 * 4));
        shopInfos = new ArrayList<>();
        shopInfo = new Company();
        shopInfo.setTel("0571-88889999");
        shopInfo.setDetailAddr("杭州市西湖区文一路5");
        shopInfo.setMemo("蔬菜类、生鲜类5");
        shopInfo.setName("天下第一条5");
//        shopInfo.setCount(5);
//        shopInfo.setState(0);
        shopInfos.add(shopInfo);
        reportModel.setShopInfos(shopInfos);
        baocaiReportModels.add(reportModel);

        reportModel = new GLBaocaiReportModel();
        reportModel.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000 * 5));
        shopInfos = new ArrayList<>();
        shopInfo = new Company();
        shopInfo.setTel("0571-88889999");
        shopInfo.setDetailAddr("杭州市西湖区文一路2");
        shopInfo.setMemo("蔬菜类、生鲜类2");
        shopInfo.setName("天下第一条2");
//        shopInfo.setCount(225);
//        shopInfo.setState(3);
        shopInfos.add(shopInfo);
        reportModel.setShopInfos(shopInfos);
        baocaiReportModels.add(reportModel);

        initAdapterData(baocaiReportModels);
        setAdapterData();
    }

    private void fetchHisOrdersForServ() {
        GLHttpRequestHelper.hisOrders(mContext, new GLApiHandler(mContext) {
            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);
                mHandler.sendEmptyMessageDelayed(GLBaocaiLogic.REFRESH_SUCCESS, 100);
            }

            @Override
            public void onFailure(GLBaseInfo baseInfo) {
                super.onFailure(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);
                mHandler.sendEmptyMessageDelayed(GLBaocaiLogic.REFRESH_SUCCESS, 100);
                if (null != baseInfo) {
                    GLCommonManager.makeText(mContext, baseInfo.getMessage());
                }
            }
        });
    }

    private void initAdapterData(List<GLBaocaiReportModel> baocaiReportModels) {
        mGroups = new ArrayList<>();
        mChildList = new ArrayList<>();
        if (null != baocaiReportModels) {
            for (int i = GLConst.NONE; i < baocaiReportModels.size(); i++) {
                GLBaocaiReportModel reportModel = baocaiReportModels.get(i);
                mGroups.add(reportModel);
                List<Company> shopInfos = reportModel.getShopInfos();
                mChildList.add(shopInfos);
            }
        }
    }

    private void setAdapterData() {
        mAdapter.clear();
        mAdapter.appendGroups(mGroups);
        mAdapter.appendChild(mChildList);

        for (int i = GLConst.NONE; i < mAdapter.getGroupCount(); i++) {
            elvList.expandGroup(i);
        }
        if ((null != mGroups && !mGroups.isEmpty()) || null != mChildList && !mChildList.isEmpty()) {
            setShowEmptyOrData(false);
        } else {
            setShowEmptyOrData(true);
        }
    }

    private void setShowEmptyOrData(boolean isEmpty) {
        if (isEmpty) {
            llEmpty.setVisibility(View.VISIBLE);
            prlRefresh.setVisibility(View.GONE);
        } else {
            llEmpty.setVisibility(View.GONE);
            prlRefresh.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        prlRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
        prlRefresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        // 下拉刷新操作
        if (mProviderId == GLConst.NEGATIVE) {
            // 主界面进入的，获取全部供应商的历史数据
            fetchHisOrdersForServ();
        } else {
            // 订单界面进入的，获取该供应商的历史数据
        }
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        // 加载操作
        mHandler.sendEmptyMessageDelayed(GLBaocaiLogic.LOADED_SUCCESS, 5000);
    }

    private void search(String providerName, long time) {
        mHandler.sendEmptyMessageDelayed(GLBaocaiLogic.REFRESH_SUCCESS, 5000);
    }

    private Handler mHandler = new Handler(GLCantingApp.getIns().getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GLBaocaiLogic.REFRESH_SUCCESS:
                    // 告诉控件刷新完毕了
                    prlRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
                    GLProgressDialogUtil.dismissProgress(mContext);
                    break;
                case GLBaocaiLogic.LOADED_SUCCESS:
                    // 告诉控件加载完毕了
                    prlRefresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    GLProgressDialogUtil.dismissProgress(mContext);
                    break;
            }
        }
    };

    @Override
    protected IntentFilter getStickyIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(GLCustomBroadcast.ON_SEARCH_KEYWORD_ACTION);
        filter.addAction(GLCustomBroadcast.ON_SEARCH_DATE_ACTION);
        return filter;
    }

    @Override
    protected void onStickyNotify(Context context, Intent intent) {
        String action = intent.getAction();
        // 进行搜索
        if (GLCustomBroadcast.ON_SEARCH_KEYWORD_ACTION.equals(action)) {
            GLProgressDialogUtil.showProgress(mContext, GLResourcesUtil.getString(R.string.searching));
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                Company shopInfo = (Company) bundle.get(GLConst.INTENT_PARAM);
                int searchType = bundle.getInt(GLConst.INTENT_PARAM1);
                if (null != shopInfo
                        && searchType == GLSearchActivity.SEARCH_BAOCAI_REPORT) {
                    search(shopInfo.getName(), 0);
                }
            }
        } else if (GLCustomBroadcast.ON_SEARCH_DATE_ACTION.equals(action)) {
            GLProgressDialogUtil.showProgress(mContext, GLResourcesUtil.getString(R.string.searching));
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                long time = bundle.getLong(GLConst.INTENT_PARAM);
                int searchType = bundle.getInt(GLConst.INTENT_PARAM1);
                if (searchType == GLSearchActivity.SEARCH_BAOCAI_REPORT) {
                    search("", time);
                }
            }
        }
    }
}
