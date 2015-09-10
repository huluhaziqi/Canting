package cn.caimatou.canting.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.bean.Company;
import cn.caimatou.canting.bean.GLBaocaiReportModel;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.modules.logic.GLBaocaiLogic;
import cn.caimatou.canting.utils.GLDateUtil;

/**
 * Description：报菜单适配器
 * <br/><br/>Created by Fausgoal on 15/9/4.
 * <br/><br/>
 */
public class GLBaociReportAdapter extends BaseExpandableListAdapter {
    private final Context mContext;
    private final LayoutInflater mInflater;
    private final GLBaocaiLogic mBaocaiLogic;

    private List<GLBaocaiReportModel> mGroups = null;
    private List<List<Company>> mChildList = null;

    public GLBaociReportAdapter(Context context, List<GLBaocaiReportModel> groups, List<List<Company>> childServices) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mBaocaiLogic = new GLBaocaiLogic(mContext);
        mGroups = (null == groups) ? new ArrayList<GLBaocaiReportModel>() : groups;
        mChildList = (null == childServices) ? new ArrayList<List<Company>>() : childServices;
    }

    public void clear() {
        if (null != mGroups) {
            mGroups.clear();
        }
        if (null != mChildList) {
            mChildList.clear();
        }
        notifyDataSetChanged();
    }

    public void appendGroups(List<GLBaocaiReportModel> groups) {
        if (null != groups) {
            mGroups.addAll(groups);
            notifyDataSetChanged();
        }
    }

    public void appendChild(List<List<Company>> childServices) {
        if (null != childServices) {
            mChildList.addAll(childServices);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (null == mChildList) ? 0 : mChildList.get(groupPosition).size();
    }

    @Override
    public GLBaocaiReportModel getGroup(int groupPosition) {
        return (null == mGroups) ? null : mGroups.get(groupPosition);
    }

    @Override
    public Company getChild(int groupPosition, int childPosition) {
        return (null == mChildList) ? null : mChildList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        GroupViewholder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.item_baocai_report_group_latyou, parent, false);
            holder = new GroupViewholder(view);
            view.setTag(holder);
        } else {
            holder = (GroupViewholder) view.getTag();
        }
        holder.setValue(groupPosition, getGroup(groupPosition));
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        ChildViewholder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.item_baocai_report_child_latyou, parent, false);
            holder = new ChildViewholder(view);
            view.setTag(holder);
        } else {
            holder = (ChildViewholder) view.getTag();
        }
        holder.setValue(groupPosition, childPosition, getChild(groupPosition, childPosition));
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupViewholder {
        private final View mView;
        private int mGroupPosition = 0;

        private final TextView tvDay;

        public GroupViewholder(View view) {
            mView = view;

            tvDay = findView(R.id.tvDay);
        }

        public void setValue(int groupPosition, GLBaocaiReportModel group) {
            mGroupPosition = groupPosition;

            String dayAndWeek = GLDateUtil.getDayAndWeek(group.getTime());
            tvDay.setText(dayAndWeek);
        }

        protected <T extends View> T findView(int id) {
            if (null == mView) {
                throw new IllegalArgumentException("view is not null!");
            }
            return (T) mView.findViewById(id);
        }
    }

    private class ChildViewholder {
        private final View mView;
        private int mGroupPosition;
        private int mChildPosition;

        private final TextView tvProviderName;
        private final TextView tvBaocaiCount;
        private final TextView tvState;
        private final View viewBottom;

        public ChildViewholder(View view) {
            mView = view;

            tvProviderName = findView(R.id.tvProviderName);
            tvBaocaiCount = findView(R.id.tvBaocaiCount);
            tvState = findView(R.id.tvState);
            viewBottom = findView(R.id.viewBottom);
        }

        public void setValue(int groupPosition, int childPosition, Company child) {
            mGroupPosition = groupPosition;
            mChildPosition = childPosition;

            tvProviderName.setText(child.getName());
//            tvBaocaiCount.setText(String.format(GLResourcesUtil.getString(R.string.total_cai), child.getCount()));
//            tvState.setText(mBaocaiLogic.getBaocaiState(child.getState()));
//            tvState.setTextColor(mBaocaiLogic.getBaocaiStateColor(child.getState()));

            // 最后一个显示下面的圆角
            if (childPosition == getChildrenCount(groupPosition) - GLConst.ONE) {
                viewBottom.setVisibility(View.VISIBLE);
            } else {
                viewBottom.setVisibility(View.GONE);
            }
        }

        protected <T extends View> T findView(int id) {
            if (null == mView) {
                throw new IllegalArgumentException("view is not null!");
            }
            return (T) mView.findViewById(id);
        }
    }
}
