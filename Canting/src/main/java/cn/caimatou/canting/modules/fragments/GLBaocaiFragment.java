package cn.caimatou.canting.modules.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.base.GLParentFragment;
import cn.caimatou.canting.bean.Order;
import cn.caimatou.canting.callback.IGLOnListItemClickListener;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLCustomBroadcast;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.manager.GLOrderManager;
import cn.caimatou.canting.modules.GLCantingApp;
import cn.caimatou.canting.modules.activites.GLBaocaiOrderActivity;
import cn.caimatou.canting.modules.adapters.GLBaocaiAdapter;
import cn.caimatou.canting.modules.logic.GLBaocaiLogic;
import cn.caimatou.canting.response.GLMyOrdersResp;
import cn.caimatou.canting.threadpool.GLFuture;
import cn.caimatou.canting.threadpool.GLFutureListener;
import cn.caimatou.canting.threadpool.GLThreadPool;
import cn.caimatou.canting.utils.GLListUtil;
import cn.caimatou.canting.utils.GLProgressDialogUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;
import cn.caimatou.canting.utils.http.GLApiHandler;
import cn.caimatou.canting.utils.http.GLHttpRequestHelper;
import cn.caimatou.canting.widget.refresh.PullToRefreshLayout;
import cn.caimatou.canting.widget.refresh.PullableListView;

/**
 * Description：主界面报菜
 * <br/><br/>Created by Fausgoal on 15/8/30.
 * <br/><br/>
 */
public class GLBaocaiFragment extends GLParentFragment implements IGLOnListItemClickListener, PullToRefreshLayout.OnRefreshListener {
    private static final String TAG = "GLBaocaiFragment";

    private View mView = null;
    private LinearLayout llEmpty = null;
    private TextView tvAddProvider = null;

    private PullToRefreshLayout prlRefresh = null;

    private GLBaocaiAdapter mAdapter = null;
    private List<Order> mOrders = null;

    private int mPageIndex = GLConst.ONE;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_baocai_layout, container, false);
        return mView;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        llEmpty = findView(mView, R.id.llEmpty);
        tvAddProvider = findView(mView, R.id.tvAddProvider);

        prlRefresh = findView(mView, R.id.prlRefresh);
        PullableListView lvList = findView(mView, R.id.lvList);

        lvList.setCanPullDown(false);
        lvList.setCanPullUp(false);

        llEmpty.setVisibility(View.GONE);

        mAdapter = new GLBaocaiAdapter(mContext, null, this);
        lvList.setAdapter(mAdapter);

//        prlRefresh.autoRefresh();
        fetchData();
//        List<Order> orders = new ArrayList<>();
//        Order order = new Order();
//        order.setSupplierAddr("杭州西湖区");
//        order.setSupplierAddrDetail("文一西路");
//        order.setSupplierBusiness("生鲜、青菜");
//        order.setSupplierCompany("大东北青菜店");
//        order.setSupplierTel("0571-99998888");
//        order.setSupplierCompanyId(143555599);
//        orders.add(order);
//        mAdapter.append(orders);
    }

    private void setIsEmpty(boolean isEmpty) {
        if (isEmpty) {
            llEmpty.setVisibility(View.VISIBLE);
            prlRefresh.setVisibility(View.GONE);
        } else {
            llEmpty.setVisibility(View.GONE);
            prlRefresh.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void setListener() {
        prlRefresh.setOnRefreshListener(this);
        GLViewClickUtil.setNoFastClickListener(tvAddProvider, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        prlRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
        prlRefresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    private void fetchData() {
        // NOTICE 先从本地加载数据

        GLCantingApp.getIns().getThreadPool().submit(new GLThreadPool.Job<List<Order>>() {
            @Override
            public List<Order> run() {
                return GLOrderManager.getOrders(mContext);
            }
        }, new GLFutureListener<List<Order>>() {
            @Override
            public void onFutureDone(GLFuture<List<Order>> future) {
                mOrders = future.get();
                mAdapter.clear();
                mAdapter.append(mOrders);
            }
        });

        fetchMyOrdersForServ();
    }

    private void fetchMyOrdersForServ() {
        GLProgressDialogUtil.showProgress(mContext);
        GLHttpRequestHelper.myOrders(mContext, new GLApiHandler(mContext) {
            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);

                GLMyOrdersResp myOrdersResp = (GLMyOrdersResp) baseInfo;
                mOrders = myOrdersResp.getResult();

                GLOrderManager.saveOrders(mContext, mOrders);

                GLProgressDialogUtil.dismissProgress(mContext);
                mHandler.sendEmptyMessageDelayed(GLBaocaiLogic.REFRESH_SUCCESS, 100);

                setIsEmpty(GLListUtil.isEmpty(mOrders));

                if (!GLListUtil.isEmpty(mOrders)) {
                    mAdapter.clear();
                    mAdapter.append(mOrders);
                }
            }

            @Override
            public void onFailure(GLBaseInfo baseInfo) {
                super.onFailure(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);
                mHandler.sendEmptyMessageDelayed(GLBaocaiLogic.REFRESH_SUCCESS, 100);
                setIsEmpty(true);
                if (null != baseInfo) {
                    GLCommonManager.makeText(mContext, baseInfo.getMessage());
                }
            }
        });
    }

    @Override
    public void onClickItem(int position, View v) {
        Order order = mAdapter.getItem(position);
        Intent intent = new Intent(mContext, GLBaocaiOrderActivity.class);
        intent.putExtra(GLConst.INTENT_PARAM, order);
        intent.putExtra(GLConst.INTENT_PARAM1, GLBaocaiLogic.ENTER_ORDER_FOR_MY_ORDER);
        GLViewManager.getIns().showActivity(mContext, intent, false);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.tvAddProvider:
                Intent intent = new Intent();
                intent.setAction(GLCustomBroadcast.ON_CHANGE_TO_PROVIDER_ACTION);
                mContext.sendBroadcast(intent);
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        // 下拉刷新操作
        mPageIndex = GLConst.ONE;
        fetchMyOrdersForServ();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        // 加载操作
        mPageIndex += GLConst.ONE;
        mHandler.sendEmptyMessageDelayed(GLBaocaiLogic.LOADED_SUCCESS, 5000);
    }

    private Handler mHandler = new Handler(GLCantingApp.getIns().getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GLBaocaiLogic.REFRESH_SUCCESS:
                    // 告诉控件刷新完毕了
                    prlRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
                    break;
                case GLBaocaiLogic.LOADED_SUCCESS:
                    // 告诉控件加载完毕了
                    prlRefresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    break;
            }
        }
    };
}
