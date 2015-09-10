package cn.caimatou.canting.manager;

import cn.caimatou.canting.bean.GLLocation;
import cn.caimatou.canting.common.GLCommonVariables;

/**
 * Description：定位位置管理类
 * <br/><br/>Created by Fausgoal on 15/9/6.
 * <br/><br/>
 */
public class GLLocationManager {

    public static GLLocation getLocation() {
        return (GLLocation) GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.LOCATION);
    }

    public static void saveLocation(GLLocation location) {
        if (null != location) {
            GLCommonVariables.getIns().saveObject(GLCommonVariables.Keys.LOCATION, location);
        }
    }
}
