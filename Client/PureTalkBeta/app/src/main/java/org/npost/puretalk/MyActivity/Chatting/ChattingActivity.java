package org.npost.puretalk.MyActivity.Chatting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.npost.puretalk.GCM.GCMInfo;
import org.npost.puretalk.GCM.RegistrationIntentService;
import org.npost.puretalk.MyActivity.Main.AppRunning;
import org.npost.puretalk.MyView.ChattingView.ChattingListViewAdapter;
import org.npost.puretalk.R;
import org.npost.puretalk.Volley.SingletonRequestQueue;

import java.util.ArrayList;

/**
 * Created by NPOST on 2016-01-10.
 */
public class ChattingActivity extends AppCompatActivity {
    private final String TAG = "ChattingActivity";
    Intent intent;
    TextView chatTextViewHeader;
    ListView listView;
    ChattingListViewAdapter chattingListViewAdapter;
    JSONArray jsonArray;
    EditText inputMessage;
    Button sendMessage;
    RequestQueue requestQueue;
    JSONObject jsonObject;
    String usercellphone;
    String friendcellphone;
    String gcmid;
    String friendname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        AppRunning.appRunning = true;

        intent = getIntent();
        usercellphone = intent.getStringExtra("usercellphone");
        friendcellphone = intent.getStringExtra("friendcellphone");
        gcmid = intent.getStringExtra("gcmid");
        friendname = intent.getStringExtra("friendname");

        chatTextViewHeader = (TextView) findViewById(R.id.chatTextViewHeader);
        chatTextViewHeader.setText(friendname + "님과 대화");

        inputMessage = (EditText) findViewById(R.id.inputMessage);
        sendMessage = (Button) findViewById(R.id.sendMessage);

