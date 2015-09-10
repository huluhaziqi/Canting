package cn.caimatou.canting.modules.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentFragment;

/**
 * Description：订单
 * <br/><br/>Created by Fausgoal on 15/8/30.
 * <br/><br/>
 */
public class GLOrderFragment extends GLParentFragment {

    private View mView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_baocai_layout, container, false);
        return mView;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }
}
