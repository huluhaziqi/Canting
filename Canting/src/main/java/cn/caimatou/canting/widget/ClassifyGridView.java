package cn.caimatou.canting.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.MyGridViewAdapter;


/**
 * Created by mac on 15/8/27.
 */
public class ClassifyGridView extends LinearLayout {

    private LineGridView gridView;

    private OnSelectListener mOnSelectListener;
    private ArrayList<String> arrayList = new ArrayList<String>();
    private MyGridViewAdapter myGridViewAdapter;
    private String message;

    public ClassifyGridView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.classify_gridview, this, true);
        gridView = (LineGridView) findViewById(R.id.classify_gridview);
        arrayList.add("全部");
        arrayList.add("生鲜");
        arrayList.add("时令蔬菜");
        arrayList.add("干货");
        arrayList.add("速冻食品");
        arrayList.add("生擒");
        arrayList.add("asdf");
        myGridViewAdapter = new MyGridViewAdapter(arrayList, context);
        gridView.setAdapter(myGridViewAdapter);
        myGridViewAdapter.setOnItemClickListener(new MyGridViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                message = arrayList.get(position);
                myGridViewAdapter.setSelection(position);
                myGridViewAdapter.notifyDataSetChanged();
                if (mOnSelectListener != null) {
                    mOnSelectListener.getValue(message);
                }
            }
        });
    }

    public ClassifyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        public void getValue(String showText);
    }
}
