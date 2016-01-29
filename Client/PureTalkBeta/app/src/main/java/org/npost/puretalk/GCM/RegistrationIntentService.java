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

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;
import org.npost.puretalk.Volley.SingletonRequestQueue;

public class RegistrationIntentService extends IntentService {

    public static String token;
    public static InstanceID instanceID;
    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // [START register_for_gcm]
            // [START get_token] GCM ID 받아온다.
            instanceID = InstanceID.getInstance(this);
            token = instanceID.getToken(GCMInfo.PROJECT_ID, "GCM", null);
            // [END get_token]

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(token);
            // 서버에 token을 매번 업데이트 해줘야 한다.
            // 메시지 전송 시 회원정보에 등록되어 있는 token을 가져와서 ChattingActivity에서 전송한다.

            /* GCM 저장 */
            // 서버 전송을 하는 부분
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]

        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(String token) {
        // token을 서버에 저장 (DB GCM 업데이트)
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String userCellPhone = telephonyManager.getLine1Number();

        gcmUpdate(userCellPhone, token);
    }

    private void gcmUpdate(String userCellPhone, String GCM) {
        JSONObject gcmInfo = new JSONObject();
        try {
            gcmInfo.put("usercellphone", userCellPhone);
            gcmInfo.put("gcmId", GCM);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        String url = "Your Domain put here/puretalk/updategcm";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url,
                gcmInfo,
                null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

}
