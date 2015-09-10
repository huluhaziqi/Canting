package cn.caimatou.canting.widget;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.caimatou.canting.R;
import cn.caimatou.canting.bean.GLCaipinUnitModel;
import cn.caimatou.canting.utils.GLNumberUtils;
import cn.caimatou.canting.utils.GLPixelsUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;

/**
 * Description：单位选择界面
 * <br/><br/>Created by Fausgoal on 15/9/1.
 * <br/><br/>
 */
public class GLCaipinUnitView extends LinearLayout implements GLViewClickUtil.NoFastClickListener {

    protected static final String TAG = "GLCaipinUnitView";

    private static final int TOTAL_COL = 5;// 列
    private int total_row = 3; // 行

    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private int mWidth = 0;
    private Point mPoint = null;

    private OnItemClickListener mListener = null;

    public interface OnItemClickListener {
        void onCellItemClick(GLCaipinUnitModel caipinUnitModel);
    }

    public GLCaipinUnitView(Context context) {
        this(context, null, 0);
    }

    public GLCaipinUnitView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GLCaipinUnitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mPoint = GLPixelsUtil.getDeviceSize(mContext);
        mWidth = mPoint.x / TOTAL_COL;
    }

    private <T extends View> T findView(View view, int id) {
        return (T) view.findViewById(id);
    }

    private Map<Integer, CellView> mMap = null;

    private class CellView {
        public TextView mCellTime = null;
        public LinearLayout llCellItem = null;
        public ImageView mIcon = null;
    }

    private void initView() {
        this.removeAllViews();

        // 绘制tab内容的
        LinearLayout.LayoutParams tabRowParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        tabRowParams.gravity = Gravity.CENTER;

        LinearLayout.LayoutParams tabColumnParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tabColumnParams.gravity = Gravity.CENTER;

        LinearLayout.LayoutParams tabItemParams = new LinearLayout.LayoutParams(mWidth, LayoutParams.WRAP_CONTENT);

        LinearLayout llTabRow = new LinearLayout(mContext);
        llTabRow.setGravity(Gravity.CENTER_VERTICAL);
        llTabRow.setLayoutParams(tabRowParams);
        llTabRow.setOrientation(LinearLayout.VERTICAL);

        mMap = new HashMap<>();
        int positionIndex = 0;

        for (int k = 0; k < total_row; k++) {
            LinearLayout llTabColumn = new LinearLayout(mContext);
            llTabColumn.setGravity(Gravity.CENTER_VERTICAL);
            llTabColumn.setLayoutParams(tabColumnParams);
            llTabColumn.setOrientation(LinearLayout.HORIZONTAL);

            for (int i = 0; i < TOTAL_COL; i++) {
                View view = mInflater.inflate(R.layout.item_caipin_unit_layout, null, false);
                LinearLayout llCellItem = findView(view, R.id.llCellItem);
                LinearLayout llUnitItem = findView(view, R.id.llUnitItem);
                llUnitItem.setLayoutParams(tabItemParams);

                TextView tvUnitItem = findView(view, R.id.tvUnitItem);
                ImageView ivIconSelected = findView(view, R.id.ivIconSelected);

                View view_top_hor_line = findView(view, R.id.view_top_hor_line);
                View view_ver_line = findView(view, R.id.view_ver_line);

                // 将view放在map中，后面填充数据方便
                CellView cellView = new CellView();
                cellView.mCellTime = tvUnitItem;
                cellView.llCellItem = llCellItem;
                cellView.mIcon = ivIconSelected;

                // 计算得到该view的位置
                mMap.put(positionIndex, cellView);
                llCellItem.setTag(positionIndex);
                positionIndex++;
                GLViewClickUtil.setNoFastClickListener(llCellItem, this);

                // 中间行不显示上面横线
                if (k == 0) {
                    view_top_hor_line.setVisibility(View.VISIBLE);
                } else {
                    view_top_hor_line.setVisibility(View.GONE);
                }

                // 如果是最后一列，不显示竖线
                if (i == TOTAL_COL - 1) {
                    view_ver_line.setVisibility(View.GONE);
                } else {
                    view_ver_line.setVisibility(View.VISIBLE);
                }
                llTabColumn.addView(view, i);
            }
            llTabRow.addView(llTabColumn, k);
        }
        this.addView(llTabRow);
    }

    private List<GLCaipinUnitModel> mCaipinUnitModels = null;

    public void setCellData(String strCaipinUnit, List<GLCaipinUnitModel> caipinUnitModels, OnItemClickListener listener) {
        if (null == caipinUnitModels || caipinUnitModels.isEmpty()) {
            return;
        }

        mCaipinUnitModels = caipinUnitModels;

        mListener = listener;

        int size = caipinUnitModels.size();
        total_row = (int) GLNumberUtils.getTotalPageCount(TOTAL_COL, size);
        initView();

        for (int i = 0; i < size; i++) {
            GLCaipinUnitModel caipinUnitModel = caipinUnitModels.get(i);

            String unit = caipinUnitModel.getUnit();
            CellView cellView = mMap.get(i);
            cellView.mCellTime.setText(unit);
            cellView.mCellTime.invalidate();

            if (unit.equals(strCaipinUnit)) {
                cellView.llCellItem.setSelected(true);
                cellView.mIcon.setVisibility(View.VISIBLE);
                cellView.llCellItem.setBackgroundResource(R.drawable.bg_white1_selector);
            } else {
                cellView.llCellItem.setSelected(false);
                cellView.mIcon.setVisibility(View.GONE);
                cellView.llCellItem.setBackgroundResource(R.drawable.bg_gray_selector);
            }
        }
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.llCellItem:
                if (null != v.getTag()) {
                    int pos = (int) v.getTag();
                    if (null != mListener) {
                        mListener.onCellItemClick(mCaipinUnitModels.get(pos));
                    }
                }
                break;
        }
    }
}
