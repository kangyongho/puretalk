package org.npost.puretalk.MyView.ChattingView;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.npost.puretalk.R;

// JsonChattingListView는 View 생성 책임을 진다.
// ChattingListViewAdapter로 부터 반복 호출을 받는다.
public class JsonChattingListView extends LinearLayout {
    private final String TAG = "JsonChattingListView";
    Context context = null;
    int position;
    LinearLayout chattingListViewLinearLayout;
    TextView chattingListView;
    View BasicViewLeft;
    View BasicViewRight;
    LayoutInflater inflater;
    JSONObject jsonObjectFromJsonArray = null;
    JSONArray jsonArray;
    SharedPreferences userinfo;
    String user;

    public JsonChattingListView(Context context, JSONArray jsonArray, int position) {
        super(context);
        this.context = context;
        this.position = position;
        this.jsonArray = jsonArray;

        userinfo = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        user = userinfo.getString("usercellphone", "false");

        // JSON 파싱 부분
        setViewFromJSONObject(position);
    }

    public void setFlagView(int position) {
        setViewFromJSONObject(position);
    }

    public void setViewFromJSONObject(int position) {
        String flag = null;

        try {
            jsonObjectFromJsonArray = jsonArray.getJSONObject(position);
            flag = jsonObjectFromJsonArray.getString("flag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 전화번호를 이용하여 좌, 우 구분 후 레이아웃 인플레이션
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.chatting_view, this, true);
        chattingListView = (TextView) findViewById(R.id.chattingListView);
        chattingListViewLinearLayout = (LinearLayout) findViewById(R.id.chattingListViewLinearLayout);
        BasicViewLeft = findViewById(R.id.BasicViewLeft);
        BasicViewRight = findViewById(R.id.BasicViewRight);

        batchAndInflation(flag);
    }

    public void batchAndInflation(String flag) {

        if (user.equals(flag)) {
            try {
                String message = jsonObjectFromJsonArray.getString("message");
                chattingListView.setText(message);
                chattingListView.setBackgroundResource(R.drawable.right_bubble);
                chattingListViewLinearLayout.setGravity(Gravity.RIGHT);
                BasicViewLeft.setVisibility(View.GONE);
                BasicViewRight.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // 2차개발 그룹채팅시 상황도 생각해보기.
        } else {
            try {
                String message = jsonObjectFromJsonArray.getString("message");
                chattingListView.setText(message);
                chattingListView.setBackgroundResource(R.drawable.left_bubble);
                chattingListViewLinearLayout.setGravity(Gravity.LEFT);
                BasicViewLeft.setVisibility(View.GONE);
                BasicViewRight.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public String getStringData() {
        return "test Activity";
    }
}
