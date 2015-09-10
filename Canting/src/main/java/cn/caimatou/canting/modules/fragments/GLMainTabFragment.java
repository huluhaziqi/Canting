package cn.caimatou.canting.modules.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentFragment;
import cn.caimatou.canting.common.GLConst;


/**
 * Description：
 * <br/><br/>Created by Fausgoal on 2015/5/6.
 * <br/><br/>
 */
public class GLMainTabFragment extends GLParentFragment {
    private static final String TAG = "GLMainTabFragment";

    private static final String KEY_ARG_POSITION = "key-position";

    private int position = GLConst.NEGATIVE;
    private View mView = null;

    public static Fragment newInstance(int position) {
        Fragment f = null;
        switch (position) {
            case 0:
                f = new GLBaocaiFragment();
                break;
            case 1:
                f = new GLHistoryBaocaiFragment();
                break;
            case 2:
                f = new GLProviderFragment();
                break;
            case 3:
                f = new GLMineFragment();
                break;
            default:
                break;
        }
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        position = getArguments().getInt(KEY_ARG_POSITION);
        switch (position) {
            case 0:
                mView = inflater.inflate(R.layout.gl_vege_layout, container, false);
                break;
            case 1:
                mView = inflater.inflate(R.layout.gl_history_vege_layout, container, false);
                break;
            case 2:
                mView = inflater.inflate(R.layout.gl_provider_layout, container, false);
                break;
            case 3:
                mView = inflater.inflate(R.layout.fragment_mine_layout, container, false);
                break;
            default:
                break;
        }
        return mView;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        switch (position) {
            case 2:
                break;
            case 3:
                break;
        }
    }
}
