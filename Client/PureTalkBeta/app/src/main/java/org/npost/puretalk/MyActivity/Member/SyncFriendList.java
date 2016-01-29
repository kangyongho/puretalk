package org.npost.puretalk.MyActivity.Member;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.npost.puretalk.MyActivity.Main.MainActivity;
import org.npost.puretalk.R;

import java.util.ArrayList;
import java.util.Map;

public class SyncFriendList extends AppCompatActivity {
    private final String TAG = "SyncFriendList";
    SetFriendList setFriendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syncfriendlist);

        setFriendList = new SetFriendList();
        setFriendList.execute();
    }

    private class SetFriendList extends AsyncTask<Void , Void, Void> {
        ProgressDialog progressDialog = new ProgressDialog(SyncFriendList.this);
        FriendList friendList;

        @Override
        protected void onPreExecute() {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("주소록 동기화 중입니다.");
            // show dialog
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg) {
            friendList = new FriendList(getApplicationContext());
            friendList.getDbNumber(new FriendList.VolleyCallback() {
                @Override
                public void onGetDbNumberSuccess(ArrayList<Map> result) {}
            });
            // doInBackground 작업이 끝나면 Log가 찍힌다. (비동기)
            // progress 퍼센트 표시 안해주면 백그라운드 작업이 끝나고 onPostExecute로 값을 리턴하게 된다.
            Log.d(TAG, "doInBackground 스레드 시작");
            return null;
        }

        @Override
        protected void onCancelled() {
            Intent intent = new Intent(SyncFriendList.this, LoginIndexActivity.class);
            startActivity(intent);
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            Intent intent = new Intent(SyncFriendList.this, MainActivity.class);
            startActivity(intent);
            // 다시 동기화 화면으로 돌아오면 안되므로 finish
            finish();
        }
    }

}
