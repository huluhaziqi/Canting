package cn.caimatou.canting.modules.activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.Canting;
import cn.caimatou.canting.bean.Staff;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.modules.logic.GLLoginLogic;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;
import cn.caimatou.canting.widget.GLNavigationBar;
import cn.kankan.wheel.widget.OnWheelChangedListener;

/**
 * Created by Rosicky on 15/9/1.
 */
public class GLCantingInfoActivity extends GLParentActivity {
    private View mView;
    private LayoutInflater inflater;
    private TextView cantingName, cantingAddress, cantingBrief, cantingPerson, cantingGender, cantingPhone;

    private Canting canting;

    @Override
    protected void initData() {
        mContext = this;
        inflater = LayoutInflater.from(mContext);
        fetchCanting();
    }

    private void fetchCanting() {
        // TODO 获取餐厅数据
        Staff staff = new Staff();
        staff.setStaffName("阿萨德发");
        canting = new Canting();
//        canting.setLogo(); 设置logo图案
        canting.setName("菜码头");
        canting.setAddress("菜码头");
        canting.setBrief("阿萨德开发商的风景啊；速度快放假啊");
        canting.setStaff(staff);
        canting.setGender("男");
        canting.setIsIdentify(false);
        canting.setPhone("15555558888");
    }

    @Override
    protected void initView() {
        mView = inflater.inflate(R.layout.activity_canting_info, null);
        ((TextView) mView.findViewById(R.id.canting_name).findViewById(R.id.common_text1)).setText(GLResourcesUtil.getString(R.string.canting_name1));
        cantingName = (TextView) mView.findViewById(R.id.canting_name).findViewById(R.id.common_text2);
        ((TextView) mView.findViewById(R.id.canting_address).findViewById(R.id.common_text1)).setText(GLResourcesUtil.getString(R.string.address));
        cantingAddress = (TextView) mView.findViewById(R.id.canting_address).findViewById(R.id.common_text2);
        ((TextView) mView.findViewById(R.id.canting_brief).findViewById(R.id.common_text1)).setText(GLResourcesUtil.getString(R.string.brief));
        cantingBrief = (TextView) mView.findViewById(R.id.canting_brief).findViewById(R.id.common_text2);
        ((TextView) mView.findViewById(R.id.canting_person).findViewById(R.id.common_text1)).setText(GLResourcesUtil.getString(R.string.contact_person));
        cantingPerson = (TextView) mView.findViewById(R.id.canting_person).findViewById(R.id.common_text2);
        ((TextView) mView.findViewById(R.id.canting_gender).findViewById(R.id.common_text1)).setText(GLResourcesUtil.getString(R.string.gender));
        mView.findViewById(R.id.canting_gender).setVisibility(View.GONE); // 去掉
        cantingGender = (TextView) mView.findViewById(R.id.canting_gender).findViewById(R.id.common_text2);
        ((TextView) mView.findViewById(R.id.canting_phone).findViewById(R.id.common_text1)).setText(GLResourcesUtil.getString(R.string.contact_phone));
        cantingPhone = (TextView) mView.findViewById(R.id.canting_phone).findViewById(R.id.common_text2);
        initViewData();
    }

    private void initViewData() {
        cantingName.setText(canting.getName());
        cantingAddress.setText(canting.getAddress());
        cantingBrief.setText(canting.getBrief());
        cantingPerson.setText(canting.getStaff().getStaffName());
        cantingGender.setText(canting.getGender());
        cantingPhone.setText(canting.getPhone());
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    protected void setListener() {
        mView.findViewById(R.id.llcantingImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                GLViewManager.getIns().showActivity(mContext, intent, GLConst.REQUEST_PHOTO);
            }
        });
//        GLViewClickUtil.setNoFastClickListener(cantingName, this);
//        GLViewClickUtil.setNoFastClickListener(cantingAddress, this);
        GLViewClickUtil.setNoFastClickListener(mView.findViewById(R.id.canting_brief), this);
        GLViewClickUtil.setNoFastClickListener(mView.findViewById(R.id.canting_person), this);
//        GLViewClickUtil.setNoFastClickListener(mView.findViewById(R.id.canting_gender), this);
        mView.findViewById(R.id.canting_gender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GLCantingInfoActivity.this);
                builder.setTitle(GLResourcesUtil.getString(R.string.choose_sex));
                final String[] genders = {GLResourcesUtil.getString(R.string.boy), GLResourcesUtil.getString(R.string.girl)};
                builder.setItems(genders, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cantingGender.setText(genders[i]);
                    }
                });
                builder.show();
            }
        });
        GLViewClickUtil.setNoFastClickListener(mView.findViewById(R.id.canting_phone), this);
        mView.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 保存更新的信息
            }
        });
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.canting_name));
    }

    @Override
    public void onNoFastClick(View v) {
        int editType = -1;
        CharSequence content = "";
        switch (v.getId()) {
            case R.id.canting_name:
                editType = GLLoginLogic.EDIT_CANTING_NAME;
                content = cantingName.getText();
                break;
            case R.id.canting_address:
                editType = GLLoginLogic.EDIT_CANTING_ADDRESS;
                content = cantingAddress.getText();
                break;
            case R.id.canting_brief:
                editType = GLLoginLogic.EDIT_CANTING_BRIEF;
                content = cantingBrief.getText();
                break;
            case R.id.canting_person:
                editType = GLLoginLogic.EDIT_CANTING_LINKMAN;
                content = cantingPerson.getText();
                break;
            case R.id.canting_gender:
                editType = GLLoginLogic.EDIT_CANTING_SEX;
                content = cantingGender.getText();
                break;
            case R.id.canting_phone:
                editType = GLLoginLogic.EDIT_CANTING_LINKPHONE;
                content = cantingPhone.getText();
                break;
        }
        Intent intent = new Intent(mContext, GLEditInfoActivity.class);
        intent.putExtra(GLConst.INTENT_PARAM, editType);
        intent.putExtra(GLConst.INTENT_PARAM1, content);
        GLViewManager.getIns().showActivity(mContext, intent, GLLoginLogic.REQUEST_EDIT_CANTING_INFO);
    }

    private void onBack() {
        // TODO 如果有改动信息，退出当前activity时请求服务器上传更新后的信息
        GLViewManager.getIns().pop(this);
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
    public boolean onItemSelectedListener(int viewId) {
        if (viewId == R.id.ivNavLeft) {
            onBack();
        }
        return super.onItemSelectedListener(viewId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GLLoginLogic.REQUEST_EDIT_CANTING_INFO) {
            if (resultCode == Activity.RESULT_OK) {
                int editType = data.getExtras().getInt(GLConst.INTENT_PARAM1);
                String content = data.getExtras().getString(GLConst.INTENT_PARAM);
                switch (editType) {
                    case GLLoginLogic.EDIT_CANTING_BRIEF:
                        cantingBrief.setText(content);
                        break;
                    case GLLoginLogic.EDIT_CANTING_LINKMAN:
                        cantingPerson.setText(content);
                        break;
                    case GLLoginLogic.EDIT_CANTING_LINKPHONE:
                        cantingPhone.setText(content);
                }
            }
        } else if (requestCode == GLConst.REQUEST_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                Log.e("uri", uri.toString());
                ContentResolver cr = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                /* 将Bitmap设定到ImageView */
                    ((ImageView) mView.findViewById(R.id.canting_img)).setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Log.e("Exception", e.getMessage(),e);
                }
            }
        }
    }
}
