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
import cn.caimatou.canting.bean.GLDeliveryTimeModel;
import cn.caimatou.canting.callback.IGLOnListItemClickListener;
import cn.caimatou.canting.utils.GLViewClickUtil;

/**
 * Description：配送时间适配器
 * <br/><br/>Created by Fausgoal on 15/9/1.
 * <br/><br/>
 */
public class GLDeliveryTimeAdapter extends GLBaseListAdapter<GLDeliveryTimeModel> {
    private final String mSelectedDeliveryTime;

    public GLDeliveryTimeAdapter(Context context, List<GLDeliveryTimeModel> list, String selectedDeliveryTime, IGLOnListItemClickListener listener) {
        super(context, list, listener);
        mSelectedDeliveryTime = selectedDeliveryTime;
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        GlDeliveryTimeViewholder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.item_delivery_time_layout, parent, false);
            holder = new GlDeliveryTimeViewholder(view, mClickListener);
            view.setTag(holder);
        } else {
            holder = (GlDeliveryTimeViewholder) view.getTag();
        }
        holder.setValue(position, getItem(position));
        return view;
    }

    private class GlDeliveryTimeViewholder extends GLBaseAdapterViewHolder<GLDeliveryTimeModel> {

        private final LinearLayout llDeliveryTime;
        private final ImageView ivIconPoint;
        private final TextView tvDeliveryTime;
        private final ImageView ivIconSelected;

        public GlDeliveryTimeViewholder(View view, IGLOnListItemClickListener listener) {
            super(view, listener);

            llDeliveryTime = findView(R.id.llDeliveryTime);
            ivIconPoint = findView(R.id.ivIconPoint);
            tvDeliveryTime = findView(R.id.tvDeliveryTime);
            ivIconSelected = findView(R.id.ivIconSelected);

            GLViewClickUtil.setNoFastClickListener(llDeliveryTime, this);
        }

        @Override
        public void setValue(int position, GLDeliveryTimeModel deliveryTimeModel) {
            mPosition = position;
            // 已选择的显示勾选
            if (mSelectedDeliveryTime.equals(deliveryTimeModel.getTime())) {
                ivIconSelected.setVisibility(View.VISIBLE);
                llDeliveryTime.setBackgroundResource(R.drawable.bg_white1_selector);
            } else {
                ivIconSelected.setVisibility(View.GONE);
                llDeliveryTime.setBackgroundResource(R.drawable.bg_gray_selector);
            }

            tvDeliveryTime.setText(deliveryTimeModel.getTime());

            int resId;
            switch (position) {
                case 0:
                    resId = R.mipmap.icon_today_am;
                    break;
                case 1:
                    resId = R.mipmap.icon_today_pm;
                    break;
                case 2:
                    resId = R.mipmap.icon_next_day_am;
                    break;
                case 3:
                    resId = R.mipmap.icon_next_day_pm;
                    break;
                case 4:
                    resId = R.mipmap.icon_immediately_served;
                    break;
                default:
                    resId = 0;
                    break;
            }
            ivIconPoint.setImageResource(resId);
        }
    }
}
