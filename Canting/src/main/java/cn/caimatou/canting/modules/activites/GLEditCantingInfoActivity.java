package cn.caimatou.canting.modules.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.GLRestaurant;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLCantingManager;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.manager.GLUserManager;
import cn.caimatou.canting.modules.logic.GLLoginLogic;
import cn.caimatou.canting.modules.logic.GLProvinceLogic;
import cn.caimatou.canting.response.GLRestaurantResp;
import cn.caimatou.canting.utils.GLProgressDialogUtil;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLStringUtil;
import cn.caimatou.canting.utils.GLTextCheckUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;
import cn.caimatou.canting.utils.http.GLApiHandler;
import cn.caimatou.canting.utils.http.GLHttpRequestHelper;
import cn.caimatou.canting.widget.GLNavigationBar;
import cn.kankan.wheel.widget.OnWheelChangedListener;
import cn.kankan.wheel.widget.WheelView;
import cn.kankan.wheel.widget.adapters.ArrayWheelAdapter;

/**
 * Description：餐厅信息
 * <br/><br/>Created by Fausgoal on 15/8/29.
 * <br/><br/>
 */
public class GLEditCantingInfoActivity extends GLParentActivity implements OnWheelChangedListener {
    private static final String TAG = "GLEditCantingInfoActivity";
    /**
     * 注册时进入的
     */
    public static final int ENTERY_TYPE_REGISTE = 1;
    /**
     * 编辑餐厅信息
     */
    public static final int ENTERY_TYPE_EDIT = 2;

    private View mView = null;

    private LinearLayout llCantingName = null;
    private TextView tvCantingName = null;
    private LinearLayout llCantingCity = null;
    private TextView tvCantingCity = null;
    private LinearLayout llCantingAddress = null;
    private TextView tvCantingAddress = null;
    private LinearLayout llCantingContact = null;
    private TextView tvCantingContact = null;
    private LinearLayout llCantingPhone = null;
    private TextView tvCantingPhone = null;
    private LinearLayout llCantingQuarters = null;
    private TextView tvCantingQuarters = null;
    private Button btnCompleter = null;

    private LinearLayout llWheelView = null;
    private WheelView wvProvince = null;
    private WheelView wvCity = null;
    private WheelView wvDistrict = null;

    private String mMobilePahone = null;
    private int mEnteryType = GLConst.NEGATIVE;

