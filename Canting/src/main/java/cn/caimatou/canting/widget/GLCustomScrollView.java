package cn.caimatou.canting.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Description：禁止ScrollView在子控件的布局改变时自动滚动的方法
 * <br/><br/>Created by Fausgoal on 15/8/28.
 * <br/><br/>
 */
public class GLCustomScrollView extends ScrollView {

    public GLCustomScrollView(Context context) {
        this(context, null, 0);
    }

    public GLCustomScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GLCustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }
}
