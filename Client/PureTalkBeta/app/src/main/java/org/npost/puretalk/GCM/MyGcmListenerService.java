/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.npost.puretalk.GCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import org.npost.puretalk.MyActivity.Chatting.ChattingActivity;
import org.npost.puretalk.MyActivity.Main.AppRunning;
import org.npost.puretalk.MyActivity.Main.MainActivity;
import org.npost.puretalk.R;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    // [START receive_message]
    // 무조건 여기로 들어온다.
    // 그리고 앱 실행중에 새로운 메시지가 들어오면 ChattingActivity에 onNewIntent()가 호출된다.
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        String usercellphone = data.getString("friendcellphone");
        String friendcellphone = data.getString("usercellphone");
        String gcmid = data.getString("gcmid");

        if (AppRunning.appRunning) {
            // Intent를 이용하는 방법
            Intent intent = new Intent(getApplicationContext(), ChattingActivity.class);

            // Intent에 사용자 정보 넣어줘야 한다.
            intent.putExtra("usercellphone", usercellphone);
            intent.putExtra("friendcellphone", friendcellphone);
            intent.putExtra("gcmid", gcmid); // 상대방 gcmid

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            getApplicationContext().startActivity(intent);
        } else {
            sendNotification(message); // 사용자정보 넘겨주기
        }
    }
    // [END receive_message]

    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.btn_stat_notify)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
