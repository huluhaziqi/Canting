package cn.caimatou.canting.modules.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.GLCaipinModel;
import cn.caimatou.canting.bean.GLDeliveryAddressModel;
import cn.caimatou.canting.bean.GLDeliveryTimeModel;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLAddressManager;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.modules.adapters.GLAddCaiAdapter;
import cn.caimatou.canting.threadpool.GLFuture;
import cn.caimatou.canting.threadpool.GLFutureListener;
import cn.caimatou.canting.threadpool.GLThreadPool;
import cn.caimatou.canting.utils.GLProgressDialogUtil;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLTextCheckUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;
import cn.caimatou.canting.utils.http.GLApiHandler;
import cn.caimatou.canting.utils.http.GLHttpRequestHelper;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Description：下订单界面
 * <br/><br/>Created by Fausgoal on 15/8/31.
 * <br/><br/>
 */
public class GLCommitOrderActivity extends GLParentActivity {

    private View mView = null;

    private Button btnSingle = null;
    private LinearLayout llDeliveryTime = null;
    private TextView tvDeliveryTime = null;
    private LinearLayout llDeliveryAddress = null;
    private TextView tvDeliveryAddress = null;
    private EditText etNote = null;

    private List<GLCaipinModel> mCaipinModels = null;
    /**
     * 选择的配置地址
     */
    private GLDeliveryAddressModel mDeliveryAddressModel = null;
    private GLDeliveryTimeModel mDeliveryTimeModel = null;

    private long mProviderId;

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mCaipinModels = (List<GLCaipinModel>) bundle.get(GLConst.INTENT_PARAM);
            mProviderId = bundle.getLong(GLConst.INTENT_PARAM1);
        }
    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.activity_commit_order_layout, null, false);
        ScrollView svView = findView(mView, R.id.svView);
        svView.smoothScrollTo(GLConst.NONE, GLConst.NONE);

        btnSingle = findView(mView, R.id.btnSingle);

        llDeliveryTime = findView(mView, R.id.llDeliveryTime);
        tvDeliveryTime = findView(mView, R.id.tvDeliveryTime);
        llDeliveryAddress = findView(mView, R.id.llDeliveryAddress);
        tvDeliveryAddress = findView(mView, R.id.tvDeliveryAddress);
        etNote = findView(mView, R.id.etNote);
        ListView lvList = findView(mView, R.id.lvList);

        GLAddCaiAdapter adapter = new GLAddCaiAdapter(mContext, mCaipinModels, GLAddCaiAdapter.TYPE_SHOW_CAIPIN, null);
        lvList.setAdapter(adapter);

        fetchDefaultAddress();

        mDeliveryTimeModel = new GLDeliveryTimeModel();
        mDeliveryTimeModel.setId(2);
        mDeliveryTimeModel.setTime(GLResourcesUtil.getString(R.string.next_day_am));

        tvDeliveryTime.setText(mDeliveryTimeModel.getTime());
    }

    private void fetchDefaultAddress() {
        mApp.getThreadPool().submit(new GLThreadPool.Job<GLDeliveryAddressModel>() {
            @Override
            public GLDeliveryAddressModel run() {
                return GLAddressManager.getDefaultAddress(mContext);
            }
        }, new GLFutureListener<GLDeliveryAddressModel>() {
            @Override
            public void onFutureDone(GLFuture<GLDeliveryAddressModel> future) {
                mDeliveryAddressModel = future.get();
                setDeliveryAddress(mDeliveryAddressModel);
            }
        });
    }

    private void setDeliveryAddress(GLDeliveryAddressModel deliveryAddress) {
        if (null != deliveryAddress) {
            String address = deliveryAddress.getAddr() + deliveryAddress.getDetailAddr();
            tvDeliveryAddress.setText(address);
        }
    }

    @Override
    protected void setListener() {
        GLViewClickUtil.setNoFastClickListener(llDeliveryTime, this);
        GLViewClickUtil.setNoFastClickListener(llDeliveryAddress, this);
        GLViewClickUtil.setNoFastClickListener(btnSingle, this);
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.provider_name));
    }

    @Override
    public void onNoFastClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.llDeliveryTime:
                intent = new Intent(mContext, GLDialogActivity.class);
                intent.putExtra(GLConst.INTENT_PARAM, GLConst.DIALOG_DELIVERY_TIME);
                intent.putExtra(GLConst.INTENT_PARAM1, tvDeliveryTime.getText());
                startActivityForResult(intent, GLConst.DIALOG_DELIVERY_TIME);
                break;
            case R.id.llDeliveryAddress:
                intent = new Intent(mContext, GLDeliveryAddressActivity.class);
                intent.putExtra(GLConst.INTENT_PARAM, mDeliveryAddressModel);
                startActivityForResult(intent, GLConst.REQUEST_DELIVERY_ADDRESS);
                break;
            case R.id.btnSingle:
                GLTextCheckUtil.GLCheckResult result = GLTextCheckUtil.checkCommitOrder(tvDeliveryTime.getText(), tvDeliveryAddress.getText());
                if (!result.isSuccess()) {
                    GLCommonManager.makeText(mContext, result.mFailMsg);
                } else {
                    submit();
                }
                break;
        }
    }

    private void submit() {
        GLProgressDialogUtil.showProgress(mContext);
        GLHttpRequestHelper.orderSubmit(mContext, mProviderId, mDeliveryAddressModel.getId(),
                mDeliveryTimeModel.getId(), mCaipinModels, mDeliveryTimeModel.getTime(), etNote.getText().toString(), new GLApiHandler(mContext) {
                    @Override
                    public void onSuccess(GLBaseInfo baseInfo) {
                        super.onSuccess(baseInfo);
                        GLProgressDialogUtil.dismissProgress(mContext);
                        GLCommonManager.makeText(mContext, "下单成功");
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
            case GLConst.DIALOG_DELIVERY_TIME: // 选择配送时间
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    mDeliveryTimeModel = (GLDeliveryTimeModel) bundle.get(GLConst.INTENT_PARAM);
                    if (null != mDeliveryTimeModel) {
                        tvDeliveryTime.setText(mDeliveryTimeModel.getTime());
                    }
                }
                break;
            case GLConst.REQUEST_DELIVERY_ADDRESS: // 配送地址
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    mDeliveryAddressModel = (GLDeliveryAddressModel) bundle.get(GLConst.INTENT_PARAM);
                    setDeliveryAddress(mDeliveryAddressModel);
                }
                break;
        }
    }

}
