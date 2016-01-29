package org.npost.puretalk.MyView.FirstPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.npost.puretalk.R;

public class FirstListViewItem extends LinearLayout {

    JSONArray jsonArray;
    JSONObject jsonObjectFromJsonArray;
    int position;
    Context context;
    TextView friendListItem;

    public FirstListViewItem(Context context, JSONArray jsonArray, int position) {
        super(context);

        this.context = context;
        this.jsonArray = jsonArray;
        this.position = position;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.my_first_view_list_item, this, true);

        setViewFromJSONObject(position);
    }

    public void setViewFromJSONObject(int position) {
        String firends = null;

        try {
            jsonObjectFromJsonArray = jsonArray.getJSONObject(position);
            firends = jsonObjectFromJsonArray.getString("friendname");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        friendListItem = (TextView) findViewById(R.id.friendListItem);
        friendListItem.setText(firends);
    }

    public void setTextView(int position) {
        this.position = position;
    }
}
