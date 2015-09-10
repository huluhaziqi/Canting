package cn.caimatou.canting.modules.adapters;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 15/9/4.
 * <br/><br/>
 */
public class GLCalendarViewAdapter<V extends View> extends PagerAdapter {
    public static final String TAG = "GLCalendarViewAdapter";
    private V[] views;

    public GLCalendarViewAdapter(V[] views) {
        super();
        this.views = views;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (container.getChildCount() == views.length) {
            container.removeView(views[position % views.length]);
        }
        container.addView(views[position % views.length], 0);
        return views[position % views.length];
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) container);
    }

    public V[] getAllItems() {
        return views;
    }
}
