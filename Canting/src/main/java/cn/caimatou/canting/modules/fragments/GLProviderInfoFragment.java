package cn.caimatou.canting.modules.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentFragment;
import cn.caimatou.canting.bean.Company;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.modules.activites.GLBaocaiOrderActivity;
import cn.caimatou.canting.modules.logic.GLBaocaiLogic;
import cn.caimatou.canting.utils.GLViewClickUtil;

/**
 * Created by Rosicky on 15/9/1.
 */
public class GLProviderInfoFragment extends GLParentFragment {
    private View mView;
    private ListView listView;

    private Company shopInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_vegeinfo, container, false);
        return mView;
    }
    @Override
    protected void initData() {
        shopInfo = (Company) getArguments().getSerializable(GLConst.INTENT_PARAM);
        initShopInfo();
    }

    private void initShopInfo() {
        // TODO GET SHOPINFO
        if (shopInfo == null) {
            shopInfo = new Company();
            shopInfo.setName("都市菜店菜商名称");
            shopInfo.setMemo("主营：蔬菜、生鲜");
            shopInfo.setAddr("益乐新村1区50号1楼2号放");
            shopInfo.setTel("15158823897");
            shopInfo.setBank("满30免费配送，所有水果可以帮您切好洗好，24小时内包退包换，免费为您代购一切");
        }
//        shopInfo.setShopCreditImg("");
    }

    @Override
    protected void initView() {
        listView = (ListView) mView.findViewById(R.id.shopinfo_listview);
        listView.setAdapter(new VegeShopInfoAdapter(GLProviderInfoFragment.this.getActivity(), shopInfo));
    }

    @Override
    protected void setListener() {
        GLViewClickUtil.setNoFastClickListener(mView.findViewById(R.id.requestVege_immediately), this);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.requestVege_immediately:
                Intent intent = new Intent(mContext, GLBaocaiOrderActivity.class);
                intent.putExtra(GLConst.INTENT_PARAM, shopInfo);
                intent.putExtra(GLConst.INTENT_PARAM1, GLBaocaiLogic.ENTER_ORDER_FOR_PROVIDER);
                GLViewManager.getIns().showActivity(mContext, intent, false);
                break;
        }
    }

    private class VegeShopInfoAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private Company shopInfo;
        public VegeShopInfoAdapter(Context context, Company shopInfo) {
            this.context = context;
            this.shopInfo = shopInfo;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (shopInfo.getIsValid() == 1) {
                return 9;
            } else {
                return 8;
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = new View(context);
            switch (position) {
                case 0:
                    view = inflater.inflate(R.layout.shopinfo_title, null);
                    ((TextView) view.findViewById(R.id.shopinfo_name)).setText(shopInfo.getName());
                    ((TextView) view.findViewById(R.id.shopinfo_sellmost)).setText(shopInfo.getMemo());
                    break;
                case 1:
                    view = inflater.inflate(R.layout.shopinfo_common, null);
                    ((ImageView) view.findViewById(R.id.shopinfo_icn)).setImageDrawable(getResources().getDrawable(R.mipmap.address));
                    ((TextView) view.findViewById(R.id.shopinfo_text1)).setText(shopInfo.getAddr());
                    break;
                case 2:
                    view = inflater.inflate(R.layout.shopinfo_common, null);
                    ((ImageView) view.findViewById(R.id.shopinfo_icn)).setImageDrawable(getResources().getDrawable(R.mipmap.phone));
                    ((TextView) view.findViewById(R.id.shopinfo_text1)).setText(shopInfo.getTel());
                    break;
                case 3:
                    view = inflater.inflate(R.layout.shopinfo_common, null);
                    ((ImageView) view.findViewById(R.id.shopinfo_icn)).setImageDrawable(getResources().getDrawable(R.mipmap.send_district));
                    ((TextView) view.findViewById(R.id.shopinfo_text1)).setText(getResources().getString(R.string.shop_send_district));
                    ((TextView) view.findViewById(R.id.shopinfo_text2)).setVisibility(View.VISIBLE);
                    ((TextView) view.findViewById(R.id.shopinfo_text2)).setText(shopInfo.getDetailAddr());
                    break;
                case 4:
                    view = inflater.inflate(R.layout.shopinfo_common, null);
                    ((ImageView) view.findViewById(R.id.shopinfo_icn)).setImageDrawable(getResources().getDrawable(R.mipmap.annonce));
                    ((TextView) view.findViewById(R.id.shopinfo_text1)).setText(shopInfo.getMessage());
                    view.findViewById(R.id.shop_line).setVisibility(View.GONE);
                    break;
                case 5:
                    view = inflater.inflate(R.layout.shop_brief, null);
                    ((TextView) view.findViewById(R.id.shop_intro)).setText(shopInfo.getDetailAddr());
                    break;
                case 6:
                    view = inflater.inflate(R.layout.shop_image, null);
                    ((TextView) view.findViewById(R.id.shop_image_title)).setText(getResources().getString(R.string.shop_identify));
                    // TODO 资质证照
//                    String[] identify_images = shopInfo.getShopCreditImg();
                    HorizontalScrollView identifyScrollView = (HorizontalScrollView) view.findViewById(R.id.shop_image_srollview);
                    LinearLayout identifyScrollViewContainer = (LinearLayout) view.findViewById(R.id.shop_image_container);
                    identifyScrollViewContainer.removeAllViews();
                    identifyScrollView.scrollTo(0, 0);
                    for (int i = 0; i < 10; i++) {
                        ImageView image = (ImageView) inflater.inflate(R.layout.shop_identify_image, null);
                        image.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(155, 155);
                        lp.leftMargin = 24;
                        lp.rightMargin = 24;
                        identifyScrollViewContainer.addView(image, lp);
                    }
                    break;
                case 7:
                    view = inflater.inflate(R.layout.shop_image, null);
                    ((TextView) view.findViewById(R.id.shop_image_title)).setText(getResources().getString(R.string.shop_view));
                    // TODO 店铺实景
//                    String[] view_images = shopInfo.getShopCreditImg();
                    HorizontalScrollView viewScrollView = (HorizontalScrollView) view.findViewById(R.id.shop_image_srollview);
                    LinearLayout scrollViewContainer = (LinearLayout) view.findViewById(R.id.shop_image_container);
                    scrollViewContainer.removeAllViews();
                    viewScrollView.scrollTo(0, 0);
                    for (int i = 0; i < 10; i++) {
                        ImageView image = (ImageView) inflater.inflate(R.layout.shop_identify_image, null);
                        image.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(155, 155);
                        lp.leftMargin = 24;
                        lp.rightMargin = 24;
                        scrollViewContainer.addView(image, lp);
                    }
                    break;
                case 8:
                    view = inflater.inflate(R.layout.shop_identify, null);
                    // TODO 根据是否认证显示不同的图
                    ((ImageView) view.findViewById(R.id.shopinfo_icn)).setImageDrawable(getResources().getDrawable(R.mipmap.identify));
                    ((TextView) view.findViewById(R.id.shopinfo_text1)).setText(getResources().getString(R.string.vegeWharf_identify));
                    break;
            }
            return view;
        }
    }
}
