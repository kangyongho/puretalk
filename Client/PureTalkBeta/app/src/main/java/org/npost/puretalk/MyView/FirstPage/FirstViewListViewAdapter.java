package org.npost.puretalk.MyView.FirstPage;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirstViewListViewAdapter extends BaseAdapter {
    private final String TAG = "FirstViewAdapter";
    Context context;
    JSONArray jsonArray;
    JSONObject jsonObjectFromJsonArray;

    private List<FirstListViewItem> mItems = new ArrayList<FirstListViewItem>();

    public FirstViewListViewAdapter(Context context) {
        this.context = context;
    }

    public FirstViewListViewAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FirstListViewItem firendListView;
        if (convertView == null) {
            firendListView = new FirstListViewItem(context, jsonArray, position);
        } else {
            firendListView = (FirstListViewItem) convertView;
        }
        firendListView.setTextView(position);
        return firendListView;
    }

    // ListView 클릭 시 데이터 제공해주는 부분
    // 사용자 객체의 ID 구분 등. 필요한 도메인 객체 호출
    @Override
    public Object getItem(int position) {
        // jsonArray 파싱해서 usercellphone, friendcellphone 정보를 position에 맞게 리턴해준다.
        Map<String, String> chattinglist = new HashMap<String, String>();
        try {
            jsonObjectFromJsonArray = jsonArray.getJSONObject(position);
            Log.d(TAG, ""+jsonObjectFromJsonArray);

            String usercellphone = jsonObjectFromJsonArray.getString("usercellphone");
            String friendcellphone = jsonObjectFromJsonArray.getString("friendcellphone");
            String gcmid = jsonObjectFromJsonArray.getString("gcmid");
            String friendname = jsonObjectFromJsonArray.getString("friendname");

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
