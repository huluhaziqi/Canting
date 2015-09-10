package cn.caimatou.canting.modules.fragments;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentFragment;
import cn.caimatou.canting.base.ShopListAdapter;
import cn.caimatou.canting.bean.Company;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLCompanyManager;
import cn.caimatou.canting.modules.GLCantingApp;
import cn.caimatou.canting.modules.activites.GLProviderInfoActivity;
import cn.caimatou.canting.modules.logic.GLProviderLogic;
import cn.caimatou.canting.threadpool.GLFuture;
import cn.caimatou.canting.threadpool.GLFutureListener;
import cn.caimatou.canting.threadpool.GLThreadPool;
import cn.caimatou.canting.widget.ClassifyGridView;
import cn.caimatou.canting.widget.DistrictSelectedView;
import cn.caimatou.canting.widget.OrderListView;
import cn.caimatou.canting.widget.refresh.PullToRefreshLayout;
import cn.caimatou.canting.widget.refresh.PullableListView;


/**
 * Created by mac on 15/8/26.
 */
public class GLProviderFragment extends GLParentFragment implements PullToRefreshLayout.OnRefreshListener {
    private View mView;
    private ClassifyGridView gridView;
    private OrderListView listView;
    private PullableListView shopListView;
    private RelativeLayout district, classify, reorder;
    private DistrictSelectedView districtView;
    private PopupWindow disPopup, claPopup, orderPopup;
    private View line;

