package cn.caimatou.canting.modules.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentFragment;
import cn.caimatou.canting.bean.GLUserInfo;
import cn.caimatou.canting.common.GLCommonVariables;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.manager.GLCommonManager;
import cn.caimatou.canting.modules.activites.GLAptitudeIdentifyActivity;
import cn.caimatou.canting.modules.activites.GLCantingInfoActivity;
import cn.caimatou.canting.modules.activites.GLCertificationUploadActivity;
import cn.caimatou.canting.modules.activites.GLMyProviderActivity;
import cn.caimatou.canting.modules.activites.GLSettingActivity;
import cn.caimatou.canting.modules.activites.GLStaffAccoutManagerActivity;
import cn.caimatou.canting.modules.activites.GLToadyBuyListActivity;
import cn.caimatou.canting.modules.activites.GLUserInfoActivity;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 15/8/30.
 * <br/><br/>
 */
public class GLMineFragment extends GLParentFragment {
    private View mView = null;
    private TextView nameTextView = null;
    private TextView phoneTextView = null;
    private GLUserInfo userInfo = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mine_layout, container, false);
        return mView;
    }

    @Override
    protected void initData() {
        userInfo = (GLUserInfo) GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.USER);
        nameTextView = (TextView) mView.findViewById(R.id.tvInfoOfName);
        nameTextView.setText(userInfo.getNickname().toString());
        phoneTextView = (TextView) mView.findViewById(R.id.tvInfoOfMobileNum);
        phoneTextView.setText(userInfo.getLoginName().toString());

    }

    @Override
    public void onResume() {
        super.onResume();
        userInfo = (GLUserInfo) GLCommonVariables.getIns().readObject(GLCommonVariables.Keys.USER);
        nameTextView.setText(userInfo.getNickname().toString());
        phoneTextView.setText(userInfo.getLoginName().toString());
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfo", userInfo);
        Intent intent = new Intent(mContext, GLUserInfoActivity.class);
        intent.putExtras(bundle);
    }
    @Override
    protected void initView() {
        mView.findViewById(R.id.restaurantInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("userInfo", userInfo);
                Intent intent = new Intent(mContext, GLUserInfoActivity.class);
                intent.putExtras(bundle);
                GLViewManager.getIns().showActivity(GLMineFragment.this.getActivity(), intent, false);
            }
        });
        mView.findViewById(R.id.rlAuthorityManagement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 员工权限本地功能逻辑已实现，暂时不让进入。要用去掉注释则可。
//                GLViewManager.getIns().showActivity(GLMineFragment.this.getActivity(), GLStaffAccoutManagerActivity.class, false);
                GLCommonManager.makeText(mContext, GLResourcesUtil.getString(R.string.temporary_not_dredge_hope));
            }
        });
        mView.findViewById(R.id.rlMyProvider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GLViewManager.getIns().showActivity(GLMineFragment.this.getActivity(), GLMyProviderActivity.class, false);
            }
        });
        mView.findViewById(R.id.relCertification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GLViewManager.getIns().showActivity(GLMineFragment.this.getActivity(), GLAptitudeIdentifyActivity.class, false);
            }
        });
        mView.findViewById(R.id.numInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GLViewManager.getIns().showActivity(GLMineFragment.this.getActivity(), GLToadyBuyListActivity.class, false);
            }
        });
        mView.findViewById(R.id.infoOfRestaurant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GLViewManager.getIns().showActivity(GLMineFragment.this.getActivity(), GLCantingInfoActivity.class, false);
            }
        });
        mView.findViewById(R.id.settingRel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GLViewManager.getIns().showActivity(GLMineFragment.this.getActivity(), GLSettingActivity.class, false);
            }
        });
        mView.findViewById(R.id.rlSharing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare();
            }
        });
        mView.findViewById(R.id.tvCheck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GLViewManager.getIns().showActivity(GLMineFragment.this.getActivity(),GLCertificationUploadActivity.class,false);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            String numOfProvider = data.getStringExtra("numOfProvider");
            ((TextView) mView.findViewById(R.id.tvMyProviderNum)).setText(numOfProvider + "家");
        }
    }

    private void showShare() {
        ShareSDK.initSDK(GLMineFragment.this.getActivity());
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
//        oks.setDialogMode();//如果是对话框模式就写该语句
// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("分享成功我不打死你");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpeg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
// 启动分享GUI
        oks.show(GLMineFragment.this.getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(GLMineFragment.this.getActivity());
    }

}
