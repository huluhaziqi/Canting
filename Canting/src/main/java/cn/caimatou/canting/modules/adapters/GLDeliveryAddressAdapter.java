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
import cn.caimatou.canting.bean.GLDeliveryAddressModel;
import cn.caimatou.canting.callback.IGLOnListItemClickListener;
import cn.caimatou.canting.utils.GLViewClickUtil;

/**
 * Description：配送地址适配器
 * <br/><br/>Created by Fausgoal on 15/9/2.
 * <br/><br/>
 */
public class GLDeliveryAddressAdapter extends GLBaseListAdapter<GLDeliveryAddressModel> {
    private final boolean isShowList;

    public GLDeliveryAddressAdapter(Context context, List<GLDeliveryAddressModel> list, boolean isShowList, IGLOnListItemClickListener listener) {
        super(context, list, listener);
        this.isShowList = isShowList;
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        GLDeliveryAddressViewholder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.item_delivery_address_layout, parent, false);
            holder = new GLDeliveryAddressViewholder(view, mClickListener);
            view.setTag(holder);
        } else {
            holder = (GLDeliveryAddressViewholder) view.getTag();
        }
        holder.setValue(position, getItem(position));
        return view;
    }

    private class GLDeliveryAddressViewholder extends GLBaseAdapterViewHolder<GLDeliveryAddressModel> {

        private final LinearLayout llDeliveryAddress;
        private final TextView tvDefault;
        private final TextView tvCantinName;
        private final TextView tvAddress;
        private final ImageView ivIconSelected;
        private final ImageView ivArrow;

        public GLDeliveryAddressViewholder(View view, IGLOnListItemClickListener listener) {
            super(view, listener);

            llDeliveryAddress = findView(R.id.llDeliveryAddress);
            tvDefault = findView(R.id.tvDefault);
            tvCantinName = findView(R.id.tvCantinName);
            tvAddress = findView(R.id.tvAddress);
            ivIconSelected = findView(R.id.ivIconSelected);
            ivArrow = findView(R.id.ivArrow);

            GLViewClickUtil.setNoFastClickListener(llDeliveryAddress, this);
        }

        @Override
        public void setValue(int position, GLDeliveryAddressModel deliveryAddressModel) {
            mPosition = position;

            if (deliveryAddressModel.getIsDefault() == 1) {
                tvDefault.setVisibility(View.VISIBLE);
            } else {
                tvDefault.setVisibility(View.GONE);
            }

            if (deliveryAddressModel.isSelected() && isShowList) {
                ivIconSelected.setVisibility(View.VISIBLE);
                llDeliveryAddress.setBackgroundResource(R.drawable.bg_white1_selector);
            } else {
                ivIconSelected.setVisibility(View.GONE);
                llDeliveryAddress.setBackgroundResource(R.drawable.bg_gray_selector);
            }

            if (!isShowList) {
                ivArrow.setVisibility(View.VISIBLE);
            } else {
                ivArrow.setVisibility(View.GONE);
            }

//            tvCantinName.setText(deliveryAddressModel.getCanTingName());
            tvCantinName.setText("餐厅名称何来？");
            String address = deliveryAddressModel.getAddr() + deliveryAddressModel.getDetailAddr();
            tvAddress.setText(address);
        }
    }
}
