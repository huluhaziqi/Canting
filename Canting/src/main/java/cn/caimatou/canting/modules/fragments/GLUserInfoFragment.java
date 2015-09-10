package cn.caimatou.canting.modules.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.base.GLParentFragment;
import cn.caimatou.canting.bean.GLUserInfo;
import cn.caimatou.canting.common.GLCommonVariables;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.manager.GLUserManager;
import cn.caimatou.canting.modules.activites.GLEditInfoActivity;
import cn.caimatou.canting.modules.activites.GLRegistActivity;
import cn.caimatou.canting.modules.activites.GLRegistVCodeActivity;
import cn.caimatou.canting.modules.activites.GLUserInfoActivity;
import cn.caimatou.canting.modules.logic.GLLoginLogic;
import cn.caimatou.canting.response.GLBooleanResp;
import cn.caimatou.canting.response.GLStep1Resp;
import cn.caimatou.canting.utils.GLDateUtil;
import cn.caimatou.canting.utils.GLProgressDialogUtil;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLTextCheckUtil;
import cn.caimatou.canting.utils.http.GLApiHandler;
import cn.caimatou.canting.utils.http.GLHttpRequestHelper;

/**
 * Created by Rosicky on 15/8/30.
 */
public class GLUserInfoFragment extends GLParentFragment {

