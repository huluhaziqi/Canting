package cn.caimatou.canting.utils.http;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import cn.caimatou.canting.base.GLBaseInfo;
import cn.caimatou.canting.utils.GLLog;

/**
 * Description：fastjson相互转换的类
 * <br/><br/>Created by Fausgoal on 2015/5/17.
 * <br/><br/>
 */
public class GLJsonToObject {

    private static final String TAG = "JsonToObject";

    public static GLBaseInfo getBaseInfo(JSONObject resonse) {
        return getJsonToModel(resonse, GLBaseInfo.class);
    }

    public static GLBaseInfo getJsonToModel(JSONObject resonse, Class<? extends GLBaseInfo> cls) {
        try {
            return JSON.parseObject(resonse.toString(), cls);
        } catch (Exception e) {
            GLLog.trace(TAG, "解析json到实体转换失败！");
            e.printStackTrace();
        }
        return null;
    }

    public static String getModeltoJson(Object obj) {
        try {
            return JSON.toJSONString(obj);
        } catch (Exception e) {
            GLLog.trace(TAG, "实体转换json失败！");
            e.printStackTrace();
        }
        return null;
    }
}