    private int displayWidth, displayHeight;
    private boolean isDisOpen = false, isClaOpen = false, isOrderOpen = false;
    private List<Company> providers;
    private ShopListAdapter adapter;
    private PullToRefreshLayout prlRefresh = null;
    private GLProviderLogic mProviderLogic = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.gl_provider_layout, container, false);
        displayWidth = GLProviderFragment.this.getActivity().getWindowManager().getDefaultDisplay().getWidth();
        displayHeight = GLProviderFragment.this.getActivity().getWindowManager().getDefaultDisplay().getHeight();
        return mView;
    }

    @Override
    protected void initData() {
        mProviderLogic = new GLProviderLogic(mContext);
        providers = new ArrayList<Company>();
        adapter = new ShopListAdapter(providers, GLProviderFragment.this.getActivity());
        fetchCompaniesForLoc();
    }

    @Override
    protected void initView() {
        district = (RelativeLayout) mView.findViewById(R.id.district_classify);
        classify = (RelativeLayout) mView.findViewById(R.id.veg_classify);
        reorder = (RelativeLayout) mView.findViewById(R.id.reorder_classify);
        shopListView = (PullableListView) mView.findViewById(R.id.shop_list);
        line = mView.findViewById(R.id.provider_line);
        districtView = new DistrictSelectedView(GLProviderFragment.this.getActivity());
        gridView = new ClassifyGridView(GLProviderFragment.this.getActivity());
        listView = new OrderListView(GLProviderFragment.this.getActivity());
        ((TextView) district.findViewById(R.id.buttonText)).setText(getResources().getString(R.string.district));
        district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDisPopupWindow();
                closeClaPopupWindow();
                closeOrdPopupWindow();
            }
        });
        ((TextView) classify.findViewById(R.id.buttonText)).setText(getResources().getString(R.string.classify));
        classify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startClaPopupWindow();
                closeDisPopupWindow();
                closeOrdPopupWindow();
            }
        });
        ((TextView) reorder.findViewById(R.id.buttonText)).setText(getResources().getString(R.string.reorder));
        reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startOrderPopupWindow();
                closeClaPopupWindow();
                closeDisPopupWindow();
            }
        });
        shopListView.setAdapter(adapter);
        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Company supply = adapter.getItem(i);
                Intent intent = new Intent(GLProviderFragment.this.getActivity(), GLProviderInfoActivity.class);
                intent.putExtra(GLConst.INTENT_PARAM, supply);
                GLViewManager.getIns().showActivity(GLProviderFragment.this.getActivity(), intent, false);
            }
        });
        prlRefresh = findView(mView, R.id.prlRefresh);
        prlRefresh.autoRefresh();
    }

    @Override
    protected void setListener() {
        initListener();
        prlRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        prlRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
        prlRefresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    private void startDisPopupWindow() {
        isDisOpen = !isDisOpen;
        if (disPopup == null) {
            disPopup = new PopupWindow(districtView, displayWidth, displayHeight / 3);
            disPopup.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        onPressBack(disPopup);
                        isDisOpen = false;
                        return true;
                    }
                    return false;
                }
            });
            disPopup.setBackgroundDrawable(new BitmapDrawable());
            disPopup.setAnimationStyle(R.style.PopupWindowAnimation);
            disPopup.setFocusable(false);
            disPopup.setOutsideTouchable(true);
        }
        if (isDisOpen && !disPopup.isShowing()) {
            ((TextView) district.findViewById(R.id.buttonText)).setTextColor(getResources().getColor(R.color.green));
            ((ImageView) district.findViewById(R.id.buttonPressIcon)).setImageResource(R.mipmap.choosebar_press_up);
            if (disPopup.getContentView() != districtView) {
                disPopup.setContentView(districtView);
            }
            disPopup.showAsDropDown(line, 0, 0);
        }
    }

    private void startClaPopupWindow() {
        isClaOpen = !isClaOpen;
        if (claPopup == null) {
            claPopup = new PopupWindow(gridView, displayWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            claPopup.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        onPressBack(claPopup);
                        isClaOpen = false;
                        return true;
                    }
                    return false;
                }
            });
            claPopup.setBackgroundDrawable(new BitmapDrawable());
            claPopup.setAnimationStyle(R.style.PopupWindowAnimation);
            claPopup.setFocusable(false);
            claPopup.setOutsideTouchable(true);
        }
        if (isClaOpen && !claPopup.isShowing()) {
            ((TextView) classify.findViewById(R.id.buttonText)).setTextColor(getResources().getColor(R.color.green));
            ((ImageView) classify.findViewById(R.id.buttonPressIcon)).setImageResource(R.mipmap.choosebar_press_up);
            if (claPopup.getContentView() != gridView) {
                claPopup.setContentView(gridView);
            }
            claPopup.showAsDropDown(line, 0, 0);
        }
    }

    private void startOrderPopupWindow() {
        isOrderOpen = !isOrderOpen;
        if (orderPopup == null) {
            orderPopup = new PopupWindow(gridView, displayWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            orderPopup.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        onPressBack(orderPopup);
                        isOrderOpen = false;
                        return true;
                    }
                    return false;
                }
            });
            orderPopup.setBackgroundDrawable(new BitmapDrawable());
            orderPopup.setAnimationStyle(R.style.PopupWindowAnimation);
            orderPopup.setFocusable(false);
            orderPopup.setOutsideTouchable(true);
        }
        if (isOrderOpen && !orderPopup.isShowing()) {
            ((TextView) reorder.findViewById(R.id.buttonText)).setTextColor(getResources().getColor(R.color.green));
            ((ImageView) reorder.findViewById(R.id.buttonPressIcon)).setImageResource(R.mipmap.choosebar_press_up);
            if (orderPopup.getContentView() != listView) {
                orderPopup.setContentView(listView);
            }
            orderPopup.showAsDropDown(line, 0, 0);
        }
    }

    private void closeDisPopupWindow() {
        if (isDisOpen && disPopup != null && disPopup.isShowing()) {
            isDisOpen = !isDisOpen;
            resetDisPopup();
            disPopup.dismiss();
        }
    }

    private void closeClaPopupWindow() {
        if (isClaOpen && claPopup != null && claPopup.isShowing()) {
            isClaOpen = !isClaOpen;
            resetClaPopup();
            claPopup.dismiss();
        }
    }

    private void closeOrdPopupWindow() {
        if (isOrderOpen && orderPopup != null && orderPopup.isShowing()) {
            isOrderOpen = !isOrderOpen;
            resetOrdPopup();
            orderPopup.dismiss();
        }
    }

    private void initListener() {
        districtView.setOnSelectListener(new DistrictSelectedView.OnSelectListener() {
            @Override
            public void getValue(String showText) {
                onRefresh(districtView, showText);
            }
        });
        gridView.setOnSelectListener(new ClassifyGridView.OnSelectListener() {
            @Override
            public void getValue(String showText) {
                onRefresh(gridView, showText);
            }
        });
        listView.setOnSelectListener(new OrderListView.OnSelectListener() {
            @Override
            public void getValue(String showText) {
                onRefresh(listView, showText);
            }
        });
    }

    private void onRefresh(View view, String message) {
        if (view == districtView) {
            onPressBack(disPopup);
        } else if (view == gridView) {
            onPressBack(claPopup);
        } else {
            onPressBack(orderPopup);
        }
        // TODO 根据message请求数据
        Toast.makeText(GLProviderFragment.this.getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 如果菜单成展开状态，则让菜单收回去
     */
    public boolean onPressBack(PopupWindow popupWindow) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            resetPopup(popupWindow);
            return true;
        } else {
            return false;
        }
    }

    private void resetPopup(PopupWindow popupWindow) {
        if (popupWindow == disPopup) {
            ((TextView) district.findViewById(R.id.buttonText)).setTextColor(getResources().getColor(R.color.gray4));
            ((ImageView) district.findViewById(R.id.buttonPressIcon)).setImageResource(R.mipmap.choosebar_press_down);
            isDisOpen = false;
        } else if (popupWindow == claPopup) {
            ((TextView) classify.findViewById(R.id.buttonText)).setTextColor(getResources().getColor(R.color.gray4));
            ((ImageView) classify.findViewById(R.id.buttonPressIcon)).setImageResource(R.mipmap.choosebar_press_down);
            isClaOpen = false;
        } else {
            ((TextView) reorder.findViewById(R.id.buttonText)).setTextColor(getResources().getColor(R.color.gray4));
            ((ImageView) reorder.findViewById(R.id.buttonPressIcon)).setImageResource(R.mipmap.choosebar_press_down);
            isOrderOpen = false;
        }
    }

    private void resetDisPopup() {
        ((TextView) district.findViewById(R.id.buttonText)).setTextColor(getResources().getColor(R.color.gray4));
        ((ImageView) district.findViewById(R.id.buttonPressIcon)).setImageResource(R.mipmap.choosebar_press_down);
    }

    private void resetClaPopup() {
        ((TextView) classify.findViewById(R.id.buttonText)).setTextColor(getResources().getColor(R.color.gray4));
        ((ImageView) classify.findViewById(R.id.buttonPressIcon)).setImageResource(R.mipmap.choosebar_press_down);
    }

    private void resetOrdPopup() {
        ((TextView) reorder.findViewById(R.id.buttonText)).setTextColor(getResources().getColor(R.color.gray4));
        ((ImageView) reorder.findViewById(R.id.buttonPressIcon)).setImageResource(R.mipmap.choosebar_press_down);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        mProviderLogic.fetch(mContext);
        mHandler.sendEmptyMessageDelayed(GLProviderLogic.REFRESH_SUCCESS, 5000);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        // 加载操作
        mHandler.sendEmptyMessageDelayed(GLProviderLogic.LOADED_SUCCESS, 5000);
    }

    private void fetchCompaniesForLoc() {
        GLCantingApp.getIns().getThreadPool().submit(new GLThreadPool.Job<List<Company>>() {
            @Override
            public List<Company> run() {
                return GLCompanyManager.getAllCompany(mContext);
            }
        }, new GLFutureListener<List<Company>>() {
            @Override
            public void onFutureDone(GLFuture<List<Company>> future) {
                adapter.append(future.get());
            }
        });
    }

    private Handler mHandler = new Handler(GLCantingApp.getIns().getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GLProviderLogic.REFRESH_SUCCESS:
                    // 告诉控件刷新完毕了
                    adapter = (ShopListAdapter) shopListView.getAdapter();
                    if (mProviderLogic.getProviders() != null && mProviderLogic.getProviders().size() != 0) {
                        adapter.clear();
                        adapter.append(mProviderLogic.getProviders());
                    }
                    prlRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
                    break;
                case GLProviderLogic.LOADED_SUCCESS:
                    // 告诉控件加载完毕了
                    prlRefresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    break;
            }
        }
    };

}
