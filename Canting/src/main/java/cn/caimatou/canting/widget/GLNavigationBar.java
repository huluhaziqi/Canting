package cn.caimatou.canting.widget;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.caimatou.canting.R;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;


/**
 * Description：封装的自定义导航
 * <br/><br/>Created by Fausgoal on 2015/5/6.
 * <br/><br/>
 */
public class GLNavigationBar implements GLViewClickUtil.NoFastClickListener, Toolbar.OnMenuItemClickListener {

    private static int SET_IMG_AT_RIGHT_OF_TEXT = 1;

    private Activity mActivity = null;

    private Toolbar mToolbar = null;
    private SlidingTabLayout stlNavSlidingTabsLayout = null;

    private ImageView ivNavLogo = null;
    private ImageView ivNavLeft = null;
    private TextView tvNavLeft = null;
    private TextView tvNavRight = null;
    private LinearLayout llNavCenterTitle = null;
    private ImageView ivNavTitleLeftImg = null;
    private TextView tvNavTitle = null;
    private ImageView ivNavTitleRightImg = null;
    private ImageView ivNavRight = null;

    private FrameLayout flRight = null;
    private ImageView ivNavRedPoint = null;

    public GLNavigationBar(Activity activity, Toolbar toolbar) {
        mActivity = activity;
        this.mToolbar = toolbar;

        AppCompatActivity appCompatActivity = ((AppCompatActivity) mActivity);
        appCompatActivity.setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
        }

        ivNavLogo = findView(R.id.ivNavLogo);
        ivNavLeft = findView(R.id.ivNavLeft);
        tvNavLeft = findView(R.id.tvNavLeft);
        tvNavRight = findView(R.id.tvNavRight);
        llNavCenterTitle = findView(R.id.llNavCenterTitle);
        stlNavSlidingTabsLayout = findView(R.id.stlNavSlidingTabsLayout);
        ivNavTitleLeftImg = findView(R.id.ivNavTitleLeftImg);
        tvNavTitle = findView(R.id.tvNavTitle);
        ivNavTitleRightImg = findView(R.id.ivNavTitleRightImg);
        ivNavRight = findView(R.id.ivNavRight);
        flRight = findView(R.id.flRight);
        ivNavRedPoint = findView(R.id.ivNavRedPoint);

