package cn.caimatou.canting.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.caimatou.canting.R;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.utils.GLNumberUtils;
import cn.caimatou.canting.utils.GLPixelsUtil;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;


/**
 * Description：自定义的主界面下面的tabar
 * <br/><br/>Created by Fausgoal on 2015/5/5.
 * <br/><br/>
 */
public class GLCustomBottomTabar extends LinearLayout implements GLViewClickUtil.NoFastClickListener {

    private int tabarCount = 4;

    private Context mContext = null;

    private ImageView[] mTabIcons = null;
    private TextView[] mTabTitles = null;

    private int[] mIconIds_nor = null;
    private int[] mIconIds_press = null;
    private String[] mTitles = null;

    public void setIconIds_nor(int[] iconIds_nor) {
        mIconIds_nor = iconIds_nor;
    }

    public void setIconIds_press(int[] iconIds_press) {
        mIconIds_press = iconIds_press;
    }

    public void setTitles(String[] titles) {
        mTitles = titles;
    }

    public interface IGLCustomTabarCallback {
        void onClickSelectedItem(int position);

        void getTabCount(int tabarCount, String[] titles);
    }

    private IGLCustomTabarCallback mCallback = null;

    public void setCustomTabarCallback(IGLCustomTabarCallback callback) {
        this.mCallback = callback;
    }

    public GLCustomBottomTabar(Context context) {
        this(context, null, GLConst.NONE);
    }

    public GLCustomBottomTabar(Context context, AttributeSet attrs) {
        this(context, attrs, GLConst.NONE);
    }

    public GLCustomBottomTabar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.glCustomTabarMenu);
        //读取按钮数量
        tabarCount = a.getInt(R.styleable.glCustomTabarMenu_tabarCount, 4);
        a.recycle();
    }

    /**
     * 填充底部tabar
     */
    public void initTabar() {
        mTabIcons = new ImageView[tabarCount];
        mTabTitles = new TextView[tabarCount];

        this.removeAllViews();

        Point point = GLPixelsUtil.getDeviceSize(mContext);
        int w = point.x / tabarCount;

        LayoutParams params = new LayoutParams(w, ViewGroup.LayoutParams.MATCH_PARENT, 1);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        for (int i = GLConst.NONE; i < tabarCount; i++) {
            View tabView = inflater.inflate(R.layout.gl_main_tab_item, null, false);
            tabView.setLayoutParams(params);
            tabView.setId(i);

            ImageView ivIcon = findView(tabView, R.id.ivTabIcon);
            TextView tvTitle = findView(tabView, R.id.tvTabTitle);

            GLViewClickUtil.setNoFastClickListener(tabView, this);

            tabView.setTag(i);
            ivIcon.setTag(i);
            tvTitle.setTag(i);

            ivIcon.setImageResource(mIconIds_nor[i]);
            tvTitle.setText(mTitles[i]);

            mTabIcons[i] = ivIcon;
            mTabTitles[i] = tvTitle;

            this.addView(tabView, i);
        }

        if (null != mCallback) {
            mCallback.getTabCount(tabarCount, mTitles);
        }
    }

    protected <T extends View> T findView(View parent, int id) {
        if (null == parent) {
            throw new NullPointerException("parent is not null!");
        }
        return (T) parent.findViewById(id);
    }

    @Override
    public void onNoFastClick(View v) {
        if (null != v.getTag() &&
                !TextUtils.isEmpty(v.getTag().toString())) {
            resetTabView();
            String strTag = v.getTag().toString();
            int index = GLNumberUtils.stringToInt(strTag);
            setSelectedTabView(index);
            if (null != mCallback) {
                mCallback.onClickSelectedItem(index);
            }
        }
    }

    /**
     * 重置tab状态
     */
    public void resetTabView() {
        if (null == mTabTitles || null == mTabIcons)
            return;

        for (int i = GLConst.NONE; i < mTabTitles.length; i++) {
            mTabTitles[i].setTextColor(GLResourcesUtil.getColor(R.color.gray1));
            mTabIcons[i].setImageResource(mIconIds_nor[i]);
        }
    }

    /**
     * 设置tab选中的状态
     *
     * @param position position
     */
    public void setSelectedTabView(int position) {
        if (null == mTabTitles || null == mTabIcons)
            return;
        if (mTabTitles.length < position || mTabIcons.length < position)
            return;

        mTabTitles[position].setTextColor(GLResourcesUtil.getColor(R.color.green));
        mTabIcons[position].setImageResource(mIconIds_press[position]);
    }
}
