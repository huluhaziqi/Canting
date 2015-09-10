package cn.caimatou.canting.modules.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.GLDeliveryAddressModel;
import cn.caimatou.canting.callback.IGLOnListItemClickListener;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLAddressManager;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.modules.adapters.GLDeliveryAddressAdapter;
import cn.caimatou.canting.response.GLAddressCompList;
import cn.caimatou.canting.threadpool.GLFuture;
import cn.caimatou.canting.threadpool.GLFutureListener;
import cn.caimatou.canting.threadpool.GLThreadPool;
import cn.caimatou.canting.utils.GLListUtil;
import cn.caimatou.canting.utils.GLProgressDialogUtil;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;
import cn.caimatou.canting.utils.http.GLApiHandler;
import cn.caimatou.canting.utils.http.GLHttpRequestHelper;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Description：配送地址
 * <br/><br/>Created by Fausgoal on 15/9/2.
 * <br/><br/>
 */
public class GLDeliveryAddressActivity extends GLParentActivity implements IGLOnListItemClickListener {
    private View mView = null;
    private Button btnManagerAddress = null;
    private GLDeliveryAddressAdapter mAdapter = null;
    private GLDeliveryAddressModel mSelectedDeliveryAddress = null;

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mSelectedDeliveryAddress = (GLDeliveryAddressModel) bundle.get(GLConst.INTENT_PARAM);
        }
    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.activity_delivery_address_layout, null, false);
        ListView lvList = findView(mView, R.id.lvList);
        btnManagerAddress = findView(mView, R.id.btnManagerAddress);

        mAdapter = new GLDeliveryAddressAdapter(mContext, null, true, this);
        lvList.setAdapter(mAdapter);

        fetchAddressListForLoc();
        fetchAddressForServ();
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
                setSelected(future.get());
                mAdapter.clear();
                mAdapter.append(future.get());
            }
        });
    }

    private void fetchAddressForServ() {
        GLProgressDialogUtil.showProgress(mContext);
        GLHttpRequestHelper.addressCompList(mContext, new GLApiHandler(mContext) {
            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);
                GLAddressCompList addressResp = (GLAddressCompList) baseInfo;
                List<GLDeliveryAddressModel> addressModels = addressResp.getResult();

                GLAddressManager.saveAddress(mContext, addressModels);
                setSelected(addressModels);
                if (null != addressModels) {
                    mAdapter.clear();
                    mAdapter.append(addressModels);
                }
            }

            @Override
            public void onFailure(GLBaseInfo baseInfo) {
                super.onFailure(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);
                if (null != baseInfo) {
                    GLCommonManager.makeText(mContext, baseInfo.getMessage());
                }
            }
        });
    }

    private void setSelected(List<GLDeliveryAddressModel> addressModels) {
        if (GLListUtil.isEmpty(addressModels)) {
            return;
        }
        boolean isSetSelected = false;
        for (int i = GLConst.NONE; i < addressModels.size(); i++) {
            GLDeliveryAddressModel temp = addressModels.get(i);
            if (null != mSelectedDeliveryAddress) {
                if (temp.getId() == mSelectedDeliveryAddress.getId()) {
                    temp.setIsSelected(true);
                    isSetSelected = true;
                } else {
                    temp.setIsSelected(false);
                }
            } else {
                // 如果没有选择，使用默认地址
                if (temp.getIsDefault() == 1) {
                    temp.setIsSelected(true);
                    isSetSelected = true;
                } else {
                    temp.setIsSelected(false);
                }
            }
        }
//        if (!isSetSelected) {
//            addressModels.get(GLConst.NONE).setIsSelected(true);
//        }
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
        for (int i = GLConst.NONE; i < mAdapter.getCount(); i++) {
            GLDeliveryAddressModel temp = mAdapter.getItem(i);
            if (temp.getId() == deliveryAddressModel.getId()) {
                deliveryAddressModel.setIsSelected(true);
                break;
            } else {
                deliveryAddressModel.setIsSelected(false);
            }
        }
        Intent intent = new Intent();
        intent.putExtra(GLConst.INTENT_PARAM, deliveryAddressModel);
        setResult(Activity.RESULT_OK, intent);
        onBack();
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.btnManagerAddress:
                Intent intent = new Intent(mContext, GLManagerDeliveryAddressActivity.class);
                ArrayList<GLDeliveryAddressModel> addressModels = new ArrayList<>();
                List<GLDeliveryAddressModel> deliveryAddressModels = mAdapter.getList();
                if (null != deliveryAddressModels) {
                    addressModels.addAll(deliveryAddressModels);
                }
                intent.putExtra(GLConst.INTENT_PARAM, addressModels);
                GLViewManager.getIns().showActivity(mContext, intent, GLConst.REQUEST_MANAGER_DELIVERY_ADDRESS);
                break;
        }
    }

    private void setSelectedAddress() {
        if (null != mAdapter.getList()) {
            for (int i = GLConst.NONE; i < mAdapter.getList().size(); i++) {
                GLDeliveryAddressModel addressModel = mAdapter.getItem(i);
                if (addressModel.isSelected()) {
                    Intent intent = new Intent();
                    intent.putExtra(GLConst.INTENT_PARAM, addressModel);
                    setResult(Activity.RESULT_OK, intent);
                    break;
                }
            }
        }
    }

    private void onBack() {
        GLViewManager.getIns().pop(this);
    }

    @Override
    public boolean onItemSelectedListener(int viewId) {
        if (viewId == R.id.ivNavLeft) {
            setSelectedAddress();
            onBack();
            return false;
        }
        return super.onItemSelectedListener(viewId);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setSelectedAddress();
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
                }
                break;
        }
    }
}
