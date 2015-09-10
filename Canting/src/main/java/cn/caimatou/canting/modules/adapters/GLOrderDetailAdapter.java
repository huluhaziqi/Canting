package cn.caimatou.canting.modules.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLBaseAdapterViewHolder;
import cn.caimatou.canting.base.GLBaseListAdapter;
import cn.caimatou.canting.bean.GLCaipinModel;
import cn.caimatou.canting.callback.IGLOnListItemClickListener;
import cn.caimatou.canting.utils.GLStringUtil;

/**
 * Description：报菜单适配器
 * <br/><br/>Created by Fausgoal on 15/8/30.
 * <br/><br/>
 */
public class GLOrderDetailAdapter extends GLBaseListAdapter<GLCaipinModel> {
    public static final int TYPE_SHOW_CAIPIN = 0;
    public static final int TYPE_SHOW_NOTE = 1;

    private final int showCaipinListType;

    public GLOrderDetailAdapter(Context context, List<GLCaipinModel> list, int showCaipinListType, IGLOnListItemClickListener listener) {
        super(context, list, listener);
        this.showCaipinListType = showCaipinListType;
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        GLCaipingViewHoder hoder;
        if (null == view) {
            view = mInflater.inflate(R.layout.item_order_detail_layout, parent, false);
            hoder = new GLCaipingViewHoder(view, mClickListener);
            view.setTag(hoder);
        } else {
            hoder = (GLCaipingViewHoder) view.getTag();
        }
        hoder.setValue(position, getItem(position));
        return view;
    }

    private class GLCaipingViewHoder extends GLBaseAdapterViewHolder<GLCaipinModel> {
        private final TextView tvVegeName;
        private final TextView tvCount;
        private final TextView tvUnit;
        private final TextView tvNote;

        public GLCaipingViewHoder(View view, IGLOnListItemClickListener listener) {
            super(view, listener);

            tvVegeName = findView(R.id.tvVegeName);
            tvCount = findView(R.id.tvCount);
            tvUnit = findView(R.id.tvUnit);
            tvNote = findView(R.id.tvNote);
        }

        @Override
        public void setValue(int position, GLCaipinModel caipinModel) {
            mPosition = position;

            tvVegeName.setText(caipinModel.getName());
            tvCount.setText(caipinModel.getNum() + "");
            tvUnit.setText(caipinModel.getUnit());

            if (GLStringUtil.isEmpty(caipinModel.getMemo())
                    || showCaipinListType == TYPE_SHOW_CAIPIN) {
                tvNote.setVisibility(View.GONE);
            } else {
                tvNote.setText(caipinModel.getMemo());
                tvNote.setVisibility(View.VISIBLE);
            }
        }
    }
}
