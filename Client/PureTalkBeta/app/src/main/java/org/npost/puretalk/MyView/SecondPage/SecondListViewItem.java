package org.npost.puretalk.MyView.SecondPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.npost.puretalk.R;

public class SecondListViewItem extends LinearLayout {

    JSONArray jsonArray;
    JSONObject jsonObjectFromJsonArray;
    int position;
    Context context;
    TextView chattingListViewItem;

    public SecondListViewItem(Context context, JSONArray jsonArray, int position) {
        super(context);

        this.context = context;
        this.jsonArray = jsonArray;
        this.position = position;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.my_second_view_list_item, this, true);

        setViewFromJSONObject(position);
    }

    public void setViewFromJSONObject(int position) {
        String chatting = null;

        try {
            jsonObjectFromJsonArray = jsonArray.getJSONObject(position);
            chatting = jsonObjectFromJsonArray.getString("friendname");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        chattingListViewItem = (TextView) findViewById(R.id.chattingListViewItem);
        chattingListViewItem.setText(chatting);
    }

    public void setTextView(int position) {
        this.position = position;
    }
}
