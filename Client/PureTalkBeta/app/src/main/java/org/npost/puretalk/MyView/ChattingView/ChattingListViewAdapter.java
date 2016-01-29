package org.npost.puretalk.MyView.ChattingView;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;

public class ChattingListViewAdapter extends BaseAdapter {
    Context context = null;
    JSONArray jsonArray = null;

    public ChattingListViewAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @Override
    public int getCount() {
        int length = jsonArray.length();
        return length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convertView로 뷰 재사용해야 성능이 높아진다.
        // 하지만 flag 값에 따라서 리턴하는 뷰가 달라질 때는 뷰 재사용이 까다롭다.
        // 2차 개발로 연기하자.
        JsonChattingListView messageListView;
        messageListView = new JsonChattingListView(context, jsonArray, position);
        messageListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return messageListView;

        // 2차 개발 진행상황. position 값에 문제가 있다. jsonArray nullPointException 난다.
        // 해결 방법: 프로그램 방식으로 View 속성들을 지정해줬다. JsonChattingListView 소스 참고
        /*JsonChattingListView messageListView;
        if (convertView == null) {
            messageListView = new JsonChattingListView(context, jsonArray, position);
            messageListView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        } else {
            messageListView = (JsonChattingListView) convertView;
            messageListView.setFlagView(position);
        }
        Log.d("position : ", ""+position);
        return messageListView;*/
    }

    public void add(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    // ListView 클릭 시 데이터 제공해주는 부분
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
