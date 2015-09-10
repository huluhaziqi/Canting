package cn.caimatou.canting.modules.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.GLDeliveryAddressModel;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLAddressManager;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.modules.logic.GLProvinceLogic;
import cn.caimatou.canting.response.GLAddressResp;
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
 * Description：添加、编辑配送地址界面
 * <br/><br/>Created by Fausgoal on 15/9/2.
 * <br/><br/>
 */
public class GLEditDeliveryAddressActivity extends GLParentActivity implements OnWheelChangedListener {

    private View mView = null;
    private int mPosition = GLConst.NEGATIVE;
    private GLDeliveryAddressModel mEditAddressModel = null;

    private TextView tvSetDefault = null;
    private TextView tvSave = null;
    private LinearLayout llWheelView = null;
    private WheelView wvProvince = null;
    private WheelView wvCity = null;
    private WheelView wvDistrict = null;

    private LinearLayout llCityAndArea = null;
    private TextView tvCityAndArea = null;
    private EditText etAddress = null;
    private EditText etCantinName = null;
    private EditText etLinkMan = null;
    private EditText etMobilePhone = null;

    private boolean isFirstProvince = true;
    private boolean isFirstCity = true;
    private boolean isFirstDistrict = true;

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mPosition = bundle.getInt(GLConst.INTENT_PARAM1);
            if (mPosition != GLConst.NEGATIVE) {
                // 编辑模式
                mEditAddressModel = (GLDeliveryAddressModel) bundle.get(GLConst.INTENT_PARAM);
            }
        }
    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.activity_edit_adress_layout, null, false);

        ScrollView svView = findView(mView, R.id.svView);
        svView.smoothScrollTo(0, 0);

        tvSetDefault = findView(mView, R.id.tvSetDefault);
        tvSave = findView(mView, R.id.tvSave);
        llWheelView = findView(mView, R.id.llWheelView);
        wvProvince = findView(mView, R.id.wvProvince);
        wvCity = findView(mView, R.id.wvCity);
        wvDistrict = findView(mView, R.id.wvDistrict);

        llCityAndArea = findView(mView, R.id.llCityAndArea);
        tvCityAndArea = findView(mView, R.id.tvCityAndArea);
        etAddress = findView(mView, R.id.etAddress);
        etCantinName = findView(mView, R.id.etCantinName);
        etLinkMan = findView(mView, R.id.etLinkMan);
        etMobilePhone = findView(mView, R.id.etMobilePhone);

        setEditAddressText();
        setUpData();

        showInputByEnter(etAddress);
    }

    private void setEditAddressText() {
        if (null != mEditAddressModel && mPosition != GLConst.NEGATIVE) {
            if (mEditAddressModel.getIsDefault() == 1) {
                tvSetDefault.setVisibility(View.GONE);
            } else {
                tvSetDefault.setVisibility(View.VISIBLE);
            }

            tvCityAndArea.setText(mEditAddressModel.getAddr());
            etAddress.setText(mEditAddressModel.getDetailAddr());
            // 要根据 id 查餐厅名
//            etCantinName.setText(mEditAddressModel.getCompanyId() + "");
            etLinkMan.setText(mEditAddressModel.getLinkMan());
            etMobilePhone.setText(mEditAddressModel.getTel());

//            etCantinName.setSelection(etCantinName.getText().length());
            etAddress.setSelection(etAddress.getText().length());
            etLinkMan.setSelection(etLinkMan.getText().length());
            etMobilePhone.setSelection(etMobilePhone.getText().length());

            GLProvinceLogic.getIns().mCurrentProviceName = mEditAddressModel.getProvince();
            GLProvinceLogic.getIns().mCurrentCityName = mEditAddressModel.getTown();
            GLProvinceLogic.getIns().mCurrentDistrictName = mEditAddressModel.getRegion();
        } else {
            tvSetDefault.setVisibility(View.GONE);
        }
    }

    private void setUpData() {
        String selectedProvince = null;
        String selectedCity = null;
        String selectedDistrict = null;

        boolean isSetFirst = (mPosition == GLConst.NEGATIVE);
        GLProvinceLogic.getIns().initProvinceDatas(mContext, isSetFirst, selectedProvince, selectedCity, selectedDistrict);
        wvProvince.setViewAdapter(new ArrayWheelAdapter<>(mContext, GLProvinceLogic.getIns().mProvinceDatas));
        wvProvince.setVisibleItems(7);
        wvCity.setVisibleItems(7);
        wvDistrict.setVisibleItems(7);

        int index = GLConst.NONE;
        if (!isFirstProvince) {
            index = GLConst.NONE;
        } else {
            if (mPosition != GLConst.NEGATIVE) {
                String selectedProvince1 = mEditAddressModel.getProvince();
                GLProvinceLogic.getIns().mCurrentCityName = selectedProvince1;
                String[] provinces = GLProvinceLogic.getIns().mProvinceDatas;
                for (int i = GLConst.NONE; i < provinces.length; i++) {
                    if (provinces[i].equals(selectedProvince1)) {
                        index = i;
                        break;
                    }
                }
            }
        }
        wvProvince.setCurrentItem(index);
        isFirstProvince = false;
        updateCities();
        updateAreas();
    }

    private void updateCities() {
        if (mPosition == GLConst.NEGATIVE
                || GLStringUtil.isEmpty(GLProvinceLogic.getIns().mCurrentProviceName)
                || !isFirstCity) {
            int pCurrent = wvProvince.getCurrentItem();
            GLProvinceLogic.getIns().mCurrentProviceName = GLProvinceLogic.getIns().mProvinceDatas[pCurrent];
        }

        String[] cities = GLProvinceLogic.getIns().mCitisDatasMap.get(GLProvinceLogic.getIns().mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        wvCity.setViewAdapter(new ArrayWheelAdapter<>(this, cities));

        int index = GLConst.NONE;
        if (!isFirstCity) {
            index = GLConst.NONE;
        } else {
            if (mPosition != GLConst.NEGATIVE) {
                String selectedCity = mEditAddressModel.getTown();
                GLProvinceLogic.getIns().mCurrentCityName = selectedCity;
                for (int i = GLConst.NONE; i < cities.length; i++) {
                    if (cities[i].equals(selectedCity)) {
                        index = i;
                        break;
                    }
                }
            }
        }
        wvCity.setCurrentItem(index);

        isFirstCity = false;
        updateAreas();
    }

    private void updateAreas() {
        if (mPosition == GLConst.NEGATIVE
                || GLStringUtil.isEmpty(GLProvinceLogic.getIns().mCurrentCityName)
                || !isFirstDistrict) {
            int pCurrent = wvCity.getCurrentItem();
            GLProvinceLogic.getIns().mCurrentCityName = GLProvinceLogic.getIns().mCitisDatasMap.get(GLProvinceLogic.getIns().mCurrentProviceName)[pCurrent];
        }

        String[] areas = GLProvinceLogic.getIns().mDistrictDatasMap.get(GLProvinceLogic.getIns().mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        wvDistrict.setViewAdapter(new ArrayWheelAdapter<>(this, areas));

        int index = GLConst.NONE;
        String selectedDistrict = GLProvinceLogic.getIns().mCurrentDistrictName;
        for (int i = GLConst.NONE; i < areas.length; i++) {
            if (areas[i].equals(selectedDistrict)) {
                index = i;
                break;
            }
        }
        wvDistrict.setCurrentItem(index);

        GLProvinceLogic.getIns().mCurrentDistrictName = GLProvinceLogic.getIns().mDistrictDatasMap.get(GLProvinceLogic.getIns().mCurrentCityName)[index];
        GLProvinceLogic.getIns().mCurrentZipCode = GLProvinceLogic.getIns().mZipcodeDatasMap.get(GLProvinceLogic.getIns().mCurrentDistrictName);

        isFirstDistrict = false;
        setCityAndDis();
    }

    @Override
    protected void setListener() {
        GLViewClickUtil.setNoFastClickListener(etAddress, this);
        GLViewClickUtil.setNoFastClickListener(etCantinName, this);
        GLViewClickUtil.setNoFastClickListener(etLinkMan, this);
        GLViewClickUtil.setNoFastClickListener(etMobilePhone, this);
        GLViewClickUtil.setNoFastClickListener(tvSetDefault, this);
        GLViewClickUtil.setNoFastClickListener(tvSave, this);
        GLViewClickUtil.setNoFastClickListener(llCityAndArea, this);

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
        if (mPosition != GLConst.NEGATIVE) {
            navBar.setNavTitle(GLResourcesUtil.getString(R.string.edit_address));
            navBar.setRightBtn(GLResourcesUtil.getString(R.string.delete));
        } else {
            navBar.setNavTitle(GLResourcesUtil.getString(R.string.add_address));
        }
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
        tvCityAndArea.setText(text);
    }

    @Override
    public void onNoFastClick(View v) {
        if (llWheelView.getVisibility() == View.VISIBLE) {
            llWheelView.setVisibility(View.GONE);
        }
        switch (v.getId()) {
            case R.id.tvSetDefault:
                setDefault();
                break;
            case R.id.tvSave:
                GLTextCheckUtil.GLCheckResult result = GLTextCheckUtil.checkAddress(tvCityAndArea.getText(),
                        etAddress.getText(), etCantinName.getText(), etLinkMan.getText(), etMobilePhone.getText());
                if (!result.isSuccess()) {
                    GLCommonManager.makeText(mContext, result.mFailMsg);
                } else {
                    setDataSave();
                }
                break;
            case R.id.llCityAndArea:
                hideInput(etAddress);
                if (llWheelView.getVisibility() == View.VISIBLE) {
                    llWheelView.setVisibility(View.GONE);
                } else {
                    llWheelView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void setDefault() {
        if (mPosition != GLConst.NEGATIVE) {
            mEditAddressModel.setIsDefault(1);
            GLProgressDialogUtil.showProgress(mContext);

            GLHttpRequestHelper.setAddressDefault(mContext, mEditAddressModel.getId(), new GLApiHandler(mContext) {

                @Override
                public void onSuccess(GLBaseInfo baseInfo) {
                    super.onSuccess(baseInfo);
                    GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.set_default_success));

                    GLAddressResp addressResp = (GLAddressResp) baseInfo;
                    GLDeliveryAddressModel addressModel = addressResp.getResult();
                    GLAddressManager.updateAddress(mContext, addressModel);

                    Intent intent = new Intent();
                    intent.putExtra(GLConst.INTENT_PARAM1, mPosition);
                    setResult(Activity.RESULT_OK, intent);
                    onBack();
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
    }

    private void setDataSave() {
        if (mPosition != GLConst.NEGATIVE) {
            editAddress();
        } else {
            addressAdd();
        }
    }

    private void addressAdd() {
        String addr = tvCityAndArea.getText().toString();
        String detail_addr = etAddress.getText().toString().trim();
        String link_man = etLinkMan.getText().toString().trim();
        String tel = etMobilePhone.getText().toString().trim();
        int is_default = 0;

        String province = GLProvinceLogic.getIns().mCurrentProviceName;
        String town = GLProvinceLogic.getIns().mCurrentCityName;
        String region = GLProvinceLogic.getIns().mCurrentDistrictName;

        GLProgressDialogUtil.showProgress(mContext);
        GLHttpRequestHelper.addressAdd(mContext, addr, detail_addr, link_man, tel, is_default, province, town, region, new GLApiHandler(mContext) {
            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);

                GLAddressResp addressResp = (GLAddressResp) baseInfo;
                GLDeliveryAddressModel addressModel = addressResp.getResult();
                GLAddressManager.saveAddress(mContext, addressModel);

                Intent intent = new Intent();
                intent.putExtra(GLConst.INTENT_PARAM1, GLConst.NEGATIVE);
                setResult(Activity.RESULT_OK, intent);
                onBack();
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

    private void editAddress() {
        String addr = tvCityAndArea.getText().toString();
        String detail_addr = etAddress.getText().toString().trim();
        String link_man = etLinkMan.getText().toString().trim();
        String tel = etMobilePhone.getText().toString().trim();
        int is_default = mEditAddressModel.getIsDefault();
        String province = GLProvinceLogic.getIns().mCurrentProviceName;
        String town = GLProvinceLogic.getIns().mCurrentCityName;
        String region = GLProvinceLogic.getIns().mCurrentDistrictName;
        long addressId = mEditAddressModel.getId();

        GLProgressDialogUtil.showProgress(mContext);
        GLHttpRequestHelper.addressEdit(mContext, addressId, addr, detail_addr, link_man, tel, is_default, province, town, region, new GLApiHandler(mContext) {
            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);

                GLAddressResp addressResp = (GLAddressResp) baseInfo;
                GLDeliveryAddressModel addressModel = addressResp.getResult();
                GLAddressManager.saveAddress(mContext, addressModel);

                Intent intent = new Intent();
                intent.putExtra(GLConst.INTENT_PARAM, mEditAddressModel);
                intent.putExtra(GLConst.INTENT_PARAM1, mPosition);
                setResult(Activity.RESULT_OK, intent);
                onBack();
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

    private void addressDel() {
        GLProgressDialogUtil.showProgress(mContext, GLResourcesUtil.getString(R.string.start_delete));
        GLHttpRequestHelper.addressRemove(mContext, mEditAddressModel.getId(), new GLApiHandler(mContext) {
            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);
                GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.address_del_success));

                GLAddressManager.delAddress(mContext, mEditAddressModel.getId());

                Intent intent = new Intent();
                intent.putExtra(GLConst.INTENT_PARAM, mEditAddressModel);
                intent.putExtra(GLConst.INTENT_PARAM1, mPosition);
                setResult(Activity.RESULT_OK, intent);
                onBack();
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
    protected void onPause() {
        super.onPause();
        hideInput(etAddress);
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
        } else if (viewId == R.id.tvNavRight) {
            if (llWheelView.getVisibility() == View.VISIBLE) {
                llWheelView.setVisibility(View.GONE);
            }
            addressDel();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GLProvinceLogic.getIns().release();
    }
}
