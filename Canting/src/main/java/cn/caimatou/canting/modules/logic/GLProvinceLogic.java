package cn.caimatou.canting.modules.logic;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.caimatou.canting.bean.CityModel;
import cn.caimatou.canting.bean.DistrictModel;
import cn.caimatou.canting.bean.ProvinceModel;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.manager.GLXmlParserHandler;

/**
 * Description：省、市、区处理类
 * <br/><br/>Created by Fausgoal on 15/9/2.
 * <br/><br/>
 */
public class GLProvinceLogic {
    public String[] mProvinceDatas;
    public Map<String, String[]> mCitisDatasMap = new HashMap<>();
    public Map<String, String[]> mDistrictDatasMap = new HashMap<>();
    public Map<String, String> mZipcodeDatasMap = new HashMap<>();

    public String mCurrentProviceName;
    public String mCurrentCityName;
    public String mCurrentDistrictName = "";
    public String mCurrentZipCode = "";

    public int mSelectedProvinceIndex = GLConst.NEGATIVE;
    public int mSelectedCityIndex = GLConst.NEGATIVE;
    public int mSelectedDistrictIndex = GLConst.NEGATIVE;

    private static GLProvinceLogic ins;

    public static GLProvinceLogic getIns() {
        if (null == ins) {
            synchronized (GLProvinceLogic.class) {
                ins = new GLProvinceLogic();
            }
        }
        return ins;
    }

    private GLProvinceLogic() {
    }

    public void initProvinceDatas(Context context, boolean isSetFirst, String selectedProvince, String selectedCity, String selectedDistrict) {
        List<ProvinceModel> provinceList;
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            GLXmlParserHandler handler = new GLXmlParserHandler();
            parser.parse(input, handler);
            input.close();
            provinceList = handler.getDataList();
            if (isSetFirst && provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                String provinceName = provinceList.get(i).getName();
                mProvinceDatas[i] = provinceName;
                if (!TextUtils.isEmpty(selectedProvince) && selectedProvince.equals(provinceName)) {
                    mSelectedProvinceIndex = i;
                }
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    String city = cityList.get(j).getName();
                    cityNames[j] = city;
                    if (!TextUtils.isEmpty(selectedCity) && selectedCity.equals(city)) {
                        mSelectedCityIndex = j;
                    }
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        String district = districtList.get(k).getName();
                        if (!TextUtils.isEmpty(selectedDistrict) && selectedDistrict.equals(district)) {
                            mSelectedDistrictIndex = k;
                        }

                        DistrictModel districtModel = new DistrictModel(district, districtList.get(k).getZipcode());
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void release() {
        mCurrentProviceName = "";
        mCurrentCityName = "";
        mCurrentDistrictName = "";
        mCurrentZipCode = "";
        mSelectedProvinceIndex = GLConst.NEGATIVE;
        mSelectedCityIndex = GLConst.NEGATIVE;
        mSelectedDistrictIndex = GLConst.NEGATIVE;
        mCitisDatasMap.clear();
        mDistrictDatasMap.clear();
        mZipcodeDatasMap.clear();
        if (null != mProvinceDatas) {
            mProvinceDatas = null;
        }
    }
}
