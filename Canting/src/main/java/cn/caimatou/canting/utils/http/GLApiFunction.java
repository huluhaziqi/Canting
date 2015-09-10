package cn.caimatou.canting.utils.http;

import android.app.Activity;
import android.content.Context;

import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedHashMap;

import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.manager.GLUserManager;
import cn.caimatou.canting.response.GLAddressCompList;
import cn.caimatou.canting.response.GLAddressRemoveResp;
import cn.caimatou.canting.response.GLAddressResp;
import cn.caimatou.canting.response.GLBooleanResp;
import cn.caimatou.canting.response.GLCompanyAddResp;
import cn.caimatou.canting.response.GLConfirmDeliveryResp;
import cn.caimatou.canting.response.GLHisOrdersResp;
import cn.caimatou.canting.response.GLLogoutResp;
import cn.caimatou.canting.response.GLMyOrdersResp;
import cn.caimatou.canting.response.GLOrderCancelResp;
import cn.caimatou.canting.response.GLOrderInfoResp;
import cn.caimatou.canting.response.GLProviderResp;
import cn.caimatou.canting.response.GLRestaurantResp;
import cn.caimatou.canting.response.GLStep1Resp;
import cn.caimatou.canting.response.GLStep2Resp;
import cn.caimatou.canting.response.GLUpdatePwdByCheckCodeResp;
import cn.caimatou.canting.response.GLUserInfoResp;
import cn.caimatou.canting.utils.GLLog;
import cn.caimatou.canting.utils.GLStringUtil;
import cn.caimatou.canting.utils.MD5;
import cn.caimatou.canting.utils.http.asynchttp.AsyncHttpClient;
import cn.caimatou.canting.utils.http.asynchttp.JsonHttpResponseHandler;
import cn.caimatou.canting.utils.http.asynchttp.RequestParams;


/**
 * Description：
 * <br/><br/>Created by Fausgoal on 15/8/27.
 * <br/><br/>
 */
