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
public class MyGridViewAdapter extends BaseAdapter {
    private int clickPos = -1;
    private ArrayList<String> arrayList = new ArrayList<String>();
    private LayoutInflater inflate;
    private Context context;
    private OnItemClickListener mOnItemClickListener;
    private View.OnClickListener onClickListener;

    public MyGridViewAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        inflate = LayoutInflater.from(context);
        init();
    }

    private void init() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, ((ItemViewTag) view.getTag()).getPosition());
                }
            }
        };
    }

    public void setSelection(int position) {
        clickPos = position;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ItemViewTag viewTag;
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.gridview_item, null);
            viewTag = new ItemViewTag((ImageView) convertView.findViewById(R.id.grid_icon), (TextView) convertView.findViewById(R.id.grid_name), position);
            convertView.setTag(viewTag);
        } else {
            viewTag = (ItemViewTag) convertView.getTag();
        }
        viewTag.mName.setText(arrayList.get(position)); // set name
        if (position == clickPos) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.white1));
            convertView.findViewById(R.id.grid_icon).setVisibility(View.VISIBLE);
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.gray2));
            convertView.findViewById(R.id.grid_icon).setVisibility(View.GONE);
        }
        convertView.setOnClickListener(onClickListener);
        return convertView;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ItemViewTag {
        protected ImageView mIcon;
        protected TextView mName;
        private int position;

        public ItemViewTag(ImageView icon, TextView name, int position) {
            this.mName = name;
            this.mIcon = icon;
            this.position = position;
        }

        public int getPosition() {
            return position;
        }
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