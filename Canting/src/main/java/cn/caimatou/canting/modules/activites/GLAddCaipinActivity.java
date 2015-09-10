package cn.caimatou.canting.modules.activites;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.GLCaipinModel;
import cn.caimatou.canting.bean.GLCaipinUnitModel;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLCustomBroadcast;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.manager.GLDialogManager;
import cn.caimatou.canting.utils.GLNumberUtils;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLTextCheckUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Description：添加菜品
 * <br/><br/>Created by Fausgoal on 15/8/31.
 * <br/><br/>
 */
public class GLAddCaipinActivity extends GLParentActivity {
    private static final String TAG = "GLAddCaipinActivity";

    private View mView = null;
    private LinearLayout llAddCaipin = null;
    private TextView tvContinueToAdd = null;
    private TextView tvCompleter = null;
    private Button btnSvae = null;
    private EditText etCaiName = null;
    private EditText etCaiCount = null;
    private LinearLayout llCaiUnit = null;
    private TextView tvCaiUnit = null;
    private EditText etOtherUnit = null;
    private TextView etNote = null;
    private RelativeLayout rlShoppingCar = null;
    private FrameLayout flShoppingCar = null;
    private TextView tvCartCount = null;

    private List<GLCaipinModel> mCaipinModels = null;

    private int mEditPosition = GLConst.NEGATIVE;
    private GLCaipinModel mEditCaipinModel = null;

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mEditPosition = bundle.getInt(GLConst.INTENT_PARAM1, GLConst.NEGATIVE);
            mCaipinModels = (List<GLCaipinModel>) bundle.get(GLConst.INTENT_PARAM);
            if (mEditPosition != GLConst.NEGATIVE) {
                mEditCaipinModel = (GLCaipinModel) bundle.get(GLConst.INTENT_PARAM2);
            }
        }
        if (null == mCaipinModels) {
            mCaipinModels = new ArrayList<>();
        }
    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.activity_add_caiping_layout, null, false);
        ScrollView svView = findView(mView, R.id.svView);
        svView.smoothScrollTo(0, 0);

        llAddCaipin = findView(mView, R.id.llAddCaipin);
        tvContinueToAdd = findView(mView, R.id.tvContinueToAdd);
        tvCompleter = findView(mView, R.id.tvCompleter);
        btnSvae = findView(mView, R.id.btnSvae);
        etCaiName = findView(mView, R.id.etCaiName);
        etCaiCount = findView(mView, R.id.etCaiCount);
        llCaiUnit = findView(mView, R.id.llCaiUnit);
        tvCaiUnit = findView(mView, R.id.tvCaiUnit);
        etOtherUnit = findView(mView, R.id.etOtherUnit);
        etNote = findView(mView, R.id.etNote);
        rlShoppingCar = findView(mView, R.id.rlShoppingCar);
        flShoppingCar = findView(mView, R.id.flShoppingCar);
        tvCartCount = findView(mView, R.id.tvCartCount);

        // TODO
        tvCaiUnit.setText("斤");

        int cartCount = GLConst.NONE;
        if (null != mCaipinModels) {
            cartCount = mCaipinModels.size();
        }
        setCarCount(cartCount);
        if (mEditPosition != GLConst.NEGATIVE) {
            setText(mEditCaipinModel);
        } else {
            // 添加时，先从缓存中读取上次保存数据
            loadingForCache();
        }
    }

    private void setCarCount(int cartCount) {
        tvCartCount.setText(String.valueOf(cartCount));
    }

    @Override
    protected void setListener() {
        GLViewClickUtil.setNoFastClickListener(tvContinueToAdd, this);
        GLViewClickUtil.setNoFastClickListener(tvCompleter, this);
        GLViewClickUtil.setNoFastClickListener(llCaiUnit, this);
        GLViewClickUtil.setNoFastClickListener(flShoppingCar, this);
        GLViewClickUtil.setNoFastClickListener(btnSvae, this);
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        if (mEditPosition != GLConst.NEGATIVE) {
            navBar.setNavTitle(GLResourcesUtil.getString(R.string.edit_caipin));
            navBar.setRightBtn(R.string.delete, GLConst.NONE, GLConst.NONE);
            llAddCaipin.setVisibility(View.GONE);
            btnSvae.setVisibility(View.VISIBLE);
            rlShoppingCar.setVisibility(View.GONE);
        } else {
            navBar.setNavTitle(GLResourcesUtil.getString(R.string.add_caipin));
            llAddCaipin.setVisibility(View.VISIBLE);
            btnSvae.setVisibility(View.GONE);
            rlShoppingCar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNoFastClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tvContinueToAdd: // 继续添加
                GLTextCheckUtil.GLCheckResult result = GLTextCheckUtil.checkAddCaipin(etCaiName.getText(),
                        etCaiCount.getText(), tvCaiUnit.getText(), etOtherUnit.getText());
                if (!result.isSuccess()) {
                    GLCommonManager.makeText(mContext, result.mFailMsg);
                } else {
                    // 保存并清空继续输入
                    addCompleter();
                    completer();
                    setCarCount(mCaipinModels.size());
                    // 如果用户还要继续添加，把编辑的id设置为-1
                    mEditPosition = GLConst.NEGATIVE;
                    etCaiName.setText("");
                    etCaiCount.setText("");
                    tvCaiUnit.setText("斤");
                    etNote.setText("");
                }
                break;
            case R.id.tvCompleter:// 完成
            case R.id.btnSvae:
                GLTextCheckUtil.GLCheckResult cResult = GLTextCheckUtil.checkAddCaipin(etCaiName.getText(),
                        etCaiCount.getText(), tvCaiUnit.getText(), etOtherUnit.getText());
                if (!cResult.isSuccess()) {
                    GLCommonManager.makeText(mContext, cResult.mFailMsg);
                } else {
                    addCompleter();
                    completer();
                    onBack();
                }
                break;
            case R.id.llCaiUnit: // 单位
                intent = new Intent(mContext, GLDialogActivity.class);
                intent.putExtra(GLConst.INTENT_PARAM, GLConst.DIALOG_UNIT);
                intent.putExtra(GLConst.INTENT_PARAM1, tvCaiUnit.getText());
                intent.putExtra(GLConst.INTENT_PARAM2, getCaipinUnit());
                startActivityForResult(intent, GLConst.DIALOG_UNIT);
                break;
            case R.id.flShoppingCar: // 购物车
                ArrayList<GLCaipinModel> params = new ArrayList<>();
                if (null != mCaipinModels) {
                    params.addAll(mCaipinModels);
                }

                intent = new Intent(mContext, GLShoppingCarDialogActivity.class);
                intent.putExtra(GLConst.INTENT_PARAM, params);
                startActivity(intent);
                break;
        }
    }

    private ArrayList<GLCaipinUnitModel> getCaipinUnit() {
        ArrayList<GLCaipinUnitModel> caipinUnitModels = new ArrayList<>();
        GLCaipinUnitModel caipinUnitModel = new GLCaipinUnitModel();
        caipinUnitModel.setId(1);
        caipinUnitModel.setUnit("斤");
        caipinUnitModels.add(caipinUnitModel);

        caipinUnitModel = new GLCaipinUnitModel();
        caipinUnitModel.setId(2);
        caipinUnitModel.setUnit("包");
        caipinUnitModels.add(caipinUnitModel);

        caipinUnitModel = new GLCaipinUnitModel();
        caipinUnitModel.setId(3);
        caipinUnitModel.setUnit("盒");
        caipinUnitModels.add(caipinUnitModel);

        caipinUnitModel = new GLCaipinUnitModel();
        caipinUnitModel.setId(4);
        caipinUnitModel.setUnit("袋");
        caipinUnitModels.add(caipinUnitModel);

        caipinUnitModel = new GLCaipinUnitModel();
        caipinUnitModel.setId(5);
        caipinUnitModel.setUnit("条");
        caipinUnitModels.add(caipinUnitModel);

        caipinUnitModel = new GLCaipinUnitModel();
        caipinUnitModel.setId(6);
        caipinUnitModel.setUnit("斤1");
        caipinUnitModels.add(caipinUnitModel);

        caipinUnitModel = new GLCaipinUnitModel();
        caipinUnitModel.setId(7);
        caipinUnitModel.setUnit("包1");
        caipinUnitModels.add(caipinUnitModel);

        caipinUnitModel = new GLCaipinUnitModel();
        caipinUnitModel.setId(8);
        caipinUnitModel.setUnit("盒1");
        caipinUnitModels.add(caipinUnitModel);

        caipinUnitModel = new GLCaipinUnitModel();
        caipinUnitModel.setId(9);
        caipinUnitModel.setUnit("袋1");
        caipinUnitModels.add(caipinUnitModel);

        caipinUnitModel = new GLCaipinUnitModel();
        caipinUnitModel.setId(10);
        caipinUnitModel.setUnit("条1");
        caipinUnitModels.add(caipinUnitModel);

        caipinUnitModel = new GLCaipinUnitModel();
        caipinUnitModel.setId(11);
        caipinUnitModel.setUnit("斤2");
        caipinUnitModels.add(caipinUnitModel);

        caipinUnitModel = new GLCaipinUnitModel();
        caipinUnitModel.setId(12);
        caipinUnitModel.setUnit("包2");
        caipinUnitModels.add(caipinUnitModel);

        caipinUnitModel = new GLCaipinUnitModel();
        caipinUnitModel.setId(13);
        caipinUnitModel.setUnit("盒2");
        caipinUnitModels.add(caipinUnitModel);

        caipinUnitModel = new GLCaipinUnitModel();
        caipinUnitModel.setId(14);
        caipinUnitModel.setUnit("袋2");
        caipinUnitModels.add(caipinUnitModel);

        caipinUnitModel = new GLCaipinUnitModel();
        caipinUnitModel.setId(15);
        caipinUnitModel.setUnit("其他");
        caipinUnitModels.add(caipinUnitModel);

        return caipinUnitModels;
    }

    private void addCompleter() {
        if (mEditPosition == GLConst.NEGATIVE) {
            // 添加模式
            GLCaipinModel caipinModel = new GLCaipinModel();
            caipinModel.setName(etCaiName.getText().toString().trim());
            caipinModel.setNum(GLNumberUtils.stringToInt(etCaiCount.getText().toString()));
            caipinModel.setMemo(etNote.getText().toString().trim());
            String unit = tvCaiUnit.getText().toString();
            if (GLResourcesUtil.getString(R.string.other).equals(unit)) {
                caipinModel.setUnit(etOtherUnit.getText().toString());
            } else {
                caipinModel.setUnit(unit);
            }

            mCaipinModels.add(caipinModel);
        } else {
            // 编辑模式
            GLCaipinModel caipinModel = mCaipinModels.get(mEditPosition);
            caipinModel.setName(etCaiName.getText().toString().trim());
            caipinModel.setNum(GLNumberUtils.stringToInt(etCaiCount.getText().toString()));
            caipinModel.setMemo(etNote.getText().toString().trim());

            String unit = tvCaiUnit.getText().toString();
            if (GLResourcesUtil.getString(R.string.other).equals(unit)) {
                caipinModel.setUnit(etOtherUnit.getText().toString());
            } else {
                caipinModel.setUnit(unit);
            }
        }
    }

    private void completer() {
        ArrayList<GLCaipinModel> params = new ArrayList<>();
        if (null != mCaipinModels) {
            params.addAll(mCaipinModels);
        }
        Intent intent = new Intent();
        intent.setAction(GLCustomBroadcast.ON_ADD_CAIPIN_ACTION);
        intent.putExtra(GLConst.INTENT_PARAM, params);
        sendBroadcast(intent);
    }

    private void setText(GLCaipinModel caipinModel) {
        if (null != caipinModel) {
            etCaiName.setText(caipinModel.getName());
            etCaiCount.setText(caipinModel.getNum() + "");
            tvCaiUnit.setText(caipinModel.getUnit());
            etNote.setText(caipinModel.getMemo());

            etCaiCount.setSelection(etCaiCount.getText().length());
            etCaiName.setFocusable(true);
            etCaiName.setFocusableInTouchMode(true);
            etCaiName.requestFocus();
            etCaiName.requestFocusFromTouch();
            etCaiName.setSelection(etCaiName.getText().length());
        }
    }

    /**
     * 进入时先从缓存中读取是否有输入一半缓存下来的数据
     */
    private void loadingForCache() {
        // TODO
//        setText(null);
    }

    /**
     * 按返回键，把输入的缓存下来，下次进入时加载
     */
    private void addToCache() {
        // TODO
//        etCaiName.getText().toString();
//        etCaiCount.getText().toString();
//        tvCaiUnit.getText().toString();
//        etNote.getText().toString();
    }

    private void onBack() {
        GLViewManager.getIns().pop(this);
    }

    @Override
    public boolean onItemSelectedListener(int viewId) {
        if (viewId == R.id.ivNavLeft) {
            addToCache();
            onBack();
            return false;
        } else if (viewId == R.id.tvNavRight) {
            String msg = GLResourcesUtil.getString(R.string.is_sure_delete);
            GLDialogManager.onAffirm(mContext, msg, GLResourcesUtil.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                    GLCaipinModel caipinModel = mCaipinModels.get(mEditPosition);
                    mCaipinModels.remove(mEditPosition);
                    completer();
                    GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.delete_success));
                    // TODO 要去删除数据库已缓存起来的菜品
                    onBack();
                }
            });
        }
        return super.onItemSelectedListener(viewId);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            addToCache();
            onBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GLConst.DIALOG_UNIT:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    GLCaipinUnitModel caipinUnitModel = (GLCaipinUnitModel) bundle.get(GLConst.INTENT_PARAM);
                    if (null != caipinUnitModel) {
                        String unit = caipinUnitModel.getUnit();
                        tvCaiUnit.setText(unit);
                        if (GLResourcesUtil.getString(R.string.other).equals(unit)) {
                            etOtherUnit.setVisibility(View.VISIBLE);
                        } else {
                            etOtherUnit.setVisibility(View.GONE);
                        }
                    }
                }
                break;
        }
    }
}
