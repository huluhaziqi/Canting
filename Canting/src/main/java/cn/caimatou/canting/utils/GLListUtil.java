package cn.caimatou.canting.utils;

import java.util.List;

/**
 * Description：判断List是否为空
 * <br/><br/>Created by Fausgoal on 15/9/5.
 * <br/><br/>
 */
public class GLListUtil {
    public static boolean isEmpty(List<?> list) {
        if (list == null || list.isEmpty())
            return true;
        return false;
    }
}
