package org.npost.puretalk.MyView.FirstPage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.npost.puretalk.MyActivity.Chatting.ChattingActivity;
import org.npost.puretalk.R;
import org.npost.puretalk.Volley.SingletonRequestQueue;

import java.util.Map;

public class MyFirstView extends LinearLayout {
    private final String TAG = "MyFirstView";
    Context context;
    TextView textData01;
    ListView friendListView;
    FirstViewListViewAdapter firstViewListViewAdapter;
    JSONArray jsonArray;
    RequestQueue requestQueue;

    public MyFirstView(Context context) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.my_first_view, this, true);
        textData01 = (TextView) findViewById(R.id.textData01);

        requestQueue = SingletonRequestQueue.getInstance(this.context).getRequestQueue();

        // 서버로 보낼 조회 JSONObject 생성 후 init()에 전달.
        SharedPreferences userinfo = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String usercellphone = userinfo.getString("usercellphone", "false");

        JSONObject getListKey = new JSONObject();
        try {
            getListKey.put("usercellphone", usercellphone);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        init(getListKey);
        setListener();
    }

    // 원래 있던 메소드
    public void init(JSONObject getListKey) {
        friendListView = (ListView) findViewById(R.id.firendListView);

        String url = "Your Domain put here/puretalk/friendlist";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url,
                getListKey,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String json = response.getString("arrayList");
                            jsonArray = new JSONArray(json); //이걸 재활용 해야한다!
                            firstViewListViewAdapter = new FirstViewListViewAdapter(context, jsonArray);
                            friendListView.setAdapter(firstViewListViewAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    public void setListener() {
        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> chattinglist  = (Map<String, String>) firstViewListViewAdapter.getItem(position);
                // 여기선 파싱해서 넘긴다.
                String usercellphone = chattinglist.get("usercellphone");
                String friendcellphone = chattinglist.get("friendcellphone");
                String gcmid = chattinglist.get("gcmid");
                String friendname = chattinglist.get("friendname");
                // 인텐트로 새로운 Activity를 띄우고 데이터를 전달한다.
                Intent intent = new Intent(context, ChattingActivity.class);
                intent.putExtra("usercellphone", usercellphone);
                intent.putExtra("friendcellphone", friendcellphone);
                intent.putExtra("gcmid", gcmid);
                intent.putExtra("friendname", friendname);
                context.startActivity(intent);
            }
        });
    }

}
