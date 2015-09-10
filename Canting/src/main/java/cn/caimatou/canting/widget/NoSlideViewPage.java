package cn.caimatou.canting.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Rosicky on 15/8/30.
 */
public class NoSlideViewPage extends ViewPager {

    private boolean scrollble = false;

    public NoSlideViewPage(Context context) {
        super(context);
    }

    public NoSlideViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollble) {
            return true;
        }
        return super.onTouchEvent(ev);
    }


    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }
}
