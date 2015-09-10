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
import cn.caimatou.canting.bean.Staff;
import cn.caimatou.canting.callback.IGLOnListItemClickListener;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;

/**
 * Created by Rosicky on 15/8/31.
 */
public class GLStaffAdapter extends GLBaseListAdapter<Staff> {

    public GLStaffAdapter(Context context, List<Staff> list, IGLOnListItemClickListener listener) {
        super(context, list, listener);
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        GLStaffViewHolder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.item_staff, parent, false);
            holder = new GLStaffViewHolder(view, mClickListener);
            view.setTag(holder);
        } else {
            holder = (GLStaffViewHolder) view.getTag();
        }
        holder.setValue(position, getItem(position));
        return view;
    }

    private class GLStaffViewHolder extends GLBaseAdapterViewHolder<Staff> {

        private final LinearLayout staffLinear;
        private final ImageView staffAvater;
        private final TextView staffName;
        private final TextView staffPost;
        private final TextView staffActive;

        public GLStaffViewHolder(View view, IGLOnListItemClickListener listener) {
            super(view, listener);

            staffLinear = findView(R.id.staff_linear);
            staffAvater = findView(R.id.staff_avater);
            staffName = findView(R.id.staff_name);
            staffPost = findView(R.id.staff_post);
            staffActive = findView(R.id.staff_active);

            GLViewClickUtil.setNoFastClickListener(staffLinear, this);
        }

        @Override
        public void setValue(int position, Staff staff) {
            mPosition = position;
//            staffAvater.setImageDrawable();
            staffName.setText(staff.getStaffName());
            if (staff.getStaffPost() != null) {
                staffPost.setVisibility(View.VISIBLE);
                staffPost.setText(staff.getStaffPost());
                if (!staff.getStaffPost().equals(GLResourcesUtil.getString(R.string.manager))) {
                    staffPost.setBackgroundDrawable(GLResourcesUtil.getDrawable(R.drawable.btn_orange_selector));
                } else {
                    staffPost.setBackgroundDrawable(GLResourcesUtil.getDrawable(R.drawable.btn_green_selector));
                }
            } else {
                staffPost.setVisibility(View.GONE);
            }

            if (staff.isActive()) {
                staffActive.setVisibility(View.GONE);
            } else {
                staffActive.setVisibility(View.VISIBLE);
            }
        }
    }
}
