package cn.caimatou.canting.modules.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.Company;
import cn.caimatou.canting.bean.CustomDate;
import cn.caimatou.canting.callback.IGLOnListItemClickListener;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLCustomBroadcast;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.modules.adapters.GLCalendarViewAdapter;
import cn.caimatou.canting.modules.adapters.GLSearchAdapter;
import cn.caimatou.canting.utils.GLDateUtil;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;
import cn.caimatou.canting.widget.GLCalendarCard;
import cn.caimatou.canting.widget.GLNavigationBar;

/**
 * Description：搜索界面
 * <br/><br/>Created by Fausgoal on 15/9/4.
 * <br/><br/>
 */
public class GLSearchActivity extends GLParentActivity implements IGLOnListItemClickListener, GLCalendarCard.OnCellClickListener {
    public static final int SEARCH_BAOCAI_REPORT = 1;
    public static final int SEARCH_PROVIDER = 2;

    private ImageView ivNavLeft = null;
    private TextView tvNavCancel = null;

    private LinearLayout llNavCenterSearch = null;
    private LinearLayout llNavCenterTitle = null;
    private ImageView ivNavLast = null;
    private TextView tvNavTitle = null;
    private ImageView ivNavNext = null;
    private ImageView ivNavRight = null;
    private EditText etKeyword = null;
    private ImageView ivClear = null;

    private TextView tvCooperationSupplier = null;
    private LinearLayout llProvider = null;
    private ListView lvList = null;
    private GLSearchAdapter mAdapter = null;

    private LinearLayout llCalendar = null;
    private ViewPager vpCalendarPager = null;
    private GLCalendarViewAdapter<GLCalendarCard> mCalendarAdapter = null;

    private int mSearchType = SEARCH_BAOCAI_REPORT;

    private static final int DEFAULT_INDEX = 498;
    private int mCurrentIndex = DEFAULT_INDEX;
    private GLCalendarCard[] mShowViews;
    private SildeDirection mDirection = SildeDirection.NO_SILDE;

    enum SildeDirection {
        RIGHT, LEFT, NO_SILDE;
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mSearchType = bundle.getInt(GLConst.INTENT_PARAM);
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search_layout);

        ivNavLeft = findView(R.id.ivNavLeft);
        tvNavCancel = findView(R.id.tvNavCancel);
        llNavCenterSearch = findView(R.id.llNavCenterSearch);
        llNavCenterTitle = findView(R.id.llNavCenterTitle);
        ivNavLast = findView(R.id.ivNavLast);
        tvNavTitle = findView(R.id.tvNavTitle);
        ivNavNext = findView(R.id.ivNavNext);
        ivNavRight = findView(R.id.ivNavRight);
        etKeyword = findView(R.id.etKeyword);
        ivClear = findView(R.id.ivClear);

        llProvider = findView(R.id.llProvider);
        tvCooperationSupplier = findView(R.id.tvCooperationSupplier);
        lvList = findView(R.id.lvList);

        llCalendar = findView(R.id.llCalendar);
        vpCalendarPager = findView(R.id.vpCalendarPager);

        tvCooperationSupplier.setVisibility(View.GONE);

        mAdapter = new GLSearchAdapter(mContext, null, this);
        lvList.setAdapter(mAdapter);

