package cn.caimatou.canting.callback;


import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 2015/5/5.
 * <br/><br/>
 */
public interface GLToolbarStateCallback {
    boolean hasToolbar();

    void setToolbarStyle(GLNavigationBar navBar);

    boolean onItemSelectedListener(int viewId);
}
