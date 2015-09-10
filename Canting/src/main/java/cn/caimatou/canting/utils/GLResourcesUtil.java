package cn.caimatou.canting.utils;

import android.graphics.drawable.Drawable;
import android.view.View;

import cn.caimatou.canting.modules.GLCantingApp;


/**
 * Description：资源文件工具类
 * <br/><br/>Created by Fausgoal on 2015/8/4.
 * <br/><br/>
 */
public class GLResourcesUtil {
    private GLResourcesUtil() {
    }

    public static String getString(int id) {
        return GLCantingApp.getIns().getApplicationContext().getResources().getString(id);
    }

    public static int getColor(int id) {
        return GLCantingApp.getIns().getApplicationContext().getResources().getColor(id);
    }

    public static Drawable getDrawable(int id) {
        return GLCantingApp.getIns().getApplicationContext().getResources().getDrawable(id);
    }

    protected <T extends View> T findView(View parent, int id) {
        if (null == parent) {
            throw new NullPointerException("parent is not null!");
        }
        return (T) parent.findViewById(id);
    }
}