        initSearch();
        initCalendarData();
    }

    private void initSearch() {
        ivNavLeft.setVisibility(View.VISIBLE);
        llNavCenterSearch.setVisibility(View.VISIBLE);
        llNavCenterTitle.setVisibility(View.GONE);
        tvNavCancel.setVisibility(View.GONE);
        if (mSearchType == SEARCH_BAOCAI_REPORT) {
            ivNavRight.setVisibility(View.VISIBLE);
        } else {
            ivNavRight.setVisibility(View.INVISIBLE);
        }
        llProvider.setVisibility(View.VISIBLE);
        llCalendar.setVisibility(View.GONE);
    }

    private void iniCalendarSearch() {
        ivNavLeft.setVisibility(View.GONE);
        llNavCenterSearch.setVisibility(View.GONE);
        llNavCenterTitle.setVisibility(View.VISIBLE);
        tvNavCancel.setVisibility(View.VISIBLE);
        tvNavCancel.setText(GLResourcesUtil.getString(R.string.cancel));
        ivNavRight.setVisibility(View.INVISIBLE);

        llProvider.setVisibility(View.GONE);
        llCalendar.setVisibility(View.VISIBLE);
    }

    private void initCalendarData() {
        GLCalendarCard[] views = new GLCalendarCard[3];
        for (int i = 0; i < 3; i++) {
            views[i] = new GLCalendarCard(this, this);
        }
        mCalendarAdapter = new GLCalendarViewAdapter<>(views);
        setViewPager();
    }

    private void setViewPager() {
        vpCalendarPager.setAdapter(mCalendarAdapter);
        vpCalendarPager.setCurrentItem(DEFAULT_INDEX);
        vpCalendarPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                measureDirection(position);
                updateCalendarView(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /**
     * 计算方向
     *
     * @param arg0
     */
    private void measureDirection(int arg0) {
        if (arg0 > mCurrentIndex) {
            mDirection = SildeDirection.RIGHT;
        } else if (arg0 < mCurrentIndex) {
            mDirection = SildeDirection.LEFT;
        }
        mCurrentIndex = arg0;
    }

    // 更新日历视图
    private void updateCalendarView(int arg0) {
        mShowViews = mCalendarAdapter.getAllItems();
        if (mDirection == SildeDirection.RIGHT) {
            mShowViews[arg0 % mShowViews.length].rightSlide();
        } else if (mDirection == SildeDirection.LEFT) {
            mShowViews[arg0 % mShowViews.length].leftSlide();
        }
        mDirection = SildeDirection.NO_SILDE;
    }

    @Override
    protected void setListener() {
        GLViewClickUtil.setNoFastClickListener(ivNavLeft, this);
        GLViewClickUtil.setNoFastClickListener(ivNavRight, this);
        GLViewClickUtil.setNoFastClickListener(ivNavLast, this);
        GLViewClickUtil.setNoFastClickListener(ivNavNext, this);
        GLViewClickUtil.setNoFastClickListener(tvNavCancel, this);
        GLViewClickUtil.setNoFastClickListener(ivClear, this);
        GLViewClickUtil.setNoFastClickListener(etKeyword, this);
        etKeyword.addTextChangedListener(mWatcher);
    }

    private void fetchSearchKey(String keyword) {
        List<Company> shopInfos = new ArrayList<>();
        Company shopInfo = new Company();
        shopInfo.setName(keyword + "供应商1");
        shopInfos.add(shopInfo);

        shopInfo.setName(keyword + "供应商2");
        shopInfos.add(shopInfo);

        shopInfo.setName(keyword + "供应商3");
        shopInfos.add(shopInfo);

        shopInfo.setName(keyword + "供应商4");
        shopInfos.add(shopInfo);

        shopInfo.setName(keyword + "供应商5");
        shopInfos.add(shopInfo);

        mAdapter.clear();
        mAdapter.append(shopInfos);
    }

    @Override
    public void onClickItem(int position, View v) {
        // 点击供应商进行搜索
        Company shopInfo = mAdapter.getItem(position);
        Intent intent = new Intent();
        intent.setAction(GLCustomBroadcast.ON_SEARCH_KEYWORD_ACTION);
        intent.putExtra(GLConst.INTENT_PARAM, shopInfo);
        intent.putExtra(GLConst.INTENT_PARAM1, mSearchType);
        sendBroadcast(intent);
        onBack();
    }

    @Override
    public void clickDate(CustomDate date) {
        long time = GLDateUtil.getDay(date.year, date.month, date.day);
        Intent intent = new Intent();
        intent.setAction(GLCustomBroadcast.ON_SEARCH_DATE_ACTION);
        intent.putExtra(GLConst.INTENT_PARAM, time);
        intent.putExtra(GLConst.INTENT_PARAM1, mSearchType);
        sendBroadcast(intent);
        onBack();
    }

    @Override
    public void changeDate(CustomDate date) {
        long time = GLDateUtil.getDay(date.year, date.month, date.day);
        tvNavTitle.setText(GLDateUtil.format(GLDateUtil.YEAR_AND_MONTH_FORMAT, time));
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.ivNavLeft:
                onBack();
                break;
            case R.id.tvNavCancel:
                initSearch();
                break;
            case R.id.ivNavRight:
                iniCalendarSearch();
                break;
            case R.id.ivClear:
                etKeyword.setText("");
                break;
            case R.id.etKeyword:
                String content = etKeyword.getText().toString().trim();
                if (content.length() != GLConst.NONE) {
                    ivClear.setVisibility(View.VISIBLE);
                } else {
                    ivClear.setVisibility(View.GONE);
                }
                break;
            case R.id.ivNavLast:
                vpCalendarPager.setCurrentItem(vpCalendarPager.getCurrentItem() - 1);
                break;
            case R.id.ivNavNext:
                vpCalendarPager.setCurrentItem(vpCalendarPager.getCurrentItem() + 1);
                break;
        }
    }

    @Override
    protected void initToolbarView() {
    }

    @Override
    public boolean hasToolbar() {
        return false;
    }

    @Override
    protected boolean isUseDefaultLayoutToolbar() {
        return false;
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
    }

    private void onBack() {
        GLViewManager.getIns().pop(this);
    }

    @Override
    public boolean onItemSelectedListener(int viewId) {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 如果显示是的日期选择界面，点击返回键，返回到关键字搜索界面
            if (tvNavCancel.getVisibility() == View.VISIBLE) {
                initSearch();
            } else {
                onBack();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
            if (cs.toString().trim().length() <= GLConst.NONE) {
                ivClear.setVisibility(View.GONE);
                tvCooperationSupplier.setVisibility(View.GONE);
                lvList.setVisibility(View.GONE);
                return;
            }

            tvCooperationSupplier.setVisibility(View.VISIBLE);
            lvList.setVisibility(View.VISIBLE);
            ivClear.setVisibility(View.VISIBLE);
            etKeyword.requestFocusFromTouch();
            String keyword = cs.toString().trim();

            // 进行内容检索
            fetchSearchKey(keyword);
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
}
