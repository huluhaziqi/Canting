package cn.caimatou.canting.manager;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Looper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import cn.caimatou.canting.R;
import cn.caimatou.canting.utils.GLLog;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.http.GLApiFunction;
import cn.caimatou.canting.utils.http.GLApiHandler;
import cn.caimatou.canting.utils.http.GLHttpRequestParams;
import cn.caimatou.canting.utils.http.IGLHttpAPI;
import cn.caimatou.canting.utils.http.IGLSessionApiHandler;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 2015/5/17.
 * <br/><br/>
 */
public class GLApiManager {
    private static final String TAG = "GLApiManager";

    private static IGLSessionApiHandler mSessionApiHandler;

    public static void initSessionApiHandler(IGLSessionApiHandler sessionApiHandler) {
        if (mSessionApiHandler == null) {
            mSessionApiHandler = sessionApiHandler;
        }
    }

    public static IGLHttpAPI getInitialize(final Context context) {
        boolean isConnection = GLCommonManager.isNetworkConnected(context);
        if (!isConnection) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                GLDialogManager.onAffirm(context, GLResourcesUtil.getString(R.string.set_network),
                        GLResourcesUtil.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                                GLCommonManager.startWirelessSetting(context);
                            }
                        });
            }
        }

        initSessionApiHandler(new IGLSessionApiHandler() {
            @Override
            public void onSessionError(Context context, GLApiHandler handler) {
                // Notice 可以这里去执行自动登录等
                GLLog.trace(TAG, "onSessionError-->走到了initSessionApiHandler的回调结果里了");
            }
        });

        IGLHttpAPI api = new GLApiFunction(context, GLHttpRequestParams.HTTP_API_URL, mSessionApiHandler);
        return wrapApi(api);
    }

    private static IGLHttpAPI wrapApi(IGLHttpAPI api) {
        return (IGLHttpAPI) Proxy.newProxyInstance(IGLHttpAPI.class.getClassLoader(), new Class[]{IGLHttpAPI.class}, new ApiInvocationHandler(api));
    }

    private static class ApiInvocationHandler implements InvocationHandler {

        private IGLHttpAPI api;

        public ApiInvocationHandler(IGLHttpAPI api) {
            this.api = api;
        }

        @Override
        public Object invoke(Object proxy, final Method method, final Object[] args)
                throws Throwable {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... arg0) {
                    try {
                        method.invoke(api, args);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
            return null;
        }
    }
}
