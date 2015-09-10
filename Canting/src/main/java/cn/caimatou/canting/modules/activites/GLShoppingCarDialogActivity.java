package cn.caimatou.canting.modules.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.bean.GLCaipinModel;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLCustomBroadcast;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.modules.adapters.GLAddCaiAdapter;
import cn.caimatou.canting.utils.GLPixelsUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;

/**
 * Description：购物车弹框界面
 * <br/><br/>Created by Fausgoal on 15/9/1.
 * <br/><br/>
 */
public class GLShoppingCarDialogActivity extends Activity implements GLViewClickUtil.NoFastClickListener {
    private Context mContext = null;
    protected Point mPoint;

    private List<GLCaipinModel> mCaipinModels = null;

    private TextView tvCartCount = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopping_car_layout);

        RelativeLayout rlCart = findView(R.id.rlCart);
        FrameLayout flShoppingCar = findView(R.id.flShoppingCar);
        tvCartCount = findView(R.id.tvCartCount);
        ListView lvList = findView(R.id.lvList);
        TextView tvContinueToAdd = findView(R.id.tvContinueToAdd);
        TextView tvCompleter = findView(R.id.tvCompleter);

        mContext = this;
        mPoint = GLPixelsUtil.getDeviceSize(mContext);

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mCaipinModels = (List<GLCaipinModel>) bundle.get(GLConst.INTENT_PARAM);
        }

        setCartCount();

        GLAddCaiAdapter adapter = new GLAddCaiAdapter(mContext, mCaipinModels, GLAddCaiAdapter.TYPE_SHOW_SHOPPING_CART, null);
        lvList.setAdapter(adapter);

        GLViewClickUtil.setNoFastClickListener(flShoppingCar, this);
        GLViewClickUtil.setNoFastClickListener(tvContinueToAdd, this);
        GLViewClickUtil.setNoFastClickListener(tvCompleter, this);

        setWindowAttributes();

        int height = (int) (mPoint.y * 0.6f);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlCart.getLayoutParams();
        params.height = height;
        rlCart.setLayoutParams(params);
    }

    private void setCartCount() {
        int count = GLConst.NONE;
        if (null != mCaipinModels) {
            count = mCaipinModels.size();
        }
        tvCartCount.setText(String.valueOf(count));
    }

    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    private void setWindowAttributes() {
        int width = mPoint.x; // 屏幕的宽度

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = width;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.flShoppingCar:
            case R.id.tvContinueToAdd:
                finish();
                break;
            case R.id.tvCompleter:
                completer();
                finish();
                try {
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            GLViewManager.getIns().pop(GLAddCaipinActivity.class);
                        }
                    }, 50);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
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
}
