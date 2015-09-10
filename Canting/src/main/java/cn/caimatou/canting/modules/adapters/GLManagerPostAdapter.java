package cn.caimatou.canting.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.caimatou.canting.R;
import cn.caimatou.canting.bean.Post;
import cn.caimatou.canting.callback.IGLOnListItemClickListener;
import cn.caimatou.canting.utils.GLResourcesUtil;

/**
 * Created by Rosicky on 15/9/1.
 */
public class GLManagerPostAdapter extends BaseExpandableListAdapter {
    private final static int POST_BAOCAI = 1;
    private final static int POST_VIEREPORT = 2;

    private ArrayList<Post> list = new ArrayList<Post>();
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> postList = new ArrayList<String>();
    private ArrayList<ArrayList<Integer>> itemPostList = new ArrayList<ArrayList<Integer>>();
    private IGLOnListItemClickListener listener;

    public void addStaffPost(Post post) {
        list.add(post);
        postList.clear();
        itemPostList.clear();
        initData();
        notifyDataSetChanged();
    }

    public GLManagerPostAdapter(Context context, ArrayList<Post> list, IGLOnListItemClickListener listener) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
        initData();
    }

    private void initData() {
        for (int i = 0; i < list.size(); i++) {
            Post post = list.get(i);
            ArrayList<Integer> temp = new ArrayList<Integer>();
            String postName = post.getPostName();
            boolean postBaocai = post.isPostBaocai();
            boolean postViewReport = post.isPostViewReport();
            postList.add(postName);
            if (postBaocai) {
                temp.add(POST_BAOCAI);
            }
            if (postViewReport) {
                temp.add(POST_VIEREPORT);
            }
            itemPostList.add(temp);
        }
    }

    @Override
    public int getGroupCount() {
        return postList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return itemPostList.get(i).size();
    }

    @Override
    public String getGroup(int i) {
        return postList.get(i);
    }

    @Override
    public Integer getChild(int i, int i1) {
        return itemPostList.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        TextView title;
        if (view == null) {
            view = inflater.inflate(R.layout.item_choose_post_group, viewGroup, false);
        }
        title = (TextView) view.findViewById(R.id.choose_post_name);
        title.setText(getGroup(i));
        if (b) {
            ((ImageView) view.findViewById(R.id.choose_post_icon)).setImageDrawable(GLResourcesUtil.getDrawable(R.mipmap.icon_expand_up));
        } else {
            ((ImageView) view.findViewById(R.id.choose_post_icon)).setImageDrawable(GLResourcesUtil.getDrawable(R.mipmap.icon_expand_down));
        }
        view.findViewById(R.id.choose_post_arrow).setVisibility(View.GONE);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        TextView title;
        if(view == null){
            view = inflater.inflate(R.layout.item_choose_post_child, viewGroup, false);
        }
        title = (TextView) view.findViewById(R.id.choose_post_child_name);
        int tag = getChild(i, i1);
        if (tag == POST_BAOCAI) {
            title.setText(GLResourcesUtil.getString(R.string.gl_listVege));
        }
        if (tag == POST_VIEREPORT) {
            title.setText(GLResourcesUtil.getString(R.string.view_report));
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

}
