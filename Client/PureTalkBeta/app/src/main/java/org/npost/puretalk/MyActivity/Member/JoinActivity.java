package org.npost.puretalk.MyActivity.Member;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.npost.puretalk.R;
import org.npost.puretalk.Volley.SingletonRequestQueue;

public class JoinActivity extends AppCompatActivity {
    private final String TAG = "JoinActivity";
    EditText joinIdEditText;
    EditText joinPasswordEditText;
    Button checkIdButton;
    Button joinButton;
    CheckBox agreementCheckBox;
    RequestQueue requestQueue;
    String checkIdResult;
    String checkedId;
    String checkedIdflag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        joinIdEditText = (EditText) findViewById(R.id.joinIdEditText);
        joinPasswordEditText = (EditText) findViewById(R.id.joinPasswordEditText);
        checkIdButton = (Button) findViewById(R.id.checkIdButton);
        joinButton = (Button) findViewById(R.id.joinButton);
        agreementCheckBox = (CheckBox) findViewById(R.id.agreementCheckBox);

        checkIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedId = joinIdEditText.getText().toString();
                // 서버 전송, 아이디 중복체크
                checkId(checkedId, new VolleyCallback() {
                    @Override
                    public void onIdCheckSuccess(String result) {
                        if (result.equals("false")) {
                            Log.d(TAG, result);
                            Toast.makeText(getApplicationContext(), "중복된 아이디입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "사용가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                            joinIdEditText.setEnabled(false);
                            checkedIdflag = result;
                        }
                    }
                    @Override
                    public void onJoinSuccess(String result) {}
                });
            }
        });


        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 서버 전송, 회원가입
                if (checkedIdflag==null) {
                    Toast.makeText(getApplicationContext(), "중복확인을 해주세요", Toast.LENGTH_SHORT).show();
                } else if(!agreementCheckBox.isChecked()) {
                    Toast.makeText(getApplicationContext(), "주소록 이용 동의를 해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    if(!joinPasswordEditText.getText().toString().equals("")) {
                        String password = joinPasswordEditText.getText().toString();
                        JSONObject jsonObject = new JSONObject();
                        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        final String usercellphone = telephonyManager.getLine1Number();
                        try {
                            jsonObject.put("id", checkedId);
                            jsonObject.put("password", password);
                            jsonObject.put("usercellphone", usercellphone);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        join(jsonObject, new VolleyCallback() {
                            @Override
                            public void onIdCheckSuccess(String result) {}
                            @Override
                            public void onJoinSuccess(String result) {
                                if(Integer.parseInt(result) > 0) {
                                    Toast.makeText(getApplicationContext(), "가입완료!", Toast.LENGTH_SHORT).show();

                                    // 모든 액티비에서 사용할 사용자 구분값 저장 - 상대방 GCM 목록 추가 저장.
                                    SharedPreferences userinfo = getSharedPreferences("userinfo", MODE_PRIVATE);
                                    userinfo.edit().putString("id", checkedId).apply();
                                    userinfo.edit().putString("usercellphone", usercellphone).apply();

                                    Intent intent = new Intent(JoinActivity.this, SyncFriendList.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void checkId(String uncheckedId, final VolleyCallback callback) {
        requestQueue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        String url = "Your Domain put here/puretalk/checkId/" + uncheckedId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            checkIdResult = response.getString("checkIdResult");
                            callback.onIdCheckSuccess(checkIdResult);
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

    private void join(JSONObject joinInfo, final VolleyCallback callback) {
        requestQueue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        String url = "Your Domain put here/puretalk/join";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url,
                joinInfo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String memberNumber = response.getString("memberNumber");
                            callback.onJoinSuccess(memberNumber);
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

    // 콜백 인터페이스
    public interface VolleyCallback {
        void onIdCheckSuccess(String result);
        void onJoinSuccess(String result);
    }

}
