package cn.caimatou.canting.utils.http;

import android.content.Context;
import android.os.Looper;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.utils.GLLog;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 2015/5/17.
 * <br/><br/>
 */
public class GLApiHandler {
    private static final String TAG = "GLApiHandler";

    protected Context mContext = null;

    public GLApiHandler(Context context) {
        mContext = context;
    }

    public void onSuccess(GLBaseInfo baseInfo) {
    }

    public void onFailure(GLBaseInfo baseInfo) {
        if (null == baseInfo) {
            boolean isConnection = GLCommonManager.isNetworkConnected(mContext);
            if (isConnection) {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    String msg = mContext.getResources().getString(R.string.gl_service_error);
                    GLCommonManager.makeText(mContext, msg);
                }
            }
        } else {
            if (!baseInfo.isSuccess()) {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    GLLog.trace(TAG, baseInfo.getMessage());
                }
            }
        }
    }
}