        GLViewClickUtil.setNoFastClickListener(tvNavLeft, this);
        GLViewClickUtil.setNoFastClickListener(tvNavRight, this);
        GLViewClickUtil.setNoFastClickListener(llNavCenterTitle, this);
        GLViewClickUtil.setNoFastClickListener(ivNavLeft, this);
        GLViewClickUtil.setNoFastClickListener(ivNavRight, this);
    }

    protected <T extends View> T findView(int id) {
        return (T) mActivity.findViewById(id);
    }

    protected ActionBar getSupportActionBar() {
        return ((AppCompatActivity) mActivity).getSupportActionBar();
    }

    public void setNavTitle(int resId) {
        setNavTitle(resId, GLConst.NONE, GLConst.NONE);
    }

    public void setNavTitle(String title) {
        setNavTitle(title, GLConst.NONE, GLConst.NONE);
    }

    public void setNavTitle(int resId, int drawId, int direction) {
        String text = mActivity.getResources().getString(resId);
        setNavTitle(text, drawId, direction);
    }

    public void setNavTitle(String text, int drawId, int direction) {
        setNavtitleVisible();
        tvNavTitle.setVisibility(View.VISIBLE);
        tvNavTitle.setText(text);
        setNavTitleDrawable(tvNavTitle, drawId, direction);
    }

    private void setNavtitleVisible() {
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        llNavCenterTitle.setLayoutParams(params);
        llNavCenterTitle.setVisibility(View.VISIBLE);
    }

    private void setNavTitleDrawable(TextView tv, int drawId, int direction) {
        Drawable drawable = null;
        if (drawId != GLConst.NONE) {
            drawable = mActivity.getResources().getDrawable(drawId);
            int width = drawable.getMinimumWidth();
            int height = drawable.getMinimumHeight();
            drawable.setBounds(0, 0, width, height);
        }

        if (null != drawable) {
            if (direction == SET_IMG_AT_RIGHT_OF_TEXT) {
                tv.setCompoundDrawables(drawable, null, null, null);
            } else {
                tv.setCompoundDrawables(null, null, drawable, null);
            }
        }
    }

    private void setNavBtnDrawable(TextView tv, int drawId, int direction) {
        Drawable drawable = null;
        if (drawId != GLConst.NONE) {
            drawable = mActivity.getResources().getDrawable(drawId);
            int width = drawable.getMinimumWidth();
            int height = drawable.getMinimumHeight();
            drawable.setBounds(0, 0, width, height);
        }

        if (null != drawable) {
            if (direction == SET_IMG_AT_RIGHT_OF_TEXT) {
                tv.setCompoundDrawables(null, null, drawable, null);
            } else {
                tv.setCompoundDrawables(drawable, null, null, null);
            }
        }
    }

    public void setToolbarVisible(int visible) {
        mToolbar.setVisibility(visible);
    }

    /**
     * 设置标题图片，在title的右边
     *
     * @param resId
     */
    public void setTitleRightImg(int resId) {
        setNavtitleVisible();
        ivNavTitleRightImg.setVisibility(View.VISIBLE);
        ivNavTitleRightImg.setImageResource(resId);
    }

    /**
     * 设置标题图片，在title的左边
     *
     * @param resId
     */
    public void setTitleLeftImg(int resId) {
        setNavtitleVisible();
        ivNavTitleLeftImg.setVisibility(View.VISIBLE);
        ivNavTitleLeftImg.setImageResource(resId);
    }

    private View mCustomView = null;

    /**
     * 中间自定义view
     *
     * @param view
     */
    public void setCenterCustomView(View view) {
        mCustomView = view;
        llNavCenterTitle.setVisibility(View.VISIBLE);
        llNavCenterTitle.addView(view);
    }

    public void setNavLogo(int resId) {
        ivNavLogo.setVisibility(View.VISIBLE);
        ivNavLogo.setImageResource(resId);
    }

    public void setLeftImgIcon(int resId) {
        ivNavLeft.setVisibility(View.VISIBLE);
        ivNavLeft.setImageResource(resId);
    }

    public void setLeftBtn(int resId) {
        setLeftBtn(resId, GLConst.NONE, GLConst.NONE);
    }

    public void setLeftBtn(String text) {
        setLeftBtn(text, GLConst.NONE, GLConst.NONE);
    }

    public void setLeftBtn(int resId, int drawId, int direction) {
        String text = "";
        if (resId != GLConst.NONE) {
            text = mActivity.getResources().getString(resId);
        }
        setLeftBtn(text, drawId, direction);
    }

    public void setLeftBtn(String text, int drawId, int direction) {
        tvNavLeft.setVisibility(View.VISIBLE);
        tvNavLeft.setText(text);
        setNavBtnDrawable(tvNavLeft, drawId, direction);
    }

    public void setRightBtn(int resId) {
        setRightBtn(resId, GLConst.NONE, GLConst.NONE);
    }

    public void setRightBtnImg(int drawId) {
        setRightBtn(GLConst.NONE, drawId, GLConst.NONE);
    }

    public void setRightBtn(String text) {
        setRightBtn(text, GLConst.NONE, GLConst.NONE);
    }

    public void setRightBtn(int resId, int drawId, int direction) {
        String text = "";
        if (resId != GLConst.NONE) {
            text = mActivity.getResources().getString(resId);
        }
        setRightBtn(text, drawId, direction);
    }

    public void setRightBtn(String text, int drawId, int direction) {
        setRightBtnVisibility(View.VISIBLE);
        tvNavRight.setText(text);
        setNavTitleDrawable(tvNavRight, drawId, direction);
    }

    public void setRightBtnVisibility(int visibility) {
        tvNavRight.setVisibility(visibility);
    }

    /**
     * 获取非toolbar默认item的view
     *
     * @param viewId
     * @return
     */
    public View getView(int viewId) {
        return findView(viewId);
    }

    /**
     * 设置tab
     *
     * @param viewPager
     * @param texSize
     * @param textColor
     * @param selectedColor
     * @param backgroundColor
     * @param widthDimenId
     */
    public void setTabs(ViewPager viewPager, int texSize, int textColor, int selectedColor, int backgroundColor, int widthDimenId) {
        stlNavSlidingTabsLayout.setVisibility(View.VISIBLE);

        stlNavSlidingTabsLayout.setDistributeEvenly(true);
        stlNavSlidingTabsLayout.setSelectedIndicatorColors(GLResourcesUtil.getColor(R.color.black));
        stlNavSlidingTabsLayout.setSelectedIndicatorColors(Color.GREEN);
        stlNavSlidingTabsLayout.setTextStyle(texSize, GLResourcesUtil.getColor(textColor), GLResourcesUtil.getColor(selectedColor));
        stlNavSlidingTabsLayout.setBackgroundColor(GLResourcesUtil.getColor(backgroundColor));

        int width = mActivity.getResources().getDimensionPixelSize(widthDimenId);
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(width, Toolbar.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        stlNavSlidingTabsLayout.setLayoutParams(params);

        stlNavSlidingTabsLayout.setViewPager(viewPager);
        stlNavSlidingTabsLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.RED;
            }
        });
    }

    public void setTabUnlineColor(final int unlineColor) {
        stlNavSlidingTabsLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return mActivity.getResources().getColor(unlineColor);
            }
        });
    }

    public void setNavRightIcon(int resId) {
        flRight.setVisibility(View.VISIBLE);
        ivNavRedPoint.setVisibility(View.GONE);
        ivNavRight.setVisibility(View.VISIBLE);
        ivNavRight.setImageResource(resId);
    }

    public void setNavRightIcon(int resId, boolean showRedPoint) {
        setNavRightIcon(resId);
        if (showRedPoint) {
            ivNavRedPoint.setVisibility(View.VISIBLE);
        }
    }

    public void clear() {
        clearToolbarMenu();
        clearRightBtn();
        clearTitle();
        clearTabs();
        clearLeftBtn();
        clearLeftImgIcon();
        clearNavLogo();
        clearNavRightIcon();
    }

    public void clearToolbarMenu() {
        mToolbar.getMenu().clear();
    }

    public void clearLeftImgIcon() {
        ivNavLeft.setVisibility(View.GONE);
    }

    public void clearNavLogo() {
        ivNavLogo.setVisibility(View.GONE);
    }

    public void clearLeftBtn() {
        tvNavLeft.setText("");
        tvNavLeft.setCompoundDrawables(null, null, null, null);
        tvNavLeft.setVisibility(View.GONE);
    }

    public void clearRightBtn() {
        tvNavRight.setText("");
        tvNavRight.setCompoundDrawables(null, null, null, null);
        tvNavRight.setVisibility(View.GONE);
    }

    public void clearTitle() {
        tvNavTitle.setText("");
        tvNavTitle.setCompoundDrawables(null, null, null, null);

        ivNavTitleRightImg.setImageResource(0);
        ivNavTitleRightImg.setVisibility(View.GONE);

        ivNavTitleLeftImg.setImageResource(0);
        ivNavTitleLeftImg.setVisibility(View.GONE);

        if (null != mCustomView) {
            llNavCenterTitle.removeView(mCustomView);
            mCustomView = null;
        }
        llNavCenterTitle.setVisibility(View.GONE);
    }

    private void clearNavRightIcon() {
        flRight.setVisibility(View.GONE);
        ivNavRight.setVisibility(View.GONE);
        ivNavRedPoint.setVisibility(View.GONE);
    }

    public void clearTabs() {
        stlNavSlidingTabsLayout.setVisibility(View.GONE);
    }

    @Override
    public void onNoFastClick(View v) {
        if (null != mCallback)
            mCallback.onViewClick(v.getId());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (null != mCallback)
            return mCallback.onViewClick(item.getItemId());
        return false;
    }

    private OnToolbarMenuItemClickListener mCallback = null;

    public void setMenuitemClickListener(OnToolbarMenuItemClickListener listener) {
        mCallback = listener;
    }

    public interface OnToolbarMenuItemClickListener {
        boolean onViewClick(int viewId);
    }
}
