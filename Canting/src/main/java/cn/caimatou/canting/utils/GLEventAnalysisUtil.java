package cn.caimatou.canting.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import cn.caimatou.canting.BuildConfig;

/**
 * Description：事件统计
 * <br/><br/>Created by Fausgoal on 2015/8/31.
 * <br/><br/>
 */
public class GLEventAnalysisUtil {
    private GLEventAnalysisUtil() {
    }

    public static void onResume(Context context) {
        MobclickAgent.onResume(context);
    }

    public static void onPause(Context context) {
        MobclickAgent.onPause(context);
    }

    public static void onEvent(Context context, String eventId) {
        onEvent(context, eventId, null);
    }

    public static void onEvent(Context ctx, String eventId, Map<String, String> values) {
        if (null == values) {
            values = new HashMap<>();
        }
        values.put("ref", BuildConfig.APP_CHANNEL);
        MobclickAgent.onEvent(ctx, eventId, values);
    }
}
