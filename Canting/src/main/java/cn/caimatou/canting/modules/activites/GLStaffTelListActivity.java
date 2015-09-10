package cn.caimatou.canting.modules.activites;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.base.GLParentActivity;
import cn.caimatou.canting.bean.Staff;
import cn.caimatou.canting.callback.IGLOnListItemClickListener;
import cn.caimatou.canting.common.GLConst;
import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.modules.adapters.GLTelListAdapter;
import cn.caimatou.canting.utils.GLPinyinUtils;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.widget.GLNavigationBar;
import cn.caimatou.canting.widget.IndexBar;
import cn.caimatou.canting.widget.SearchEditText;

/**
 * Created by Rosicky on 15/8/31.
 */
public class GLStaffTelListActivity extends GLParentActivity implements IGLOnListItemClickListener{


    private View mView;
    private ListView listView;
    private IndexBar indexBar;
    private SearchEditText searchEditText;

    private LayoutInflater inflater;
    private List<Staff> telList = new ArrayList<Staff>();
    private GLTelListAdapter adapter;

    @Override
    protected void initData() {
        mContext = this;
        inflater = LayoutInflater.from(mContext);
        getContactList(GLStaffTelListActivity.this);
        Collections.sort(telList, new Comparator<Staff>() {
            @Override
            public int compare(Staff staff, Staff t1) {
                char s1 = GLPinyinUtils.getPinyingFirstChar(staff.getStaffName()).toLowerCase().charAt(0);
                char s2 = GLPinyinUtils.getPinyingFirstChar(t1.getStaffName()).toLowerCase().charAt(0);
                return s1 - s2;
            }
        });
        adapter = new GLTelListAdapter(mContext, telList, this);

    }

    @Override
    protected void initView() {
        mView = inflater.inflate(R.layout.activity_tel_list, null);
        listView = (ListView) mView.findViewById(R.id.activity_tel_list);
        listView.setAdapter(adapter);
        indexBar = (IndexBar) mView.findViewById(R.id.indexBar);
        indexBar.setVisibility(telList.size() == 0 ? View.GONE : View.VISIBLE);
        indexBar.setTextView((TextView) mView.findViewById(R.id.indexToast));
        indexBar.setListView(listView);
        searchEditText = (SearchEditText) mView.findViewById(R.id.search);
        new showContactsBingsToSearch(searchEditText);
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    protected void setListener() {
        super.setListener();
    }

    @Override
    public void setToolbarStyle(GLNavigationBar navBar) {
        navBar.setLeftImgIcon(R.drawable.btn_back_selector);
        navBar.setNavTitle(GLResourcesUtil.getString(R.string.tel_list));
    }

    public void getContactList(Activity activity) {
        ContentResolver cr = activity.getContentResolver();
        String str[] = { Phone.CONTACT_ID, Phone.DISPLAY_NAME, Phone.NUMBER,
                Phone.PHOTO_ID };
        Cursor cur = cr.query(
                Phone.CONTENT_URI, str, null,
                null, null);

        if (cur != null) {
            while (cur.moveToNext()) {
                Staff staff = new Staff();
                staff.setStaffPhone(cur.getString(cur
                        .getColumnIndex(Phone.NUMBER)));// 得到手机号码
                staff.setStaffName(cur.getString(cur
                        .getColumnIndex(Phone.DISPLAY_NAME)));
                // contactsInfo.setContactsPhotoId(cur.getLong(cur.getColumnIndex(Phone.PHOTO_ID)));
                long contactid = cur.getLong(cur
                        .getColumnIndex(Phone.CONTACT_ID));
                long photoid = cur.getLong(cur.getColumnIndex(Phone.PHOTO_ID));
                // 如果photoid 大于0 表示联系人有头像 ，如果没有给此人设置头像则给他一个默认的
                if (photoid > 0) {
                    Uri uri = ContentUris.withAppendedId(
                            ContactsContract.Contacts.CONTENT_URI, contactid);
                    InputStream input = ContactsContract.Contacts
                            .openContactPhotoInputStream(cr, uri);
                    // TODO 有头像时设置头像
                } else {
                    // TODO 无头像时操作
                }
                telList.add(staff);
            }
        }
        cur.close();
    }

    @Override
    public void onClickItem(int position, View v) {
        Staff staff = adapter.getItem(position);
        String[] s = new String[2];
        s[0] = staff.getStaffName();
        s[1] = staff.getStaffPhone();
        Intent intent = new Intent();
        intent.putExtra("staff", s);
        GLStaffTelListActivity.this.setResult(RESULT_OK, intent);
        GLViewManager.getIns().pop(this);
    }

    private void onBack() {
        GLViewManager.getIns().pop(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onItemSelectedListener(int viewId) {
        if (viewId == R.id.ivNavLeft) {
            onBack();
        }
        return super.onItemSelectedListener(viewId);
    }

    private class showContactsBingsToSearch {
        private SearchEditText search = null;
        public showContactsBingsToSearch(SearchEditText searchEditText) {
            search = searchEditText;
            if (null != search)
                search.addTextChangedListener(watcher);
        }

        private TextWatcher watcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null == search)
                    return;

                String content = search.getText().toString();
                adapter = (GLTelListAdapter) listView.getAdapter();
                adapter.filter(content);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

}
