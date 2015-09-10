package cn.caimatou.canting.modules.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLBaseAdapterViewHolder;
import cn.caimatou.canting.base.GLBaseListAdapter;
import cn.caimatou.canting.bean.Staff;
import cn.caimatou.canting.callback.IGLOnListItemClickListener;
import cn.caimatou.canting.utils.GLPinyinUtils;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;

/**
 * Created by Rosicky on 15/8/31.
 */
public class GLTelListAdapter extends GLBaseListAdapter<Staff> implements SectionIndexer {

    private String pre = "Z";
    private List<Staff> store = new ArrayList<Staff>();

    public GLTelListAdapter(Context context, List<Staff> list, IGLOnListItemClickListener listener) {
        super(context, list, listener);
        for (int i = 0; i < list.size(); i++) {
            store.add(list.get(i));
        }
    }

    public void filter(String content) {
        if (content == null && content.trim().length() == 0) {
            mList = store;
        } else {
            List<Staff> newList = new ArrayList<Staff>();
            for (int i = 0; i < store.size(); i++) {
                if (store.get(i).getStaffName().startsWith(content.trim())) {
                    newList.add(store.get(i));
                }
            }
            mList.clear();
            mList = newList;
        }
        notifyDataSetChanged();
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        GLStaffViewHolder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.item_tel_list, parent, false);
            holder = new GLStaffViewHolder(view, mClickListener);
            view.setTag(holder);
        } else {
            holder = (GLStaffViewHolder) view.getTag();
        }
        holder.setValue(position, getItem(position));
        return view;
    }

    private class GLStaffViewHolder extends GLBaseAdapterViewHolder<Staff> {

        private final TextView staffLetter;
        private final RelativeLayout staffLinear;
        private final ImageView staffAvater;
        private final TextView staffName;
        private final TextView staffPhone;
        private final TextView staffIsAdd;

        public GLStaffViewHolder(View view, IGLOnListItemClickListener listener) {
            super(view, listener);
            staffLetter = findView(R.id.list_letter);
            staffLinear = findView(R.id.tel_rel);
            staffAvater = findView(R.id.tel_avater);
            staffName = findView(R.id.tel_name);
            staffPhone = findView(R.id.tel_number);
            staffIsAdd = findView(R.id.tel_is_add);
            GLViewClickUtil.setNoFastClickListener(staffLinear, this);
        }

        @Override
        public void setValue(int position, Staff staff) {
            mPosition = position;
            staffLetter.setVisibility(View.VISIBLE);
            staffLetter.setText(GLPinyinUtils.getPinyingFirstChar(staff.getStaffName()).toUpperCase());
            if (position >= 1 && getItem(position) != null) {
                String n = GLPinyinUtils.getPinyingFirstChar(staff.getStaffName()).toUpperCase();
                String p = GLPinyinUtils.getPinyingFirstChar(getItem(position - 1).getStaffName()).toUpperCase();
                if (p.equals(n)) {
                    staffLetter.setVisibility(View.GONE);
                }
            }
//            staffAvater.setImageDrawable();
            staffName.setText(staff.getStaffName());
            staffPhone.setText(staff.getStaffPhone());
            if (staff.isAdd()) {
                staffIsAdd.setText(GLResourcesUtil.getString(R.string.added));
                staffIsAdd.setBackgroundColor(GLResourcesUtil.getColor(R.color.white));
            } else {
                staffIsAdd.setText(GLResourcesUtil.getString(R.string.add));
                staffIsAdd.setBackgroundDrawable(GLResourcesUtil.getDrawable(R.drawable.btn_green_selector));
            }
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < mList.size(); i++) {
            String l = mList.get(i).getStaffName();
            char firstChar = GLPinyinUtils.getPinyingFirstChar(l).toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }
}
