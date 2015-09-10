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

/**
 * Created by linxiaobin on 2015/8/30.
 */
public class MyProviderViewAdapter extends BaseAdapter {
    private List<Company> providerList;
    private Context context;
    private LayoutInflater inflater;

    public MyProviderViewAdapter(List<Company> providerList, Context context) {//constructor
        this.providerList = providerList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setCompanyList(List<Company> list) {
        this.providerList.clear();
        this.providerList = list == null ? new ArrayList<Company>() : list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return providerList.size();
    }

    @Override
    public Company getItem(int i) {
        return providerList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.my_provider_item, null);
        }
        ((TextView) view.findViewById(R.id.my_provider_name)).setText(getItem(i).getName());
        ((TextView) view.findViewById(R.id.main_sell)).setText(getItem(i).getMemo());
//        ((TextView) view.findViewById(R.id.month_bill_num)).setText(String.valueOf(getItem(i).getShopMonthBill()));
        TextView shopAddress = (TextView) view.findViewById(R.id.my_provider_address_context);
        TextView shopPhone = (TextView) view.findViewById(R.id.my_provider_phone_context);
        shopAddress.setText(getItem(i).getAddr());
        shopPhone.setText(getItem(i).getTel());
        return view;
    }
}
