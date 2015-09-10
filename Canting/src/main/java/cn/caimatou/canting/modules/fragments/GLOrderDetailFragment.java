package cn.caimatou.canting.modules.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentFragment;
import cn.caimatou.canting.bean.Company;
import cn.caimatou.canting.bean.GLCaipinModel;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLCustomBroadcast;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.modules.activites.GLDialogActivity;
import cn.caimatou.canting.modules.adapters.GLOrderDetailAdapter;
import cn.caimatou.canting.utils.GLDateUtil;
import cn.caimatou.canting.utils.GLProgressDialogUtil;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLStringUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;
import cn.caimatou.canting.widget.GLNoScrollListView;

/**
 * Description：订单详情界面
 * <br/><br/>Created by Fausgoal on 15/8/30.
 * <br/><br/>
 */
public class GLOrderDetailFragment extends GLParentFragment {

    private View mView = null;

    private Company mShopInfo = null;

    private TextView tvOrderCancel = null;
    private View viewline1 = null;
    private TextView tvOrderRemindersAbout = null;
    private View viewline2 = null;
    private TextView tvOrderConfirmReceipt = null;
    private TextView tvOrderdelete = null;

    private ImageView ivBgGray = null;
    private ImageView ivBgGreen = null;

    private ImageView ivIconFirst = null;
    private ImageView ivIconSecond = null;
    private ImageView ivIconThird = null;
    private ImageView ivIconLast = null;

    private TextView tvFirst = null;
    private TextView tvSecond = null;
    private TextView tvThird = null;
    private TextView tvLast = null;

    private TextView tvTimeFirst = null;
    private TextView tvTimeSecond = null;
    private TextView tvTimeThird = null;
    private TextView tvTimeLast = null;

    private LinearLayout llReasonForCancellation = null;
    private TextView tvReasonForCancellation = null;

    private TextView tvProviderName = null;
    private TextView tvOrderNumber = null;
    private TextView tvOrderTime = null;

    private GLNoScrollListView lvList = null;
    private GLOrderDetailAdapter mAdapter = null;

    private TextView tvOrderlTotalCount = null;
    private TextView tvCantingName = null;
    private TextView tvMainDish = null;
    private TextView tvCantingPhone = null;
    private TextView tvShippingAddress = null;

