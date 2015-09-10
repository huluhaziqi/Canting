package cn.caimatou.canting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.MyListViewAdapter;


/**
 * Created by mac on 15/8/27.
 */
public class OrderListView extends LinearLayout {

    private ListView listView;

    private OnSelectListener mOnSelectListener;
    private MyListViewAdapter myListViewAdapter;
    private ArrayList<String> arrayList = new ArrayList<String>();
    private String message;

    public OrderListView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.order_listview, this, true);
        listView = (ListView) findViewById(R.id.order_list);
        arrayList.add("智能排序");
        arrayList.add("距离最近");
        arrayList.add("评价最好");
        myListViewAdapter = new MyListViewAdapter(context, arrayList);
        listView.setAdapter(myListViewAdapter);
        myListViewAdapter.setOnItemClickListener(new MyListViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                message = arrayList.get(position);
//                myGridViewAdapter.setSelection(position);
//                myGridViewAdapter.notifyDataSetChanged();
                if (mOnSelectListener != null) {
                    mOnSelectListener.getValue(message);
                }
            }
        });
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        public void getValue(String showText);
    }
}
