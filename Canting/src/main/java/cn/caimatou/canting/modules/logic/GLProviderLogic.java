package cn.caimatou.canting.modules.logic;

import android.content.Context;

import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.bean.Company;
import cn.caimatou.canting.common.GLCommonVariables;
import cn.caimatou.canting.database.GLDbHelper;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.manager.GLCompanyManager;
import cn.caimatou.canting.response.GLCompanyAddResp;
import cn.caimatou.canting.response.GLProviderResp;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.http.GLApiHandler;
import cn.caimatou.canting.utils.http.GLHttpRequestHelper;

/**
 * Created by Rosicky on 15/8/30.
 */
public class GLProviderLogic {

    public static final int REFRESH_SUCCESS = 0;

    public static final int LOADED_SUCCESS = 1;

    private final Context mContext;
    private List<Company> providers;
    private List<Company> myProviders;

    public List<Company> getMyProviders() {
        return myProviders;
    }

    public void setMyProviders(List<Company> myProviders) {
        this.myProviders = myProviders;
    }

    public GLProviderLogic(Context context) {
        mContext = context;
    }

    public List<Company> getProviders() {
        return providers;
    }

    public void setProviders(List<Company> providers) {
        this.providers = providers;
    }

    public void fetch(final Context context) {
        GLHttpRequestHelper.fetchSuppliers(context, new GLApiHandler(context) {
            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                GLProviderResp providerResp = (GLProviderResp) baseInfo;
                List<Company> supplies = providerResp.getResult();
                if (supplies == null || supplies.size() == 0) {
                    GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.provider_empty));
                } else {
                    setProviders(supplies);
                    GLCompanyManager.saveCompany(mContext, supplies);
                }
            }

            @Override
            public void onFailure(GLBaseInfo baseInfo) {
                super.onFailure(baseInfo);
            }
        });
    }

    public void fetchMySuppliers(final Context context) {
        GLHttpRequestHelper.fetchMySuppliers(context, new GLApiHandler(context) {
            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                GLProviderResp providerResp = (GLProviderResp) baseInfo;
                List<Company> supplies = providerResp.getResult();
                if (supplies == null || supplies.size() == 0) {
                    GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.provider_empty));
                } else {
                    setMyProviders(supplies);
                }
            }

            @Override
            public void onFailure(GLBaseInfo baseInfo) {
                super.onFailure(baseInfo);
            }
        });
    }

    public void addToMySupplier(final Context context, long companyId) {
        GLHttpRequestHelper.addToMySupplier(context, companyId, new GLApiHandler(context) {
            @Override
            public void onSuccess(GLBaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                GLCompanyAddResp companyAddResp = (GLCompanyAddResp) baseInfo;
                boolean result = companyAddResp.getResult();
                if (result) {

                } else {

                }
            }

            @Override
            public void onFailure(GLBaseInfo baseInfo) {
                super.onFailure(baseInfo);
            }
        });
    }

}