        /* 싱글턴으로 RequestQueue를 얻어와서 사용 */
        requestQueue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        // 메시지 전송
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = inputMessage.getText().toString();
                if (message.trim().equals("")) {
                    // 공백체크
                    Toast.makeText(getApplicationContext(), "메시지를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    jsonObject = new JSONObject();
                    try {
                    /* message 테이블에 저장될 정보 (이름은 2차개발) */
                        jsonObject.put("message", message);
                        jsonObject.put("usercellphone", usercellphone);
                        jsonObject.put("friendcellphone", friendcellphone);
                        //jsonObject.put("toname", toname);
                        //jsonObject.put("fromname", fromname);
                        jsonObject.put("flag", usercellphone); // 메시지 발송인 번호

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // message DB 저장
                    addMessage(jsonObject);
                    // message GCM 전송
                    SendThread thread = new SendThread(gcmid);
                    thread.start();
                    inputMessage.setText("");
                }
            }
        });

        // JSONObject를 만들어서 앞단에서 생성한 정보를 키로 DB에서 메시지를 조회해온다.
        // init에 getListKey 전달.
        JSONObject getListKey = new JSONObject();
        try {
            getListKey.put("usercellphone", usercellphone);
            getListKey.put("friendcellphone", friendcellphone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        init(getListKey);

    }

    // ChattingListViewAdapter를 생성할 때 JSON데이터를 한번만 제공.
    // 안드로이드에서 인터넷 작업은 스레드로 동작하기 때문에 비동기 작업이다. 인터넷 연결 및 애니메이션은 비동기 작업임에 유의하기.
    // 즉 비동기 작업 결과를 리턴받은 후 결과값을 가지고 후속 작업을 진행하려고 하면, 순서대로 진행되지 않고 리턴이 오기전에 후속작업이 진행된다.
    // 내가 그렇게 작업했었고 NullPointException 발생하는걸 자주 볼 수 있었다.
    public void init(JSONObject getListKey) {
        listView = (ListView) findViewById(R.id.chattingListView);

        String url = "Your Domain put here/puretalk/message";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url,
                getListKey,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // 서버에서 받아온 JSON Object. arrayList key값에 배열이 들어있다.
                            String json = response.getString("arrayList");
                            // 배열을 JSONArray()에 넣어서 JSON배열로 변환한다.
                            jsonArray = new JSONArray(json);
                            // volley 안에서 작업을 끝낸다.
                            chattingListViewAdapter = new ChattingListViewAdapter(getApplicationContext(), jsonArray);
                            listView.setAdapter(chattingListViewAdapter);
                            listView.post(new Runnable() {
                                public void run() {
                                    listView.setSelection(listView.getCount() - 1);
                                }
                            });
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

    public void refresh(JSONObject getListKey) {
        String url = "Your Domain put here/puretalk/message";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url,
                getListKey,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String json = response.getString("arrayList");
                            jsonArray = new JSONArray(json);
                            chattingListViewAdapter.notifyDataSetInvalidated();
                            chattingListViewAdapter.add(jsonArray);
                            chattingListViewAdapter.notifyDataSetChanged();
                            listView.post(new Runnable() {
                                public void run() {
                                    listView.setSelection(listView.getCount() - 1);
                                }
                            });
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

    // 메시지 저장
    public void addMessage(JSONObject message) {
        String url = "Your Domain put here/puretalk/addmessage";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url,
                message,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String json = response.getString("arrayList");
                            jsonArray = new JSONArray(json);
                            chattingListViewAdapter.notifyDataSetInvalidated();
                            chattingListViewAdapter.add(jsonArray);
                            chattingListViewAdapter.notifyDataSetChanged();
                            listView.post(new Runnable() {
                                public void run() {
                                    listView.setSelection(listView.getCount() - 1);
                                }
                            });
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

    class SendThread extends Thread {
        private	int RETRY = 3;
        // Sender 객체는 DoitAndroid 책의 소스코드를 참고했다. 라이브러리 추가 부분 (gcm-server.jar, json-simple-1.1.jar)
        Sender sender = new Sender(GCMInfo.GOOGLE_API_KEY);
        ArrayList<String> idList = new ArrayList<String>();
        String gcmid;

        // gcmid는 리스트를 얻어왔을 때 저장해두었을 것이다.
        public SendThread(String gcmid) {
            this.gcmid = gcmid;
        }

        public void run() {
            try {
                sendText();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        public void sendText() throws Exception {
            try {
                // 푸시 메시지 전송을 위한 메시지 객체 생성 및 환경 설정
                String message = inputMessage.getText().toString();
                Message.Builder gcmMessageBuilder = new Message.Builder();
                gcmMessageBuilder.addData("message", message);
                gcmMessageBuilder.addData("usercellphone", usercellphone);
                gcmMessageBuilder.addData("friendcellphone", friendcellphone);
                gcmMessageBuilder.addData("gcmid", RegistrationIntentService.token);

                Message gcmMessage = gcmMessageBuilder.build();

                // 여러 단말에 메시지 전송 후 결과 확인, idList에는 단말 등록 ID가 List 타입으로 들어있다.
                // GCM 받을 핸드폰 등록ID : gcmid / list형식으로 보낼 수 있다. 여러명에게...
                idList.add(gcmid);
                sender.send(gcmMessage, idList, RETRY);

                // 단말 등록 ID는 한번 등록하면 구글 서버에 저장되어 있다. (유효기간이 있다.)
                // 등록 ID를 DB 회원정보에 저장해두고 메시지 발송시 List에 저장해서 send메소드 파라미터로 전달해주면 된다.

            } catch(Exception ex) {
                ex.printStackTrace();
                //String output = "GCM 메시지 전송 과정에서 에러 발생 : " + ex.toString();
            }
        }
    }

    // GCM : MyGcmListenerService 서비스에서 인텐트 필터를 거쳐서 메시지를 받게 된다.
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent() called.");

        JSONObject getListKey = new JSONObject();
        try {
            // GCM Intent에 사용자 정보를 넣어서 보내준다.
            Intent fromFriend = getIntent();
            String usercellphone = fromFriend.getStringExtra("usercellphone");
            String friendcellphone = fromFriend.getStringExtra("friendcellphone");
            String gcmid = fromFriend.getStringExtra("gcmid");

            getListKey.put("usercellphone", usercellphone);
            getListKey.put("friendcellphone", friendcellphone);
            getListKey.put("gcmid", gcmid);
            Log.d(TAG, usercellphone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        refresh(getListKey);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppRunning.appRunning = false;
    }
}
