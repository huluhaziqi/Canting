package cn.caimatou.canting.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.bean.Company;
import cn.caimatou.canting.common.GLConst;

/**
 * Created by Rosicky on 15/8/28.
 */
public class ShopListAdapter extends BaseAdapter {
    private List<Company> shoplist;
    private Context context;
    private LayoutInflater inflater;

    public ShopListAdapter(List<Company> shoplist, Context context) {
        this.shoplist = shoplist;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public List<Company> getShoplist() {
        return shoplist;
    }

    public void clear() {
        if (null != this.shoplist) {
            this.shoplist.clear();
            notifyDataSetChanged();
        }
    }

    public void append(List<Company> companies) {
        if (null != companies && companies.size() > GLConst.NONE && null != this.shoplist) {
            this.shoplist.addAll(companies);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return (shoplist == null) ? 0 : shoplist.size();
    }

    @Override
    public Company getItem(int i) {
        return shoplist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.shoplist_item, null);
        }
        ((TextView) view.findViewById(R.id.shop_name)).setText(getItem(i).getName());
        ((TextView) view.findViewById(R.id.shop_sellcontent)).setText(getItem(i).getMemo());
        ((TextView) view.findViewById(R.id.shop_distance)).setText(getItem(i).getBank());
        TextView shopAddress = (TextView) view.findViewById(R.id.shop_address);
        TextView shopPhone = (TextView) view.findViewById(R.id.shop_phone);
        shopAddress.setText(getItem(i).getAddr());
        shopPhone.setText(getItem(i).getTel());
//        view.findViewById(R.id.shop_address_linear).setVisibility(View.GONE);
//        view.findViewById(R.id.shop_phone_linear).setVisibility(View.GONE);
//        shopPhone.setVisibility(View.GONE);
//        RelativeLayout shopShowMore = (RelativeLayout) view.findViewById(R.id.shoplist_show);
//            shopShowMore.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    LinearLayout showMore = (LinearLayout) view.findViewById(R.id.shoplist_show_more);
//                    LinearLayout showAddress = (LinearLayout) view.findViewById(R.id.shop_address_linear);
//                    LinearLayout showPhone = (LinearLayout) view.findViewById(R.id.shop_phone_linear);
//                    showMore.setVisibility(showMore.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//                    showAddress.setVisibility(showAddress.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//                    showPhone.setVisibility(showPhone.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//                }
//            });
        return view;
    }
}
