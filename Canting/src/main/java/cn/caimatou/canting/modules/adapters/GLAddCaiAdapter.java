package cn.caimatou.canting.modules.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLBaseAdapterViewHolder;
import cn.caimatou.canting.base.GLBaseListAdapter;
import cn.caimatou.canting.bean.GLCaipinModel;
import cn.caimatou.canting.callback.IGLOnListItemClickListener;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.utils.GLStringUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;

/**
 * Description：报菜，显示添加菜的适配器
 * <br/><br/>Created by Fausgoal on 15/8/30.
 * <br/><br/>
 */
public class GLAddCaiAdapter extends GLBaseListAdapter<GLCaipinModel> {
    public static final int TYPE_ADD_CAIPIN = 0;
    public static final int TYPE_SHOW_CAIPIN = 1;
    public static final int TYPE_SHOW_SHOPPING_CART = 2;

    private final int showCaipinListType;

    public GLAddCaiAdapter(Context context, List<GLCaipinModel> list, int showCaipinListType, IGLOnListItemClickListener listener) {
        super(context, list, listener);
        this.showCaipinListType = showCaipinListType;
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        GLCaipingViewHoder hoder;
        if (null == view) {
            view = mInflater.inflate(R.layout.item_add_cai_layout, parent, false);
            hoder = new GLCaipingViewHoder(view, mClickListener);
            view.setTag(hoder);
        } else {
            hoder = (GLCaipingViewHoder) view.getTag();
        }
        hoder.setValue(position, getItem(position));
        return view;
    }

    private class GLCaipingViewHoder extends GLBaseAdapterViewHolder<GLCaipinModel> {
        private final LinearLayout llCaipinItem;
        private final TextView tvVegeName;
        private final ImageView ivSubtr;
        private final TextView tvCount;
        private final TextView tvUnit;
        private final ImageView ivAdd;
        private final TextView tvNote;
        private final View viewTopline;
        private final View viewBottom;

        public GLCaipingViewHoder(View view, IGLOnListItemClickListener listener) {
            super(view, listener);

            llCaipinItem = findView(R.id.llCaipinItem);
            tvVegeName = findView(R.id.tvVegeName);
            ivSubtr = findView(R.id.ivSubtr);
            tvCount = findView(R.id.tvCount);
            tvUnit = findView(R.id.tvUnit);
            ivAdd = findView(R.id.ivAdd);
            tvNote = findView(R.id.tvNote);
            viewTopline = findView(R.id.viewTopline);
            viewBottom = findView(R.id.viewBottom);

            GLViewClickUtil.setNoFastClickListener(tvVegeName, this);
            GLViewClickUtil.setNoFastClickListener(tvNote, this);
            GLViewClickUtil.setNoFastClickListener(ivSubtr, this);
            GLViewClickUtil.setNoFastClickListener(ivAdd, this);
            tvVegeName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return null != mOnLongClickListener && mOnLongClickListener.onCustomLongClick(mPosition, tvVegeName);
                }
            });
            tvNote.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return null != mOnLongClickListener && mOnLongClickListener.onCustomLongClick(mPosition, tvVegeName);
                }
            });
        }

        @Override
        public void setValue(int position, GLCaipinModel caipinModel) {
            mPosition = position;

            tvVegeName.setTag(position);
            tvNote.setTag(position);

            tvVegeName.setText(caipinModel.getName());
            tvCount.setText(caipinModel.getNum() + "");
            tvUnit.setText(caipinModel.getUnit());

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llCaipinItem.getLayoutParams();
            if (GLStringUtil.isEmpty(caipinModel.getMemo())
                    || showCaipinListType == TYPE_SHOW_SHOPPING_CART) {
                tvNote.setVisibility(View.GONE);
                params.bottomMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.dim_30px);
            } else {
                params.bottomMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.dim_16px);
                tvNote.setText(caipinModel.getMemo());
                tvNote.setVisibility(View.VISIBLE);
            }
            llCaipinItem.setLayoutParams(params);

            ivSubtr.setTag(tvCount);
            ivAdd.setTag(tvCount);

            if (showCaipinListType == TYPE_ADD_CAIPIN) {
                ivSubtr.setVisibility(View.VISIBLE);
                ivAdd.setVisibility(View.VISIBLE);
            } else {
                ivSubtr.setVisibility(View.GONE);
                ivAdd.setVisibility(View.GONE);
            }

            if (position == GLConst.NONE) {
                viewTopline.setVisibility(View.GONE);
                viewBottom.setVisibility(View.GONE);
            } else {
                viewTopline.setVisibility(View.VISIBLE);
                if (position == getCount() - GLConst.ONE) {
                    viewBottom.setVisibility(View.VISIBLE);
                } else {
                    viewBottom.setVisibility(View.GONE);
                }
            }
        }
    }

    private OnCustomLongClickListener mOnLongClickListener = null;

    public void setLongClickListener(OnCustomLongClickListener l) {
        mOnLongClickListener = l;
    }

    public interface OnCustomLongClickListener {
        boolean onCustomLongClick(int position, View view);
    }
}
