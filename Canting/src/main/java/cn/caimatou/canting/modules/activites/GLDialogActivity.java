package cn.caimatou.canting.modules.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.bean.GLCaipinUnitModel;
import cn.caimatou.canting.bean.GLDeliveryTimeModel;
import cn.caimatou.canting.callback.IGLOnListItemClickListener;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLCustomBroadcast;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.modules.adapters.GLDeliveryTimeAdapter;
import cn.caimatou.canting.utils.GLNumberUtils;
import cn.caimatou.canting.utils.GLPixelsUtil;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLStringUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;
import cn.caimatou.canting.widget.GLCaipinUnitView;

/**
 * Description：Dialog 弹框界面
 * <br/><br/>Created by Fausgoal on 15/9/1.
 * <br/><br/>
 */
public class GLDialogActivity extends Activity implements GLViewClickUtil.NoFastClickListener {
    private int mDialogType = GLConst.NEGATIVE;

    private Context mContext = null;
    protected Point mPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mDialogType = bundle.getInt(GLConst.INTENT_PARAM, GLConst.NEGATIVE);
        }

        mContext = this;
        mPoint = GLPixelsUtil.getDeviceSize(mContext);

        initDialog();
    }

    private void initDialog() {
        switch (mDialogType) {
            case GLConst.DIALOG_DELIVERY_TIME:
                initDeliveryTime();
                break;
            case GLConst.DIALOG_UNIT:
                initCaipinUnit();
                break;
            case GLConst.DIALOG_ORDER_CANCELED:
                initOrderCanceled();
                break;
        }
    }

    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    private void setWindowAttributes() {
        int height = (int) (mPoint.y * 0.6f);
        setWindowAttributes(height);
    }

    private void setWindowAttributes(int height) {
        int width = mPoint.x; // 屏幕的宽度

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = height;
        params.width = width;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    }

    private void setWindowAttributesCenter(int height) {
        int width = (int) (mPoint.x * 0.9f); // 屏幕的宽度

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = height;
        params.width = width;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.CENTER);
    }

    private GLDeliveryTimeAdapter mDeliveryTimeAdapter = null;

    private void initDeliveryTime() {
        setContentView(R.layout.activity_delivery_time_layout);

        setWindowAttributes(WindowManager.LayoutParams.WRAP_CONTENT);

        Bundle bundle = getIntent().getExtras();
        String selectedDeliveryTime = bundle.getString(GLConst.INTENT_PARAM1);

        List<GLDeliveryTimeModel> deliveryTimeModels = new ArrayList<>();
        GLDeliveryTimeModel deliveryTimeModel = new GLDeliveryTimeModel();
        deliveryTimeModel.setId(4);
        deliveryTimeModel.setTime(GLResourcesUtil.getString(R.string.today_am));
        deliveryTimeModels.add(deliveryTimeModel);

        deliveryTimeModel = new GLDeliveryTimeModel();
        deliveryTimeModel.setId(5);
        deliveryTimeModel.setTime(GLResourcesUtil.getString(R.string.today_pm));
        deliveryTimeModels.add(deliveryTimeModel);

        deliveryTimeModel = new GLDeliveryTimeModel();
        deliveryTimeModel.setId(2);
        deliveryTimeModel.setTime(GLResourcesUtil.getString(R.string.next_day_am));
        deliveryTimeModels.add(deliveryTimeModel);

        deliveryTimeModel = new GLDeliveryTimeModel();
        deliveryTimeModel.setId(3);
        deliveryTimeModel.setTime(GLResourcesUtil.getString(R.string.next_day_pm));
        deliveryTimeModels.add(deliveryTimeModel);

        deliveryTimeModel = new GLDeliveryTimeModel();
        deliveryTimeModel.setId(1);
        deliveryTimeModel.setTime(GLResourcesUtil.getString(R.string.immediately_served));
        deliveryTimeModels.add(deliveryTimeModel);

        ListView lvList = findView(R.id.lvList);
        mDeliveryTimeAdapter = new GLDeliveryTimeAdapter(mContext, deliveryTimeModels, selectedDeliveryTime, new IGLOnListItemClickListener() {
            @Override
            public void onClickItem(int position, View v) {
                GLDeliveryTimeModel deliveryTimeModel = mDeliveryTimeAdapter.getItem(position);
                Intent data = new Intent();
                data.putExtra(GLConst.INTENT_PARAM, deliveryTimeModel);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
        lvList.setAdapter(mDeliveryTimeAdapter);
    }

    private void initCaipinUnit() {
        setContentView(R.layout.activity_caipin_unit_layout);

        setWindowAttributes(WindowManager.LayoutParams.WRAP_CONTENT);

        Bundle bundle = getIntent().getExtras();
        final String selectedUnit = bundle.getString(GLConst.INTENT_PARAM1);
        final List<GLCaipinUnitModel> caipinUnitModels = (List<GLCaipinUnitModel>) bundle.get(GLConst.INTENT_PARAM2);

        GLCaipinUnitView llCaipinUnit = findView(R.id.llCaipinUnit);


        llCaipinUnit.setCellData(selectedUnit, caipinUnitModels, new GLCaipinUnitView.OnItemClickListener() {
            @Override
            public void onCellItemClick(GLCaipinUnitModel caipinUnitModel) {
                Intent data = new Intent();
                data.putExtra(GLConst.INTENT_PARAM, caipinUnitModel);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
    }

    private LinearLayout llInfoIsError = null;
    private RadioButton rbInfoIsErrorCheck = null;
    private TextView tvInfoIsError = null;

    private LinearLayout llDonotWantHave = null;
    private RadioButton rbDonotWantHaveCheck = null;
    private TextView tvDonotWantHave = null;

    private LinearLayout llOther = null;
    private RadioButton rbOther = null;
    private TextView tvOther = null;

    private EditText etOther = null;

    private TextView tvCancel = null;
    private TextView tvConfirm = null;

    private List<RadioButton> mCheckedRadio = null;

    private void initOrderCanceled() {
        setContentView(R.layout.activity_order_canceled_layout);

        setWindowAttributesCenter(WindowManager.LayoutParams.WRAP_CONTENT);

        llInfoIsError = findView(R.id.llInfoIsError);
        rbInfoIsErrorCheck = findView(R.id.rbInfoIsErrorCheck);
        tvInfoIsError = findView(R.id.tvInfoIsError);

        llDonotWantHave = findView(R.id.llDonotWantHave);
        rbDonotWantHaveCheck = findView(R.id.rbDonotWantHaveCheck);
        tvDonotWantHave = findView(R.id.tvDonotWantHave);

        llOther = findView(R.id.llOther);
        rbOther = findView(R.id.rbOther);
        tvOther = findView(R.id.tvOther);

        etOther = findView(R.id.etOther);

        tvCancel = findView(R.id.tvCancel);
        tvConfirm = findView(R.id.tvConfirm);

        llInfoIsError.setTag(0);
        rbInfoIsErrorCheck.setTag(0);
        tvInfoIsError.setTag(0);

        llDonotWantHave.setTag(1);
        rbDonotWantHaveCheck.setTag(1);
        tvDonotWantHave.setTag(1);

        llOther.setTag(2);
        rbOther.setTag(2);
        tvOther.setTag(2);

        rbInfoIsErrorCheck.setChecked(true);
        mCheckedRadio = new ArrayList<>();

        mCheckedRadio.add(rbInfoIsErrorCheck);
        mCheckedRadio.add(rbDonotWantHaveCheck);
        mCheckedRadio.add(rbOther);

        rbOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    etOther.setVisibility(View.VISIBLE);
                } else {
                    etOther.setVisibility(View.GONE);
                }
            }
        });

        GLViewClickUtil.setNoFastClickListener(llInfoIsError, this);
        GLViewClickUtil.setNoFastClickListener(rbInfoIsErrorCheck, this);
        GLViewClickUtil.setNoFastClickListener(tvInfoIsError, this);

        GLViewClickUtil.setNoFastClickListener(llDonotWantHave, this);
        GLViewClickUtil.setNoFastClickListener(rbDonotWantHaveCheck, this);
        GLViewClickUtil.setNoFastClickListener(tvDonotWantHave, this);

        GLViewClickUtil.setNoFastClickListener(llOther, this);
        GLViewClickUtil.setNoFastClickListener(rbOther, this);
        GLViewClickUtil.setNoFastClickListener(tvOther, this);

        GLViewClickUtil.setNoFastClickListener(tvCancel, this);
        GLViewClickUtil.setNoFastClickListener(tvConfirm, this);
    }

    private void setChecked(int position) {
        if (null != mCheckedRadio) {
            for (int i = GLConst.NONE; i < mCheckedRadio.size(); i++) {
                RadioButton rb = mCheckedRadio.get(i);
                if (position == i) {
                    rb.setChecked(true);
                } else {
                    rb.setChecked(false);
                }
            }
        }
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.llInfoIsError: // 信息有误
            case R.id.rbInfoIsErrorCheck:
            case R.id.tvInfoIsError:
            case R.id.llDonotWantHave: // 不想要了
            case R.id.rbDonotWantHaveCheck:
            case R.id.tvDonotWantHave:
            case R.id.llOther: // 其他
            case R.id.rbOther:
            case R.id.tvOther:
                if (null != v.getTag()) {
                    int position = GLNumberUtils.stringToInt(v.getTag().toString());
                    setChecked(position);
                }
                break;
            case R.id.tvCancel:
                finish();
                break;
            case R.id.tvConfirm:
                String strMsg = "";
                if (rbInfoIsErrorCheck.isChecked()) {
                    // 选择的是信息有误
                    strMsg = tvInfoIsError.getText().toString();
                } else if (rbDonotWantHaveCheck.isChecked()) {
                    // 选择的是不想要了
                    strMsg = tvDonotWantHave.getText().toString();
                } else if (rbOther.isChecked()) {
                    // 选择的是其他
                    String otherMsg = etOther.getText().toString().trim();
                    if (GLStringUtil.isNotEmpty(otherMsg)) {
                        strMsg = otherMsg;
                    } else {
                        GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.canceled_other_note));
                        return;
                    }
                }

                Intent intent = new Intent();
                intent.setAction(GLCustomBroadcast.ON_ORDER_CANCELED_ACTION);
                intent.putExtra(GLConst.INTENT_PARAM, strMsg);
                sendBroadcast(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mDialogType != GLConst.DIALOG_ORDER_CANCELED) {
                finish();
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mDeliveryTimeAdapter) {
            mDeliveryTimeAdapter.clear();
            mDeliveryTimeAdapter = null;
        }
    }
}
