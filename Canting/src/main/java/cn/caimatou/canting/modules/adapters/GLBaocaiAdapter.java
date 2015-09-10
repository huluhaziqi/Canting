package cn.caimatou.canting.modules.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLBaseAdapterViewHolder;
import cn.caimatou.canting.base.GLBaseListAdapter;
import cn.caimatou.canting.bean.Order;
import cn.caimatou.canting.callback.IGLOnListItemClickListener;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;

/**
 * Description：报菜的适配器
 * <br/><br/>Created by Fausgoal on 15/8/30.
 * <br/><br/>
 */
public class GLBaocaiAdapter extends GLBaseListAdapter<Order> {

    public GLBaocaiAdapter(Context context, List<Order> list, IGLOnListItemClickListener listener) {
        super(context, list, listener);
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        GLBaocaiViewHolder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.item_baocai_layout, parent, false);
            holder = new GLBaocaiViewHolder(view, mClickListener);
            view.setTag(holder);
        } else {
            holder = (GLBaocaiViewHolder) view.getTag();
        }
        holder.setValue(position, getItem(position));
        return view;
    }

    private class GLBaocaiViewHolder extends GLBaseAdapterViewHolder<Order> {

        private final LinearLayout llBaocaiItem;
        private final TextView tvProviderName;
        private final TextView tvKinds;
        private final TextView tvAddress;
        private final TextView tvPhone;
        private final TextView tvBaocaiCount;
        private final TextView tvBaocaiTime;
        private final View vieBottom;

        public GLBaocaiViewHolder(View view, IGLOnListItemClickListener listener) {
            super(view, listener);

            llBaocaiItem = findView(R.id.llBaocaiItem);
            tvProviderName = findView(R.id.tvProviderName);
            tvKinds = findView(R.id.tvKinds);
            tvAddress = findView(R.id.tvAddress);
            tvPhone = findView(R.id.tvPhone);
            tvBaocaiCount = findView(R.id.tvBaocaiCount);
            tvBaocaiTime = findView(R.id.tvBaocaiTime);
            vieBottom = findView(R.id.vieBottom);

            GLViewClickUtil.setNoFastClickListener(llBaocaiItem, this);
        }

        @Override
        public void setValue(int position, Order order) {
            mPosition = position;

            tvProviderName.setText(order.getSupplierCompany());

            String kinds = GLResourcesUtil.getString(R.string.kinds);
            kinds = String.format(kinds, order.getSupplierBusiness());
            tvKinds.setText(kinds);
            tvAddress.setText(order.getSupplierAddr());
            tvPhone.setText(order.getSupplierTel());
            tvBaocaiCount.setText(order.getNum() + "");
            tvBaocaiTime.setText(GLResourcesUtil.getString(R.string.today_baocai));

            if (position == getCount() - GLConst.ONE) {
                vieBottom.setVisibility(View.VISIBLE);
            } else {
                vieBottom.setVisibility(View.GONE);
            }
        }
    }
}
