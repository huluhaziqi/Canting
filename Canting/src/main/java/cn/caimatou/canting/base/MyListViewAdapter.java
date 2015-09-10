package cn.caimatou.canting.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.caimatou.canting.R;


/**
 * Created by mac on 15/8/27.
 */
public class MyListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> arrayList;
    private LayoutInflater inflate;
    private OnItemClickListener mOnItemClickListener;
    private View.OnClickListener onClickListener;

    public MyListViewAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        inflate = LayoutInflater.from(context);
        init();
    }

    private void init() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, (Integer) view.getTag());
                }
            }
        };
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public String getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflate.inflate(R.layout.orderlist_item, null);
            ((TextView) view.findViewById(R.id.order_item_name)).setText(getItem(i));
        }
        switch (i) {
            case 0:
                ((ImageView) view.findViewById(R.id.order_item_icon)).setImageResource(R.mipmap.intelli_order);
                break;
            case 1:
                ((ImageView) view.findViewById(R.id.order_item_icon)).setImageResource(R.mipmap.distance_order);
                break;
            case 2:
                ((ImageView) view.findViewById(R.id.order_item_icon)).setImageResource(R.mipmap.distance_order);
                break;
        }
        if (i == getCount() - 1) {
            view.findViewById(R.id.order_list_line).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.order_list_line).setVisibility(View.VISIBLE);
        }
        view.setTag(i);
        view.setOnClickListener(onClickListener);
        return view;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    /**
     * 重新定义菜单选项单击接口
     */
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}
