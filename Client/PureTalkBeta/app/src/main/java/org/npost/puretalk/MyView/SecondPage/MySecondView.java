package org.npost.puretalk.MyView.SecondPage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

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

public class MySecondView extends LinearLayout {
    private final String TAG = "MySecondView";
    Context context;
    ListView chattingListView;
    SecondViewListViewAdapter secondViewListViewAdapter;
    JSONArray jsonArray;
    RequestQueue requestQueue;

    public MySecondView(Context context) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.my_second_view, this, true);

        requestQueue = SingletonRequestQueue.getInstance(this.context).getRequestQueue();

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

    // 채팅리스트 가져오는 메소드
    private void init(JSONObject getListKey) {
        chattingListView = (ListView) findViewById(R.id.chattingListView);

        String url = "Your Domain put here/puretalk/chattinglist";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url,
                // 여기에 구분정보 들어가야 함.
                getListKey,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // 서버에서 받아온 JSON Object. arrayList key값에 배열이 들어있다.
                            // 서버에서 usercellphone, friendcellphone 두개를 같이 넣어서 리스트에 돌려주자.
                            String json = response.getString("arrayList");
                            jsonArray = new JSONArray(json);
                            secondViewListViewAdapter = new SecondViewListViewAdapter(context, jsonArray);
                            chattingListView.setAdapter(secondViewListViewAdapter);
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

    // 메시지 전송용 메소드
    private void setListener() {
        chattingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // postion 값을 이용해서 ListViewAdapter의 아이템에서 정보를 가져온다. getItem 메소드는 ListViewAdapter에 선언되어 있음.
                // ListView 리스트 아이템은 각각 position 값이 key다.
                Map<String, String> chattinglist  = (Map<String, String>) secondViewListViewAdapter.getItem(position);
                // Parcelable 구현 VO객체를 만들서서 인텐트로 정보를 넘기는 방법도 있다. 데이터가 많으면 이용해야한다.
                // 여기선 파싱해서 넘긴다.
                String usercellphone = chattinglist.get("usercellphone");
                String friendcellphone = chattinglist.get("friendcellphone");
                String gcmid = chattinglist.get("gcmid");
                String friendname = chattinglist.get("friendname");

                // 인텐트로 새로운 Activity를 띄우고 데이터를 전달한다.
                Intent intent = new Intent(context, ChattingActivity.class);
                intent.putExtra("usercellphone", usercellphone);
                intent.putExtra("friendcellphone", friendcellphone);
                // 여기서 gcmid도 넘기자.
                intent.putExtra("gcmid", gcmid);
                intent.putExtra("friendname", friendname);

                context.startActivity(intent);
            }
        });
    }

}
