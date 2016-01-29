package org.npost.puretalk.MyView.SecondPage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.npost.puretalk.MyView.FirstPage.FirstListViewItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondViewListViewAdapter extends BaseAdapter {
    private final String TAG = "SecondAdapter";
    Context context;
    JSONArray jsonArray;
    JSONObject jsonObjectFromJsonArray;

    private List<FirstListViewItem> mItems = new ArrayList<FirstListViewItem>();

    public SecondViewListViewAdapter(Context context) {
        this.context = context;
    }

    public SecondViewListViewAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SecondListViewItem chattingListView;
        if (convertView == null) {
            chattingListView = new SecondListViewItem(context, jsonArray, position);
        } else {
            chattingListView = (SecondListViewItem) convertView;
        }
        chattingListView.setTextView(position);
        return chattingListView;
    }

    @Override
    public Object getItem(int position) {
        Map<String, String> chattinglist = new HashMap<String, String>();
        try {
            jsonObjectFromJsonArray = jsonArray.getJSONObject(position);
            String usercellphone = jsonObjectFromJsonArray.getString("usercellphone");
            String friendcellphone = jsonObjectFromJsonArray.getString("friendcellphone");
            String friendname = jsonObjectFromJsonArray.getString("friendname");

            String gcmid = jsonObjectFromJsonArray.getString("gcmid");

            chattinglist.put("usercellphone", usercellphone);
            chattinglist.put("friendcellphone", friendcellphone);
            chattinglist.put("gcmid", gcmid);
            chattinglist.put("friendname", friendname);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return chattinglist;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