public class GLApiFunction implements IGLHttpAPI {
    private static final String TAG = "GLApiFunction";

    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        client.setTimeout(60000);
    }

    private Context mContext = null;
    private String mUrl;
    private IGLSessionApiHandler mSessionApiHandler = null;

    public GLApiFunction(Context context, String url, IGLSessionApiHandler sessionApiHandler) {
        mContext = context;
        mUrl = url;
        mSessionApiHandler = sessionApiHandler;
    }

    private class ApiJsonHttpResponseHandler extends JsonHttpResponseHandler {
        private GLApiHandler mHandler;
        private String mFunckey;

        public ApiJsonHttpResponseHandler(String funckey, GLApiHandler handler) {
            this.mFunckey = funckey;
            mHandler = handler;
        }

        private boolean isSuccess(GLBaseInfo info) {
            return null != info && info.isSuccess();
        }

        @Override
        public void onStart() {
            super.onStart();
        }

        public void onSuccess(JSONObject response, final GLBaseInfo info) {
            super.onSuccess(response);
            if (mContext instanceof Activity) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccess(info)) {
                            mHandler.onSuccess(info);
                        } else {
                            mHandler.onFailure(info);
                        }
                    }
                });
            } else {
                if (isSuccess(info)) {
                    mHandler.onSuccess(info);
                } else {
                    mHandler.onFailure(info);
                }
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);
            if (mContext instanceof Activity) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.onFailure(null);
                    }
                });
            } else {
                mHandler.onFailure(null);
            }
            GLLog.trace(TAG, mFunckey + "-->出现异常-->" + content + "\r\n" + error.getMessage());
            error.printStackTrace();
        }

        @Override
        public void onFinish() {
            super.onFinish();
        }
    }

    @Override
    public void login(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "login-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLUserInfoResp.class));
            }
        });
    }

    @Override
    public void logout(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "logout-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLLogoutResp.class));
            }
        });
    }

    @Override
    public void step1(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "step1-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLStep1Resp.class));
            }
        });
    }

    @Override
    public void step2(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "step2-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLStep2Resp.class));
            }
        });
    }

    @Override
    public void step3(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "step3-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLUserInfoResp.class));
            }
        });
    }

    @Override
    public void loginByCheckCode(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "loginByCheckCode-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLUserInfoResp.class));
            }
        });
    }

    @Override
    public void updatePasswordByCheckCode(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "updatePasswordByCheckCode-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLUpdatePwdByCheckCodeResp.class));
            }
        });
    }

    @Override
    public void fetchSuppliers(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "fetchSuppliers-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLProviderResp.class));
            }
        });
    }

    @Override
    public void fetchMySuppliers(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response, GLBaseInfo info) {
                GLLog.trace(TAG, "fetchMySuppliers-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLProviderResp.class));
            }
        });
    }

    @Override
    public void updatePassword(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "updatePassword-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLBooleanResp.class));
            }
        });
    }

    @Override
    public void sendCheckCode(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "verifyChangePhone-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLBooleanResp.class));
            }
        });
    }

    @Override
    public void updateUserInfo(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "updateUserInfo-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLBooleanResp.class));
            }
        });
    }

    @Override
    public void restaurant(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "restaurant-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLRestaurantResp.class));
            }
        });
    }

    @Override
    public void addressAdd(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "addressAdd-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLAddressResp.class));
            }
        });
    }

    @Override
    public void addressEdit(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "addressEdit-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLAddressResp.class));
            }
        });
    }

    @Override
    public void addressCompList(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "addressCompList-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLAddressCompList.class));
            }
        });
    }

    @Override
    public void setAddressDefault(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "setAddressDefault-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLAddressResp.class));
            }
        });
    }

    @Override
    public void addressRemove(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "addressRemove-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLAddressRemoveResp.class));
            }
        });
    }

    @Override
    public void hisOrders(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "hisOrders-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLHisOrdersResp.class));
            }
        });
    }

    @Override
    public void myOrders(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "myOrders-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLMyOrdersResp.class));
            }
        });
    }

    @Override
    public void confirmDelivery(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "confirmDelivery-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLConfirmDeliveryResp.class));
            }
        });
    }

    @Override
    public void orderCancel(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "orderCancel-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLOrderCancelResp.class));
            }
        });
    }

    @Override
    public void orderInfo(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "orderInfo-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLOrderInfoResp.class));
            }
        });
    }

    @Override
    public void addToMySupplier(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "addToMySupplier-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLCompanyAddResp.class));
            }
        });
    }

    @Override
    public void orderSubmit(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                GLLog.trace(TAG, "orderSubmit-->response-->" + response);
                super.onSuccess(response, GLJsonToObject.getJsonToModel(response, GLOrderInfoResp.class));
            }
        });
    }

    /**
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////
     * ///////////////////////////////////////下面的私有方法////////////////////////////////////////////////////
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////
     */

    private void request(LinkedHashMap<String, Object> mapParams, String funcKey, ApiJsonHttpResponseHandler jsonHandler) {
        GLLog.trace(TAG, "request-->mapParams-->" + mapParams);
        // 把参数放到json中
        String data = GLJsonToObject.getModeltoJson(mapParams);
        GLLog.trace(TAG, "request-->json data-->" + data);

        String url = getQueryWithUrl(funcKey, mapParams);
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(data.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        GLLog.trace(TAG, "request-->url-->" + url);
        clientPost(mContext, funcKey, url, entity, jsonHandler);
    }

    public void clientPost(final Context context, final String funcKey, final String url, final ByteArrayEntity entity, final ApiJsonHttpResponseHandler apiJsonHttpResponseHandler) {
        client.post(mContext, url, null, entity, GLHttpRequestParams.CONTENT_TYPE, new ApiJsonHttpResponseHandler(funcKey, new GLApiHandler(context)) {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                apiJsonHttpResponseHandler.onSuccess(response);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                apiJsonHttpResponseHandler.onFailure(error, content);
            }
        });
    }

    private String getApiUrl(String key) {
        StringBuilder buffer = new StringBuilder(mUrl);
        if (!mUrl.endsWith("/") && !key.startsWith("/")) {
            buffer.append("/");
        }
        return buffer.append(key).toString();
    }

    private String getQueryWithUrl(String funcKey, LinkedHashMap<String, Object> mapParams) {
        String url = getApiUrl(funcKey);
        GLLog.trace(TAG, "调用接口：" + funcKey + " ， 接口地址：" + url);
        RequestParams params = getRequestParams(mapParams);
        // 重新返回url
        return AsyncHttpClient.getUrlWithQueryString(url, params);
    }

    /**
     * 生成请求签名信息
     *
     * @return RequestParams
     */
    private RequestParams getRequestParams(LinkedHashMap<String, Object> params) {

        if (null == params) {
            params = new LinkedHashMap<>();
        }
        params.put(GLHttpRequestField.APP_KEY, GLHttpRequestParams.APP_KEY);
        params.put(GLHttpRequestField.SECRECT, GLHttpRequestParams.SECRET_KEY);
        params.put(GLHttpRequestField.TIMESTAMP, System.currentTimeMillis());
        params.put(GLHttpRequestField.CLIENT_TYPE, GLHttpRequestParams.ANDROID);

        String token = GLUserManager.getToken(mContext);
        if (GLStringUtil.isNotEmpty(token)) {
            params.put(GLHttpRequestField.TOKEN, token);
        }

        if (params.containsKey(GLHttpRequestField.SECRECT)) {
            params.remove(GLHttpRequestField.SECRECT);
        }

        //signMap 中不包含sign
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // step 2: 把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();

        RequestParams requestParams = new RequestParams();

        for (String key : keys) {
            String value = params.get(key).toString();
            requestParams.put(key, value);

            if (GLHttpRequestField.SIGN.equals(key)
                    || GLHttpRequestField.APP_KEY.equals(key)
                    || GLHttpRequestField.TIMESTAMP.equals(key)) {
                continue;
            }
            if (!GLStringUtil.isEmpty(key) && !GLStringUtil.isEmpty(value)) {
                query.append(key).append(value);
            }
        }
        GLLog.trace(TAG, "sign：" + query);
        String md5Sign = MD5.md5(query.toString() + GLHttpRequestParams.SECRET_KEY);
        GLLog.trace(TAG, "md5 sign：" + md5Sign);

        requestParams.put(GLHttpRequestField.SIGN, md5Sign);

        GLLog.trace(TAG, "http request params：" + requestParams.toString());
        return requestParams;
    }
}
