package org.npost.puretalk.MyActivity.Member;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.npost.puretalk.Volley.SingletonRequestQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * getNumberPhoneBook() : 핸드폰 주소록 가져오기
 * getDbNumber() : DB User 전체 정보 가져오기
 */
public class FriendList {
    private final String TAG = "FriendList";
    Context context;
    RequestQueue requestQueue;

    public FriendList() {}
    public FriendList(Context context) {
        this.context = context;
    }

    public JSONObject getNumberPhoneBook() {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String usercellphone = telephonyManager.getLine1Number();

        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);

        JSONObject phoneBookNumber = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        while (cursor.moveToNext()) {
            // 사용자 주소록
            JSONObject jsonObject = new JSONObject();
            // _ID열 조회
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            // DISPLAY_NAME_PRIMARY 열 조회
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
            // JSON으로 저장
            try {
                jsonObject.put("usercellphone", usercellphone); // 사용자 전화번호
                jsonObject.put("friendname", name);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Cursor phoneCursor = context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                    null, null
            );
            if (phoneCursor.moveToFirst()) {
                String number = phoneCursor.getString(phoneCursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                ));
                try {
                    jsonObject.put("friendphone", number.replace("-", ""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            jsonArray.put(jsonObject);
            try {
                phoneBookNumber.put("phoneBookNumber", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            phoneCursor.close();
        }
        cursor.close();
        return phoneBookNumber;
    }

    // 결과 리턴용 인터페이스 콜백. 참고 joinActivity - 여전히 비동기 작업임에 유의하기.
    public void getDbNumber(final VolleyCallback callback) {
        ArrayList<Map> dbNumber = new ArrayList<Map>();

        // DB접속 url
        requestQueue = SingletonRequestQueue.getInstance(context).getRequestQueue();
        String url = "Your Domain put here/puretalk/setfriendlist";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url,
                getNumberPhoneBook(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<Map> dbNumber = new ArrayList<Map>();
                            // 서버에서 받아온 DB저장된 친구목록 arrayList key값에 배열이 들어있다.
                            String json = response.getString("arrayList");
                            JSONArray jsonArray = new JSONArray(json); //이걸 재활용 해야한다!
                            for(int index=0; index < jsonArray.length(); index++) {
                                Map<String, String> map = new HashMap<String, String>();
                                // 첫번째 배열에서 첫번째 오브젝트를 꺼낸다. 반복
                                JSONObject jsonObject = jsonArray.getJSONObject(index);
                                String friendName = jsonObject.getString("name");
                                String friendCellphone = jsonObject.getString("usercellphone");

                                map.put("friendName", friendName);
                                map.put("friendCellphone", friendCellphone);
                                dbNumber.add(map);
                            }
                            callback.onGetDbNumberSuccess(dbNumber);
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(12000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    public interface VolleyCallback {
        void onGetDbNumberSuccess(ArrayList<Map> result);
    }
}