    private GLLoginLogic mLoginLogic = null;

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mMobilePahone = bundle.getString(GLConst.INTENT_PARAM);
            mEnteryType = bundle.getInt(GLConst.INTENT_PARAM1, GLConst.NEGATIVE);
        }

        mLoginLogic = new GLLoginLogic(mContext);
    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.activity_edit_cantinginfo_layout, null, false);
        llCantingName = findView(mView, R.id.llCantingName);
        tvCantingName = findView(mView, R.id.tvCantingName);
        llCantingCity = findView(mView, R.id.llCantingCity);
        tvCantingCity = findView(mView, R.id.tvCantingCity);
        llCantingAddress = findView(mView, R.id.llCantingAddress);
        tvCantingAddress = findView(mView, R.id.tvCantingAddress);
        llCantingContact = findView(mView, R.id.llCantingContact);
        tvCantingContact = findView(mView, R.id.tvCantingContact);
        llCantingPhone = findView(mView, R.id.llCantingPhone);
        tvCantingPhone = findView(mView, R.id.tvCantingPhone);
        llCantingQuarters = findView(mView, R.id.llCantingQuarters);
        tvCantingQuarters = findView(mView, R.id.tvCantingQuarters);
        btnCompleter = findView(mView, R.id.btnCompleter);

        llWheelView = findView(mView, R.id.llWheelView);
        wvProvince = findView(mView, R.id.wvProvince);
        wvCity = findView(mView, R.id.wvCity);
        wvDistrict = findView(mView, R.id.wvDistrict);

        // 默认是注册的手机号
        tvCantingPhone.setText(mMobilePahone);

        setUpData();
    }

    private void setUpData() {
        GLProvinceLogic.getIns().initProvinceDatas(mContext, true, null, null, null);
        wvProvince.setViewAdapter(new ArrayWheelAdapter<>(mContext, GLProvinceLogic.getIns().mProvinceDatas));
        wvProvince.setVisibleItems(7);
        wvCity.setVisibleItems(7);
        wvDistrict.setVisibleItems(7);

        int index = GLConst.NONE;
        wvProvince.setCurrentItem(index);

        updateCities();
        updateAreas();
    }

    private void updateCities() {
        int pCurrent = wvProvince.getCurrentItem();
        GLProvinceLogic.getIns().mCurrentProviceName = GLProvinceLogic.getIns().mProvinceDatas[pCurrent];

        String[] cities = GLProvinceLogic.getIns().mCitisDatasMap.get(GLProvinceLogic.getIns().mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        wvCity.setViewAdapter(new ArrayWheelAdapter<>(this, cities));

        int index = GLConst.NONE;
        wvCity.setCurrentItem(index);

        updateAreas();
    }

    private void updateAreas() {
        int pCurrent = wvCity.getCurrentItem();
        GLProvinceLogic.getIns().mCurrentCityName = GLProvinceLogic.getIns().mCitisDatasMap.get(GLProvinceLogic.getIns().mCurrentProviceName)[pCurrent];

        String[] areas = GLProvinceLogic.getIns().mDistrictDatasMap.get(GLProvinceLogic.getIns().mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        wvDistrict.setViewAdapter(new ArrayWheelAdapter<>(this, areas));

        int index = GLConst.NONE;
        wvDistrict.setCurrentItem(index);

        GLProvinceLogic.getIns().mCurrentDistrictName = GLProvinceLogic.getIns().mDistrictDatasMap.get(GLProvinceLogic.getIns().mCurrentCityName)[index];
        GLProvinceLogic.getIns().mCurrentZipCode = GLProvinceLogic.getIns().mZipcodeDatasMap.get(GLProvinceLogic.getIns().mCurrentDistrictName);

        setCityAndDis();
    }

    @Override
    protected void setListener() {
        GLViewClickUtil.setNoFastClickListener(llCantingName, this);
        GLViewClickUtil.setNoFastClickListener(llCantingCity, this);
        GLViewClickUtil.setNoFastClickListener(llCantingAddress, this);
        GLViewClickUtil.setNoFastClickListener(llCantingContact, this);
        GLViewClickUtil.setNoFastClickListener(llCantingPhone, this);
        GLViewClickUtil.setNoFastClickListener(llCantingQuarters, this);
        GLViewClickUtil.setNoFastClickListener(btnCompleter, this);

        wvProvince.addChangingListener(this);
        wvCity.addChangingListener(this);
        wvDistrict.addChangingListener(this);
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.edit_canting_info));
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == wvProvince) {
            updateCities();
        } else if (wheel == wvCity) {
            updateAreas();
        } else if (wheel == wvDistrict) {
            GLProvinceLogic.getIns().mCurrentDistrictName = GLProvinceLogic.getIns().mDistrictDatasMap.get(GLProvinceLogic.getIns().mCurrentCityName)[newValue];
            GLProvinceLogic.getIns().mCurrentZipCode = GLProvinceLogic.getIns().mZipcodeDatasMap.get(GLProvinceLogic.getIns().mCurrentDistrictName);
        }
        setCityAndDis();
    }

    private void setCityAndDis() {
        String text = GLProvinceLogic.getIns().mCurrentProviceName
                + GLProvinceLogic.getIns().mCurrentCityName
                + GLProvinceLogic.getIns().mCurrentDistrictName;
        tvCantingCity.setText(text);
    }

    @Override
    public void onNoFastClick(View v) {
        boolean isEdit = false;
        int editType = -1;
        if (llWheelView.getVisibility() == View.VISIBLE) {
            llWheelView.setVisibility(View.GONE);
        }
        CharSequence content = "";
        switch (v.getId()) {
            case R.id.llCantingName:
                isEdit = true;
                editType = GLLoginLogic.EDIT_CANTING_NAME;
                content = tvCantingName.getText();
                break;
            case R.id.llCantingCity:
                isEdit = false;
                if (llWheelView.getVisibility() == View.VISIBLE) {
                    llWheelView.setVisibility(View.GONE);
                } else {
                    llWheelView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.llCantingAddress:
                isEdit = true;
                editType = GLLoginLogic.EDIT_CANTING_ADDRESS;
                content = tvCantingAddress.getText();
                break;
            case R.id.llCantingContact:
                isEdit = true;
                editType = GLLoginLogic.EDIT_CANTING_CONTACT;
                content = tvCantingContact.getText();
                break;
            case R.id.llCantingPhone:
                isEdit = true;
                editType = GLLoginLogic.EDIT_CANTING_PHONE;
                content = tvCantingPhone.getText();
                break;
            case R.id.llCantingQuarters:
                isEdit = false;
                Intent intent = new Intent(mContext, GLStaffChoosePostActivity.class);
                int type = GLConst.NEGATIVE;
                // 标识是注册时进入的
                if (mEnteryType == ENTERY_TYPE_REGISTE) {
                    type = GLStaffChoosePostActivity.ENTERY_TYPE_REGIST;
                }

                intent.putExtra(GLConst.INTENT_PARAM, type);
                GLViewManager.getIns().showActivity(mContext, intent, GLLoginLogic.REQUEST_POST_CODE);
                break;
            case R.id.btnCompleter:
                isEdit = false;
                llWheelView.setVisibility(View.GONE);
                GLTextCheckUtil.GLCheckResult result = GLTextCheckUtil.checkCantingInfo(tvCantingName.getText(),
                        tvCantingCity.getText(), tvCantingAddress.getText(), tvCantingContact.getText(),
                        tvCantingPhone.getText(), tvCantingQuarters.getText());
                if (!result.isSuccess()) {
                    GLCommonManager.makeText(mContext, result.mFailMsg);
                } else {
                    hideInput(btnCompleter);
                    completer();
                }
                break;
        }

        if (isEdit) {
            Intent intent = new Intent(mContext, GLEditInfoActivity.class);
            intent.putExtra(GLConst.INTENT_PARAM, editType);
            intent.putExtra(GLConst.INTENT_PARAM1, content);
            GLViewManager.getIns().showActivity(mContext, intent, GLLoginLogic.REQUEST_EDIT_CANTING_INFO);
        }
    }

    private void completer() {
        // NOTICE 调用API提交注册
        GLProgressDialogUtil.showProgress(mContext);

        String name = tvCantingName.getText().toString().trim();
        String link_man = tvCantingContact.getText().toString().trim();
        String tel = tvCantingPhone.getText().toString().trim();
        String addr = tvCantingCity.getText().toString().trim();
        String detail_addr = tvCantingAddress.getText().toString().trim();
        String memo = tvCantingQuarters.getText().toString().trim();

        GLHttpRequestHelper.restaurant(mContext, name, link_man, tel, addr, detail_addr, memo, new GLApiHandler(mContext) {
            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                // 保存到本地
                GLRestaurantResp restaurantResp = (GLRestaurantResp) baseInfo;
                GLRestaurant restaurant = restaurantResp.getResult();

                // 保存到数据库
                GLCantingManager.saveCantingInfo(mContext, restaurant);

                GLProgressDialogUtil.showProgress(mContext, GLResourcesUtil.getString(R.string.start_login));

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 执行自动登录
                        GLUserManager.onLogin(mContext, new GLLoginLogic.LoginHandler(mContext, true));
                    }
                }, 500);
            }

            @Override
            public void onFailure(GLBaseInfo baseInfo) {
                super.onFailure(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);
                if (null != baseInfo && GLStringUtil.isNotEmpty(baseInfo.getMessage())) {
                    GLCommonManager.makeText(mContext, baseInfo.getMessage());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GLLoginLogic.REQUEST_EDIT_CANTING_INFO) {
            if (resultCode == Activity.RESULT_OK) {
                int editType = data.getExtras().getInt(GLConst.INTENT_PARAM1);
                String content = data.getExtras().getString(GLConst.INTENT_PARAM);
                switch (editType) {
                    case GLLoginLogic.EDIT_CANTING_NAME:
                        tvCantingName.setText(content);
                        break;
                    case GLLoginLogic.EDIT_CANTING_CITY:
                        tvCantingCity.setText(content);
                        break;
                    case GLLoginLogic.EDIT_CANTING_ADDRESS:
                        tvCantingAddress.setText(content);
                        break;
                    case GLLoginLogic.EDIT_CANTING_CONTACT:
                        tvCantingContact.setText(content);
                        break;
                    case GLLoginLogic.EDIT_CANTING_PHONE:
                        tvCantingPhone.setText(content);
                        break;
                    case GLLoginLogic.EDIT_CANTING_QUARTERS:
                        tvCantingQuarters.setText(content);
                        break;
                    default:
                        break;
                }
            }
        } else if (requestCode == GLLoginLogic.REQUEST_POST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // 选择岗位
                Bundle bundle = data.getExtras();
                if (null != bundle) {
                    // NOTICE 要根据返回的结果修改
                    String quartersName = bundle.getString(GLConst.INTENT_PARAM);
                    tvCantingQuarters.setText(quartersName);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideInput(btnCompleter);
    }

    private void onBack() {
        GLViewManager.getIns().pop(this);
    }

    @Override
    public boolean onItemSelectedListener(int viewId) {
        if (viewId == R.id.ivNavLeft) {
            if (llWheelView.getVisibility() == View.VISIBLE) {
                llWheelView.setVisibility(View.GONE);
            } else {
                onBack();
            }
            return false;
        }
        return super.onItemSelectedListener(viewId);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (llWheelView.getVisibility() == View.VISIBLE) {
                llWheelView.setVisibility(View.GONE);
            } else {
                onBack();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
