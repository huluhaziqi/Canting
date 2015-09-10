package cn.caimatou.canting.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Description：不能滚动的ListView
 * <br/><br/>Created by Fausgoal on 15/8/31.
 * <br/><br/>
 */
public class GLNoScrollListView extends ListView {
    public GLNoScrollListView(Context context) {
        this(context, null, 0);
    }

    public GLNoScrollListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GLNoScrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