    private static final String TAG = "GLUserInfoFragment";
    private static final String KEY_ARG_POSITION = "key-position";
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;
    private File tempFile = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());

    private int position = GLConst.NEGATIVE;
    private View mView = null;
    private GLUserInfo userInfo = null;

    public static GLUserInfoFragment newInstance(int position) {
        GLUserInfoFragment f = new GLUserInfoFragment();
        Bundle b = new Bundle();
        b.putInt(KEY_ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        position = getArguments().getInt(KEY_ARG_POSITION);
        userInfo = ((GLUserInfoActivity) getActivity()).getUserInfo();
        switch (position) {
            case 0:
                mView = inflater.inflate(R.layout.fragment_user_center, container, false);
                break;
            case 1:
                mView = inflater.inflate(R.layout.fragment_edit_userinfo, container, false);
                break;
            case 2:
                mView = inflater.inflate(R.layout.fragment_edit_password, container, false);
                break;
            case 3:
                mView = inflater.inflate(R.layout.fragment_edit_phone, container, false);
                break;
        }
        return mView;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        switch (position) {
            case 0:
                mView.findViewById(R.id.user_center_avater).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog();
                    }
                });
                mView.findViewById(R.id.user_center_info).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((GLUserInfoActivity) GLUserInfoFragment.this.getActivity()).setShowPager(1);
                        ((GLUserInfoActivity) GLUserInfoFragment.this.getActivity()).setNavTitle(GLResourcesUtil.getString(R.string.edit_userinfo));
                    }
                });
                mView.findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GLUserManager.logout(GLUserInfoFragment.this.getActivity());
                    }
                });
                mView.findViewById(R.id.user_center_pwd).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((GLUserInfoActivity) GLUserInfoFragment.this.getActivity()).setShowPager(2);
                        ((GLUserInfoActivity) GLUserInfoFragment.this.getActivity()).setNavTitle(GLResourcesUtil.getString(R.string.edit_password));
                    }
                });
                mView.findViewById(R.id.user_center_phone).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((GLUserInfoActivity) GLUserInfoFragment.this.getActivity()).setShowPager(3);
                        ((GLUserInfoActivity) GLUserInfoFragment.this.getActivity()).setNavTitle(GLResourcesUtil.getString(R.string.edit_phone));
                    }
                });
                break;
            case 1:
                ((TextView) mView.findViewById(R.id.user_name_show)).setText(((GLUserInfo) GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.USER)).getNickname());
                ((TextView) mView.findViewById(R.id.user_sex_show)).setText(userInfo.getSex() == 1 ? "男" : "女");
                ((TextView) mView.findViewById(R.id.user_phone_show)).setText(userInfo.getLoginName());
                ((TextView) mView.findViewById(R.id.user_birth_show)).setText(userInfo.getBirthday());
                mView.findViewById(R.id.user_name).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int editType = GLLoginLogic.EDIT_USER_NAME;
                        String content = ((TextView) view.findViewById(R.id.user_name_show)).getText().toString();
                        Intent intent = new Intent(mContext, GLEditInfoActivity.class);
                        intent.putExtra(GLConst.INTENT_PARAM, editType);
                        intent.putExtra(GLConst.INTENT_PARAM1, content);
                        GLViewManager.getIns().showActivity(mContext, intent, GLLoginLogic.REQUEST_EDIT_CANTING_INFO);
                    }
                });
                mView.findViewById(R.id.user_sex).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(GLUserInfoFragment.this.getActivity());
                        builder.setTitle(GLResourcesUtil.getString(R.string.choose_sex));
                        final String[] genders = {GLResourcesUtil.getString(R.string.boy), GLResourcesUtil.getString(R.string.girl)};
                        builder.setItems(genders, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((TextView) mView.findViewById(R.id.user_sex_show)).setText(genders[i]);
                            }
                        });
                        builder.show();
                    }
                });
                mView.findViewById(R.id.user_birth).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        new DatePickerDialog(GLUserInfoFragment.this.getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(datePicker.getYear(), datePicker.getMonth(),
                                        datePicker.getDayOfMonth());
                                String birthday = GLDateUtil.format(GLDateUtil.YYYY_MM_DD, calendar.getTimeInMillis());
                                if (calendar.compareTo(Calendar.getInstance()) < 0) {
                                    ((TextView) mView.findViewById(R.id.user_birth_show)).setText(birthday);
                                } else {
                                    GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.should_before_now));
                                }
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                mView.findViewById(R.id.user_info_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO 保存更新的个人资料信息
                        long userId = Long.valueOf((String) GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.ACCOUNT));
                        String nickName = ((TextView) mView.findViewById(R.id.user_name_show)).getText().toString();
                        String sex = ((TextView) mView.findViewById(R.id.user_sex_show)).getText().toString();
                        int sexInt;
                        if (sex.equals("男")) {
                            sexInt = 1;
                        } else {
                            sexInt = 2;
                        }
                        String birthday = ((TextView) mView.findViewById(R.id.user_birth_show)).getText().toString();
                        if (nickName == null || nickName.length() == 0) {
                            GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.enter_user_nickname));
                            return;
                        }
                        if (sex == null || sex.length() == 0) {
                            GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.enter_user_sex));
                            return;
                        }
                        if (birthday == null || birthday.length() == 0) {
                            GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.enter_user_birthday));
                            return;
                        }
                        GLUserManager.updateUserInfo(GLUserInfoFragment.this.getActivity(), userId, nickName, sexInt, birthday);
                    }
                });
                mView.findViewById(R.id.user_phone).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((GLUserInfoActivity) getActivity()).setShowPager(3);
                    }
                });
                break;
            case 2:
                mView.findViewById(R.id.user_pwd_show_img).setTag(1);
                mView.findViewById(R.id.user_pwd_show).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImageView imageView = (ImageView) view.findViewById(R.id.user_pwd_show_img);
                        if (null != imageView.getTag() && ((int)imageView.getTag()) == 1) {
                            imageView.setImageDrawable(GLResourcesUtil.getDrawable(R.mipmap.icon_pwd_selected));
                            imageView.setTag(0);
                            ((EditText) mView.findViewById(R.id.user_pwd_current)).setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            ((EditText) mView.findViewById(R.id.user_pwd_new)).setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            ((EditText) mView.findViewById(R.id.user_pwd_new1)).setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        } else {
                            imageView.setImageDrawable(GLResourcesUtil.getDrawable(R.mipmap.icon_pwd_unselect));
                            imageView.setTag(1);
                            ((EditText) mView.findViewById(R.id.user_pwd_current)).setTransformationMethod(PasswordTransformationMethod.getInstance());
                            ((EditText) mView.findViewById(R.id.user_pwd_new)).setTransformationMethod(PasswordTransformationMethod.getInstance());
                            ((EditText) mView.findViewById(R.id.user_pwd_new1)).setTransformationMethod(PasswordTransformationMethod.getInstance());

                        }
                    }
                });
                mView.findViewById(R.id.user_pwd_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String pwdCurrent = ((EditText) mView.findViewById(R.id.user_pwd_current)).getText().toString();
                        String pwdUpdate = ((EditText) mView.findViewById(R.id.user_pwd_new)).getText().toString();
                        String pwdConfirm = ((EditText) mView.findViewById(R.id.user_pwd_new1)).getText().toString();
                        GLTextCheckUtil.GLCheckResult result1 = GLTextCheckUtil.checkPassword(pwdCurrent);
                        GLTextCheckUtil.GLCheckResult result2 = GLTextCheckUtil.checkPasswordStyle(pwdUpdate);
                        GLTextCheckUtil.GLCheckResult result3 = GLTextCheckUtil.checkPasswordStyle(pwdConfirm);
                        if (!result1.isSuccess()) {
                        } else if (!(result2.isSuccess() && result3.isSuccess())) {
                            GLCommonManager.makeText(mContext, "密码格式错误, 请重新输入");
                        } else if (!pwdUpdate.equals(pwdConfirm)) {
                            GLCommonManager.makeText(mContext, "两次输入不同，请重新输入");
                        } else {
                        }
                        GLUserManager.updatePassword(mContext, pwdCurrent, pwdConfirm);
                    }
                });
                break;
            case 3:
                ((TextView) mView.findViewById(R.id.edit_phone_number)).setText(userInfo.getLoginName());
                mView.findViewById(R.id.get_vcode).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO 请求获取验证码 目前使用step1接口会返回“手机已注册”message
                        fetchCode((String) GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.ACCOUNT));
                    }
                });
                mView.findViewById(R.id.edit_phone_next).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO 验证vcode是否正确，如果正确则跳转至绑定新手机Activity，如果不正确，弹出Toast或者弹框
                        String account = (String) GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.ACCOUNT);
                        String checkCode = ((EditText) mView.findViewById(R.id.edit_phone_check_code)).getText().toString();
                        sendCheckCode(account, checkCode);
                    }
                });
                break;
        }
    }

    private void fetchCode(final String mobilePhone) {
        // NOTICE 先调用接口发送验证短信，发送成功再执行倒计时
        GLProgressDialogUtil.showProgress(mContext);

        GLHttpRequestHelper.step1(mContext, mobilePhone, new GLApiHandler(mContext) {
            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);
                GLStep1Resp step1Resp = (GLStep1Resp) baseInfo;
                if (null != step1Resp && step1Resp.isResult()) {
                    GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.send_code_success));
                    // 验证码发送成功
                    Intent intent = new Intent(mContext, GLRegistVCodeActivity.class);
                    intent.putExtra(GLConst.INTENT_PARAM, mobilePhone);
                    GLViewManager.getIns().showActivity(mContext, intent, false);
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

    private void sendCheckCode(final String mobilePhone, String checkCode) {
        GLProgressDialogUtil.showProgress(mContext);

        GLHttpRequestHelper.sendCheckCode(mContext, mobilePhone, checkCode, new GLApiHandler(mContext) {
            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);
                GLBooleanResp booleanResp = (GLBooleanResp) baseInfo;
                if (booleanResp.getResult()) {
                    GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.vcode_success));
                    GLViewManager.getIns().showActivity(mContext, GLRegistActivity.class, false);
                } else {

                }
            }

            @Override
            public void onFailure(GLBaseInfo baseInfo) {
                super.onFailure(baseInfo);
                GLProgressDialogUtil.dismissProgress(mContext);

            }
        });
    }

    public void refreshName(String content) {
        ((TextView) mView.findViewById(R.id.user_name_show)).setText(content);
    }

    //提示对话框方法
    private void showDialog() {
        new AlertDialog.Builder(this.getActivity())
                .setTitle("头像设置")
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        // 调用系统的拍照功能
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 指定调用相机拍照后照片的储存路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                    }
                })
                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                    }
                }).show();
    }

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    //将进行剪裁后的图片显示到UI界面上
    private void setPicToView(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            Bitmap photo = bundle.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            ((ImageView) mView.findViewById(R.id.user_avater_img)).setImageDrawable(drawable);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:
                startPhotoZoom(Uri.fromFile(tempFile), 150);
                break;
            case PHOTO_REQUEST_GALLERY:
                if (data != null)
                    startPhotoZoom(data.getData(), 150);
                break;
            case PHOTO_REQUEST_CUT:
                if (data != null)
                    setPicToView(data);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
