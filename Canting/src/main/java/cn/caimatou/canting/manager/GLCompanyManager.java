package cn.caimatou.canting.manager;

import android.content.Context;
import android.util.Log;

import java.sql.SQLException;
import java.util.List;

import cn.caimatou.canting.bean.Company;
import cn.caimatou.canting.utils.GLListUtil;

/**
 * Created by Rosicky on 15/9/10.
 */
public class GLCompanyManager {
    public GLCompanyManager() {
    }

    public static void saveCompany(Context context, Company company) {
        if (null == company) {
            return;
        }

        Company locCompany = getCompany(context, company.getId());
        if (null != locCompany) {
            company.set_id(locCompany.get_id());
        }
        try {
            GLDatabaseManager.replaceCompany(context, company);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveCompany(Context context, List<Company> companies) {
        if (null == companies) {
            return;
        }

        for (Company company : companies) {
            saveCompany(context, company);
        }
    }

    public static Company getCompany(Context context, long id) {
        try {
            List<Company> companies = (List<Company>) GLDatabaseManager.queryCompany(context, new String[]{"id"}, new Object[]{id});
            if (!GLListUtil.isEmpty(companies)) {
                return companies.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Company> getAllCompany(Context context) {
        try {
            return (List<Company>) GLDatabaseManager.queryCompany(context, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
