package cn.caimatou.canting.modules.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentFragment;
import cn.caimatou.canting.bean.GLCaipinModel;
import cn.caimatou.canting.callback.IGLOnListItemClickListener;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLCustomBroadcast;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.manager.GLDialogManager;
import cn.caimatou.canting.modules.activites.GLAddCaipinActivity;
import cn.caimatou.canting.modules.activites.GLCommitOrderActivity;
import cn.caimatou.canting.modules.adapters.GLAddCaiAdapter;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.utils.GLViewClickUtil;

/**
 * Description：订单报菜
 * <br/><br/>Created by Fausgoal on 15/8/30.
 * <br/><br/>
 */
public class GLBaocaiOrderFragment extends GLParentFragment implements IGLOnListItemClickListener {

    private View mView = null;
    private TextView tvVegeCount = null;
    private TextView tvClear = null;
    private TextView tvAddVege = null;
    private TextView tvBaocai = null;
    private ListView lvList = null;
    private GLAddCaiAdapter mAdapter = null;

    /**
     * 供应商id
     */
    private long mProviderId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab_baocai_layout, container, false);
        return mView;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (null != bundle) {
            mProviderId = bundle.getLong(GLConst.INTENT_PARAM);
        }
    }

    @Override
    protected void initView() {
        tvVegeCount = findView(mView, R.id.tvVegeCount);
        tvClear = findView(mView, R.id.tvClear);
        tvAddVege = findView(mView, R.id.tvAddVege);
        tvBaocai = findView(mView, R.id.tvBaocai);
        lvList = findView(mView, R.id.lvList);

        mAdapter = new GLAddCaiAdapter(mContext, null, GLAddCaiAdapter.TYPE_ADD_CAIPIN, this);
        lvList.setAdapter(mAdapter);

        List<GLCaipinModel> caipinModels = new ArrayList<>();
        GLCaipinModel caipinModel = new GLCaipinModel();
        caipinModel.setNum(1);
        caipinModel.setName("青菜1");
        caipinModel.setUnit("斤");
        caipinModels.add(caipinModel);

        caipinModel = new GLCaipinModel();
        caipinModel.setNum(8);
        caipinModel.setName("青菜2");
        caipinModel.setUnit("斤");
        caipinModel.setMemo("要新鲜的要新鲜的要新鲜的要新鲜的要新鲜的要新鲜的要新鲜的要新鲜的要新鲜的");
        caipinModels.add(caipinModel);

        caipinModel = new GLCaipinModel();
        caipinModel.setNum(12);
        caipinModel.setName("青菜3");
        caipinModel.setUnit("斤");
        caipinModels.add(caipinModel);

        mAdapter.append(caipinModels);

        // 就是适配器中存在的条数
        setVegeCount(mAdapter.getCount());
        lvList.setOnCreateContextMenuListener(mMenuListener);

        mAdapter.setLongClickListener(new GLAddCaiAdapter.OnCustomLongClickListener() {
            @Override
            public boolean onCustomLongClick(int position, View view) {
                mLongClickPositon = position;
                return view.showContextMenu();
            }
        });
    }

    @Override
    protected void setListener() {
        GLViewClickUtil.setNoFastClickListener(tvClear, this);
        GLViewClickUtil.setNoFastClickListener(tvAddVege, this);
        GLViewClickUtil.setNoFastClickListener(tvBaocai, this);
    }

    private void setVegeCount(int count) {
        String strVegeCount = GLResourcesUtil.getString(R.string.add_vege_count);
        strVegeCount = String.format(strVegeCount, count);
        // 数量是红色的，就html标签加的，要转换
        CharSequence text = Html.fromHtml(strVegeCount);
        tvVegeCount.setText(text);
    }

    private int mLongClickPositon = GLConst.NEGATIVE;

    private View.OnCreateContextMenuListener mMenuListener = new View.OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            String delete = GLResourcesUtil.getString(R.string.delete);
            if (mLongClickPositon != GLConst.NEGATIVE) {
                GLCaipinModel caipinModel = mAdapter.getItem(mLongClickPositon);
                String title = delete + caipinModel.getName();
                menu.setHeaderView(GLDialogManager.getDialogTitleView(mContext, title));
            }
            menu.add(GLConst.NONE, GLConst.ONE, GLConst.NONE, delete);
        }
    };

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (mLongClickPositon != GLConst.NEGATIVE) {
            GLCaipinModel caipinModel = mAdapter.getItem(mLongClickPositon);
            if (item.getItemId() == GLConst.ONE) {
                mAdapter.remove(mLongClickPositon);
                mAdapter.notifyDataSetInvalidated();
                lvList.setSelection(mLongClickPositon);
                setVegeCount(mAdapter.getCount());
                // Notice 去实现删除操作
                GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.delete_success) + caipinModel.getName());
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onNoFastClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tvClear:
                String msg = GLResourcesUtil.getString(R.string.is_clear_all);
                GLDialogManager.onAffirm(mContext, msg, GLResourcesUtil.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        mAdapter.clear();
                        setVegeCount(mAdapter.getCount());
                        // NOTICE 清空操作
                        GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.cleared_all_caipin));
                    }
                });
                break;
            case R.id.tvAddVege: // 添加菜品
                // 把添加的菜品传过去
                intent = new Intent(mContext, GLAddCaipinActivity.class);
                // 把添加的菜品传过去
                intent.putExtra(GLConst.INTENT_PARAM, getAddCaipin());
                intent.putExtra(GLConst.INTENT_PARAM1, GLConst.NEGATIVE); // -1 表示添加
                GLViewManager.getIns().showActivity(mContext, intent, false);
                break;
            case R.id.tvBaocai:
                intent = new Intent(mContext, GLCommitOrderActivity.class);
                // 把添加的菜品传过去
                intent.putExtra(GLConst.INTENT_PARAM, getAddCaipin());
                intent.putExtra(GLConst.INTENT_PARAM1, mProviderId);
                GLViewManager.getIns().showActivity(mContext, intent, false);
                break;
        }
    }

    private ArrayList<GLCaipinModel> getAddCaipin() {
        List<GLCaipinModel> caipinModels = mAdapter.getList();
        ArrayList<GLCaipinModel> params;
        if (null == caipinModels) {
            params = new ArrayList<>();
        } else {
            params = new ArrayList<>(caipinModels);
        }
        return params;
    }

    @Override
    public void onClickItem(int position, View v) {
        GLCaipinModel caipinModel = mAdapter.getItem(position);
        long count = caipinModel.getNum();
        TextView tvCount;
        switch (v.getId()) {
            case R.id.tvVegeName: // 点击菜名进入编辑
            case R.id.tvNote:
                // 把添加的菜品传过去
                Intent intent = new Intent(mContext, GLAddCaipinActivity.class);
                // 把添加的菜品传过去
                intent.putExtra(GLConst.INTENT_PARAM, getAddCaipin());
                intent.putExtra(GLConst.INTENT_PARAM1, position); // 点击position
                intent.putExtra(GLConst.INTENT_PARAM2, caipinModel);
                GLViewManager.getIns().showActivity(mContext, intent, false);
                break;
            case R.id.ivSubtr: // 减
                if (count <= GLConst.NONE) {
                    count = GLConst.NONE;
                } else {
                    count -= GLConst.ONE;
                }
                caipinModel.setNum(count);
                // 直接给textView设置，不造成listview的滚动
                tvCount = (TextView) v.getTag();
                tvCount.setText(String.valueOf(count));
                tvCount.invalidate();
                break;
            case R.id.ivAdd: // 加
                if (count <= GLConst.NONE) {
                    count = GLConst.NONE;
                }
                count += GLConst.ONE;
                caipinModel.setNum(count);
                // 直接给textView设置，不造成listview的滚动
                tvCount = (TextView) v.getTag();
                tvCount.setText(String.valueOf(count));
                tvCount.invalidate();
                break;
        }
    }

    @Override
    protected IntentFilter getStickyIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(GLCustomBroadcast.ON_ADD_CAIPIN_ACTION);
        return filter;
    }

    @Override
    protected void onStickyNotify(Context context, Intent intent) {
        String action = intent.getAction();
        // 添加菜回来
        if (GLCustomBroadcast.ON_ADD_CAIPIN_ACTION.equals(action)) {
            Bundle bundle = intent.getExtras();
            ArrayList<GLCaipinModel> caipinModels = (ArrayList<GLCaipinModel>) bundle.get(GLConst.INTENT_PARAM);
            mAdapter.clear();
            mAdapter.append(caipinModels);
            setVegeCount(mAdapter.getCount());
        }
    }
}
