package org.npost.puretalk.MyView.ThirdPage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.npost.puretalk.R;

public class MyThirdView extends LinearLayout {
    private final String TAG = "MyThirdView";
    GridView gridView;
    Context context;

    public MyThirdView(final Context context) {
        super(context);
        this.context = context;
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.my_third_view, this, true);

        ImageView spring = (ImageView) findViewById(R.id.mygithub);

        spring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://github.com/kangyongho"));
                context.startActivity(intent);
            }
        });

    }

}
