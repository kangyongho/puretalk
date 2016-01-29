package org.npost.puretalk.MyActivity.Intro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import org.npost.puretalk.MyActivity.Main.MainActivity;
import org.npost.puretalk.MyActivity.Member.LoginIndexActivity;
import org.npost.puretalk.R;

public class IntroActivity extends Activity {
    private final String TAG = "IntroActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // 로그인 여부에 따라 각기 다른 activity 띄우기
        SharedPreferences userinfo = getSharedPreferences("userinfo", MODE_PRIVATE);
        String id = userinfo.getString("id", "false");

        if (id.equals("false")) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(IntroActivity.this, LoginIndexActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2500);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2500);
        }
    }
}
