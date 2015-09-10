package cn.caimatou.canting.modules.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.GLDeliveryAddressModel;
import cn.caimatou.canting.callback.IGLOnListItemClickListener;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLAddressManager;
import cn.caimatou.canting.modules.adapters.GLDeliveryAddressAdapter;
import cn.caimatou.canting.threadpool.GLFuture;
import cn.caimatou.canting.threadpool.GLFutureListener;
import cn.caimatou.canting.threadpool.GLThreadPool;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Description：管理配送地址
 * <br/><br/>Created by Fausgoal on 15/9/2.
 * <br/><br/>
 */
public class GLManagerDeliveryAddressActivity extends GLParentActivity implements IGLOnListItemClickListener {
    private View mView = null;
    private Button btnManagerAddress = null;
    private GLDeliveryAddressAdapter mAdapter = null;
    private List<GLDeliveryAddressModel> mDeliveryAddressModels = null;

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mDeliveryAddressModels = (List<GLDeliveryAddressModel>) bundle.get(GLConst.INTENT_PARAM);
        }
    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.activity_delivery_address_layout, null, false);
        ListView lvList = findView(mView, R.id.lvList);
        btnManagerAddress = findView(mView, R.id.btnManagerAddress);

        btnManagerAddress.setText(GLResourcesUtil.getString(R.string.add_address));

        mAdapter = new GLDeliveryAddressAdapter(mContext, null, false, this);
        lvList.setAdapter(mAdapter);
        mAdapter.append(mDeliveryAddressModels);
    }

    private void fetchAddressListForLoc() {
        mApp.getThreadPool().submit(new GLThreadPool.Job<List<GLDeliveryAddressModel>>() {
            @Override
            public List<GLDeliveryAddressModel> run() {
                return GLAddressManager.getAllAddress(mContext);
            }
        }, new GLFutureListener<List<GLDeliveryAddressModel>>() {
            @Override
            public void onFutureDone(GLFuture<List<GLDeliveryAddressModel>> future) {
                mDeliveryAddressModels = future.get();
                mAdapter.clear();
                mAdapter.append(future.get());
            }
        });
    }

    @Override
    protected void setListener() {
        GLViewClickUtil.setNoFastClickListener(btnManagerAddress, this);
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.delivery_address));
    }

    @Override
    public void onClickItem(int position, View v) {
        GLDeliveryAddressModel deliveryAddressModel = mAdapter.getItem(position);
        Intent intent = new Intent(mContext, GLEditDeliveryAddressActivity.class);
        intent.putExtra(GLConst.INTENT_PARAM, deliveryAddressModel);
        intent.putExtra(GLConst.INTENT_PARAM1, position);
        GLViewManager.getIns().showActivity(mContext, intent, GLConst.REQUEST_MANAGER_DELIVERY_ADDRESS);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.btnManagerAddress:
                Intent intent = new Intent(mContext, GLEditDeliveryAddressActivity.class);
                intent.putExtra(GLConst.INTENT_PARAM1, GLConst.NEGATIVE);
                GLViewManager.getIns().showActivity(mContext, intent, GLConst.REQUEST_MANAGER_DELIVERY_ADDRESS);
                break;
        }
    }

    private void onBack() {
        GLViewManager.getIns().pop(this);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GLConst.REQUEST_MANAGER_DELIVERY_ADDRESS:
                if (resultCode == Activity.RESULT_OK) {
                    fetchAddressListForLoc();
                    setResult(Activity.RESULT_OK);
                }
                break;
        }
    }
}
