package cn.caimatou.canting.modules.activites;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.FileNotFoundException;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Created by Rosicky on 15/9/1.
 */
public class GLCertificationUploadActivity extends GLParentActivity {
    private View mView;
    private LinearLayout linearLayout;
    private Button btnSubmit;
    private ImageView imageView;

    private LayoutInflater inflater;

    @Override
    protected void initData() {
        mContext = this;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    protected void initView() {
        mView = inflater.inflate(R.layout.activity_certification_upload, null);
        linearLayout = (LinearLayout) mView.findViewById(R.id.businessLinear);
        btnSubmit = (Button) mView.findViewById(R.id.btnSubmit);
        imageView = (ImageView) mView.findViewById(R.id.common_img);
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    protected void setListener() {
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                GLViewManager.getIns().showActivity(mContext, intent, GLConst.REQUEST_PHOTO);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 上传图片
                onBack();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                /* 将Bitmap设定到ImageView */
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.aptitude_identify));
        navBar.setRightBtn(GLResourcesUtil.getString(R.string.pass));
    }

    private void onBack() {
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
        } else if (viewId == R.id.tvNavRight) {
            onBack();
        }
        return super.onItemSelectedListener(viewId);
    }
}