    private int mStateIconWidth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_order_detail_layout, container, false);
        return mView;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (null != bundle) {
            mShopInfo = (Company) bundle.getSerializable(GLConst.INTENT_PARAM);
        }

        // 30 * 30 px - 48px
        mStateIconWidth = (mDeviceSizePoint.x - 60 - 48) / 3;
    }

    @Override
    protected void initView() {
        tvOrderCancel = findView(mView, R.id.tvOrderCancel);
        viewline1 = findView(mView, R.id.viewline1);
        tvOrderRemindersAbout = findView(mView, R.id.tvOrderRemindersAbout);
        viewline2 = findView(mView, R.id.viewline2);
        tvOrderConfirmReceipt = findView(mView, R.id.tvOrderConfirmReceipt);
        tvOrderdelete = findView(mView, R.id.tvOrderdelete);

        ScrollView svView = findView(mView, R.id.svView);
        svView.smoothScrollTo(GLConst.NONE, GLConst.NONE);

        ivBgGray = findView(mView, R.id.ivBgGray);
        ivBgGreen = findView(mView, R.id.ivBgGreen);

        ivIconFirst = findView(mView, R.id.ivIconFirst);
        ivIconSecond = findView(mView, R.id.ivIconSecond);
        ivIconThird = findView(mView, R.id.ivIconThird);
        ivIconLast = findView(mView, R.id.ivIconLast);

        tvFirst = findView(mView, R.id.tvFirst);
        tvSecond = findView(mView, R.id.tvSecond);
        tvThird = findView(mView, R.id.tvThird);
        tvLast = findView(mView, R.id.tvLast);

        tvTimeFirst = findView(mView, R.id.tvTimeFirst);
        tvTimeSecond = findView(mView, R.id.tvTimeSecond);
        tvTimeThird = findView(mView, R.id.tvTimeThird);
        tvTimeLast = findView(mView, R.id.tvTimeLast);

        llReasonForCancellation = findView(mView, R.id.llReasonForCancellation);
        tvReasonForCancellation = findView(mView, R.id.tvReasonForCancellation);

        tvProviderName = findView(mView, R.id.tvProviderName);
        tvOrderNumber = findView(mView, R.id.tvOrderNumber);
        tvOrderTime = findView(mView, R.id.tvOrderTime);

        lvList = findView(mView, R.id.lvList);

        tvOrderlTotalCount = findView(mView, R.id.tvOrderlTotalCount);
        tvCantingName = findView(mView, R.id.tvCantingName);
        tvMainDish = findView(mView, R.id.tvMainDish);
        tvCantingPhone = findView(mView, R.id.tvCantingPhone);
        tvShippingAddress = findView(mView, R.id.tvShippingAddress);

        mAdapter = new GLOrderDetailAdapter(mContext, null, GLOrderDetailAdapter.TYPE_SHOW_CAIPIN, null);
        lvList.setAdapter(mAdapter);

        // 供应商名
        String text = GLResourcesUtil.getString(R.string.order_provider_name);
        text = String.format(text, mShopInfo.getName());
        tvProviderName.setText(text);
        // 订单号
        text = GLResourcesUtil.getString(R.string.order_number);
        text = String.format(text, "20150905111111");
        tvOrderNumber.setText(text);
        // 下单时间
        text = GLDateUtil.format(GLDateUtil.YMDHM_FORMAT, System.currentTimeMillis());
        tvOrderTime.setText(text);

        // 餐厅名
        tvCantingName.setText("香格里拉大酒店");
        tvMainDish.setText("张三李四");
        tvCantingPhone.setText("0751-88889999");
        tvShippingAddress.setText("杭州市西湖区文一西路");

        List<GLCaipinModel> caipinModels = new ArrayList<>();
        GLCaipinModel caipinModel = new GLCaipinModel();
        caipinModel.setNum(1);
        caipinModel.setName("青菜1");
        caipinModel.setUnit("斤");
        caipinModels.add(caipinModel);

        caipinModel = new GLCaipinModel();
        caipinModel.setNum(1);
        caipinModel.setName("青菜2");
        caipinModel.setUnit("斤");
        caipinModel.setMemo("要新鲜的要新鲜的要新鲜的要新鲜的要新鲜的要新鲜的要新鲜的要新鲜的要新鲜的");
        caipinModels.add(caipinModel);

        caipinModel = new GLCaipinModel();
        caipinModel.setNum(1);
        caipinModel.setName("青菜3");
        caipinModel.setUnit("斤");
        caipinModels.add(caipinModel);

        caipinModel = new GLCaipinModel();
        caipinModel.setNum(10);
        caipinModel.setName("青菜4");
        caipinModel.setUnit("包");
        caipinModels.add(caipinModel);

        caipinModel = new GLCaipinModel();
        caipinModel.setNum(8);
        caipinModel.setName("青菜5");
        caipinModel.setUnit("包");
        caipinModels.add(caipinModel);

        caipinModel = new GLCaipinModel();
        caipinModel.setNum(5);
        caipinModel.setName("青菜6");
        caipinModel.setUnit("代");
        caipinModels.add(caipinModel);

        caipinModel = new GLCaipinModel();
        caipinModel.setNum(0);
        caipinModel.setName("青菜6");
        caipinModel.setUnit("代");
        caipinModels.add(caipinModel);

        caipinModel = new GLCaipinModel();
        caipinModel.setNum(99);
        caipinModel.setName("青菜7");
        caipinModel.setUnit("代");
        caipinModels.add(caipinModel);

        caipinModel = new GLCaipinModel();
        caipinModel.setNum(15);
        caipinModel.setName("青菜8");
        caipinModel.setUnit("代");
        caipinModels.add(caipinModel);

        caipinModel = new GLCaipinModel();
        caipinModel.setNum(2);
        caipinModel.setName("青菜9");
        caipinModel.setUnit("代");
        caipinModels.add(caipinModel);

        caipinModel = new GLCaipinModel();
        caipinModel.setNum(11);
        caipinModel.setName("青菜10");
        caipinModel.setUnit("代");
        caipinModels.add(caipinModel);

        mAdapter.append(caipinModels);
        // 合计数量
        setTotalCount(mAdapter.getCount());

        setStateIcon();

        setOrderState(4);
    }

    private void setStateIcon() {
        int thirdX = mStateIconWidth * 2;
        ivIconSecond.setX(mStateIconWidth);
        ivIconThird.setX(thirdX);

        tvSecond.setX(mStateIconWidth);
        tvThird.setX(thirdX);
        tvTimeSecond.setX(mStateIconWidth);
        tvTimeThird.setX(thirdX);
    }

    private void setBgGreen(int width, int position) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivBgGreen.getLayoutParams();
        params.width = (width * position) + (48 / 2);
        ivBgGreen.setLayoutParams(params);
    }

    private void setOrderState(int state) {
        switch (state) {
            case 0: // 已报菜
                ivIconFirst.setImageResource(R.mipmap.icon_baocai_on);
                setBgGreen(mStateIconWidth / 2, 1);
                break;
            case 1: // 已接单
                ivIconFirst.setImageResource(R.mipmap.icon_baocai_on);
                ivIconSecond.setImageResource(R.mipmap.icon_baocai_on);
                setBgGreen(mStateIconWidth / 2, 3);
                break;
            case 2: // 已送达
                ivIconFirst.setImageResource(R.mipmap.icon_baocai_on);
                ivIconSecond.setImageResource(R.mipmap.icon_baocai_on);
                ivIconThird.setImageResource(R.mipmap.icon_baocai_on);
                setBgGreen(mStateIconWidth / 2, 5);
                break;
            case 3: // 已收货
                ivIconFirst.setImageResource(R.mipmap.icon_baocai_on);
                ivIconSecond.setImageResource(R.mipmap.icon_baocai_on);
                ivIconThird.setImageResource(R.mipmap.icon_baocai_on);
                ivIconLast.setImageResource(R.mipmap.icon_baocai_on);
                setBgGreen(mStateIconWidth, 5);
                break;
            case 4: // 已取消
                ivIconFirst.setImageResource(R.mipmap.icon_baocai_on);
                ivIconSecond.setImageResource(R.mipmap.icon_baocai_on);
                ivIconThird.setImageResource(R.mipmap.icon_order_cancel_on);
                ivIconLast.setImageResource(R.mipmap.icon_cancel_on);

                tvThird.setText(GLResourcesUtil.getString(R.string.state_application_withdrawn));
                tvLast.setText(GLResourcesUtil.getString(R.string.is_canceled));

                setBgGreen(mStateIconWidth, 5);
                break;
        }
    }

    private void setCancelMsg(String strMsg) {
        llReasonForCancellation.setVisibility(View.VISIBLE);
        tvReasonForCancellation.setText(strMsg);
    }

    private void setTotalCount(int count) {
        String strTotalCount = GLResourcesUtil.getString(R.string.order_total_count);
        strTotalCount = String.format(strTotalCount, count);
        // 数量是红色的，就html标签加的，要转换
        CharSequence text = Html.fromHtml(strTotalCount);
        tvOrderlTotalCount.setText(text);
    }

    @Override
    protected void setListener() {
        GLViewClickUtil.setNoFastClickListener(tvOrderCancel, this);
        GLViewClickUtil.setNoFastClickListener(tvOrderRemindersAbout, this);
        GLViewClickUtil.setNoFastClickListener(tvOrderConfirmReceipt, this);
        GLViewClickUtil.setNoFastClickListener(tvOrderdelete, this);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.tvOrderCancel:// 取消
                Intent intent = new Intent(mContext, GLDialogActivity.class);
                intent.putExtra(GLConst.INTENT_PARAM, GLConst.DIALOG_ORDER_CANCELED);
                startActivity(intent);
                break;
            case R.id.tvOrderRemindersAbout:// 催一下
                GLCommonManager.makeText(mContext, "催成功");
                break;
            case R.id.tvOrderConfirmReceipt:// 确认收货
                GLCommonManager.makeText(mContext, "确认收货");
                break;
            case R.id.tvOrderdelete:// 删除报菜单
                GLCommonManager.makeText(mContext, "删除报菜单");
                break;
        }
    }

    @Override
    protected IntentFilter getStickyIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(GLCustomBroadcast.ON_ORDER_CANCELED_ACTION);
        return filter;
    }

    @Override
    protected void onStickyNotify(Context context, Intent intent) {
        String action = intent.getAction();
        if (GLCustomBroadcast.ON_ORDER_CANCELED_ACTION.equals(action)) {
            // NOTICE 撤消报菜单
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                String strMsg = bundle.getString(GLConst.INTENT_PARAM);
                if (GLStringUtil.isNotEmpty(strMsg)) {
                    setCancelMsg(strMsg);

                    // NOTICE 发送撤消原因到服务器
                    GLProgressDialogUtil.showProgress(mContext, GLResourcesUtil.getString(R.string.canceling));
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            GLProgressDialogUtil.dismissProgress(mContext);
                        }
                    }, 2000);
                } else {
                    llReasonForCancellation.setVisibility(View.GONE);
                }
            }
        }
    }
}
