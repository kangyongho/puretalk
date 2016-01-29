package org.npost.puretalk.MyActivity.Member;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.npost.puretalk.MyActivity.Main.MainActivity;
import org.npost.puretalk.R;
import org.npost.puretalk.Volley.SingletonRequestQueue;

public class LoginIndexActivity extends AppCompatActivity {
    private final String TAG = "LoginIndexActivity";
    EditText idEditText;
    EditText passwordEditText;
    Button loginButton;
    Button joinLinkButton;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idEditText = (EditText) findViewById(R.id.idEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);
        joinLinkButton = (Button) findViewById(R.id.joinLinkButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그인 프로세스 연결
                final String id = idEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", id);
                    jsonObject.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                login(jsonObject, new VolleyCallback() {
                    @Override
                    public void onLoginSuccess(String result) {
                        String flag = "true";
                        if (flag.equals(result)) {
                            // 사용자 구분값 저장
                            SharedPreferences userinfo = getSharedPreferences("userinfo", MODE_PRIVATE);
                            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                            String userCellPhone = telephonyManager.getLine1Number();
                            userinfo.edit().putString("id", id).apply();
                            userinfo.edit().putString("usercellphone", userCellPhone).apply();

                            // 친구목록으로 이동
                            Intent intent = new Intent(LoginIndexActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "로그인 완료", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "회원정보를 확인해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        joinLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void login(JSONObject joinInfo, final VolleyCallback callback) {
        // 서버로 id, pw 전송
        // Volley
        requestQueue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        String url = "Your Domain put here/puretalk/login";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url,
                joinInfo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String loginresult = response.getString("loginresult");
                            callback.onLoginSuccess(loginresult);
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

    public interface VolleyCallback {
        void onLoginSuccess(String result);
    }
}
