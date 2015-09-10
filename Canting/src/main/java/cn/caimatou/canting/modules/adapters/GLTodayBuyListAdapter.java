package cn.caimatou.canting.modules.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLBaseAdapterViewHolder;
import cn.caimatou.canting.base.GLBaseListAdapter;
import cn.caimatou.canting.bean.BaocaiList;
import cn.caimatou.canting.callback.IGLOnListItemClickListener;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;

/**
 * Created by Rosicky on 15/9/1.
 */
public class GLTodayBuyListAdapter extends GLBaseListAdapter<BaocaiList> {

    public GLTodayBuyListAdapter(Context context, List<BaocaiList> list, IGLOnListItemClickListener listener) {
        super(context, list, listener);
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        GLTodayBuyListViewHolder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.item_baocai_list, parent, false);
            holder = new GLTodayBuyListViewHolder(view, mClickListener);
            view.setTag(holder);
        } else {
            holder = (GLTodayBuyListViewHolder) view.getTag();
        }
        holder.setValue(position, getItem(position));
        return view;
    }

    private class GLTodayBuyListViewHolder extends GLBaseAdapterViewHolder<BaocaiList> {

        private final RelativeLayout relTodayBuyList;
        private final TextView tvProviderName;
        private final TextView tvCount;
        private final TextView tvStatus;

        public GLTodayBuyListViewHolder(View view, IGLOnListItemClickListener listener) {
            super(view, listener);

            relTodayBuyList = findView(R.id.item_baocai_list);
            tvProviderName = findView(R.id.item_list_name);
            tvCount = findView(R.id.item_list_count);
            tvStatus = findView(R.id.item_list_status);

            GLViewClickUtil.setNoFastClickListener(relTodayBuyList, this);
        }

        @Override
        public void setValue(int position, BaocaiList baocaiList) {
            mPosition = position;
            tvProviderName.setText(baocaiList.getShopName());
            int count = baocaiList.getBuyCount();
            String buyCount = "共" + count + "个菜";
            tvCount.setText(buyCount);
            int status = baocaiList.getListStatus();
            if (status == BaocaiList.DONE_LIST) {
                tvStatus.setText(GLResourcesUtil.getString(R.string.done_status));
                tvStatus.setTextColor(GLResourcesUtil.getColor(R.color.green));
            } else if (status == BaocaiList.DOING_LIST) {
                tvStatus.setText(GLResourcesUtil.getString(R.string.doing_status));
                tvStatus.setTextColor(GLResourcesUtil.getColor(R.color.oranger));
            } else if (status == BaocaiList.CANCEL_LIST) {
                tvStatus.setText(GLResourcesUtil.getString(R.string.cancel_status));
                tvStatus.setTextColor(GLResourcesUtil.getColor(R.color.red));
            }
        }
    }
}
