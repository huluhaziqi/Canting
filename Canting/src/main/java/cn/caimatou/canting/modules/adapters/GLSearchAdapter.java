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
import cn.caimatou.canting.bean.Company;
import cn.caimatou.canting.callback.IGLOnListItemClickListener;
import cn.caimatou.canting.utils.GLViewClickUtil;

/**
 * Description：关键字搜索时显示供应商的适配器
 * <br/><br/>Created by Fausgoal on 15/9/4.
 * <br/><br/>
 */
public class GLSearchAdapter extends GLBaseListAdapter<Company> {

    public GLSearchAdapter(Context context, List<Company> list, IGLOnListItemClickListener listener) {
        super(context, list, listener);
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        GLSearchViewHolder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.item_search_layout, parent, false);
            holder = new GLSearchViewHolder(view, mClickListener);
            view.setTag(holder);
        } else {
            holder = (GLSearchViewHolder) view.getTag();
        }
        holder.setValue(position, getItem(position));
        return view;
    }

    private class GLSearchViewHolder extends GLBaseAdapterViewHolder<Company> {

        private final LinearLayout llSearchItem;
        private final TextView tvSearchProvider;

        public GLSearchViewHolder(View view, IGLOnListItemClickListener listener) {
            super(view, listener);
            llSearchItem = findView(R.id.llSearchItem);
            tvSearchProvider = findView(R.id.tvSearchProvider);

            GLViewClickUtil.setNoFastClickListener(llSearchItem, this);
        }

        @Override
        public void setValue(int position, Company shopInfo) {
            mPosition = position;
            tvSearchProvider.setText(shopInfo.getName());
        }
    }
}
