package org.npost.puretalk.ViewPager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import org.npost.puretalk.MyView.FirstPage.MyFirstView;
import org.npost.puretalk.MyView.SecondPage.MySecondView;
import org.npost.puretalk.MyView.ThirdPage.MyThirdView;

public class ViewPagerAdapter extends PagerAdapter {
    Context context;
    int tabNumber;

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    public ViewPagerAdapter(Context context, int tabNumber) {
        this.context = context;
        this.tabNumber = tabNumber;
    }

    @Override
    public int getCount() {
        return tabNumber;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null;
        switch (position) {
            case 0 :
                view = new MyFirstView(context);
                break;
            case 1 :
                view = new MySecondView(context);
                break;
            case 2 :
                view = new MyThirdView(context);
                break;
        }
        if (view != null) {
            container.addView(view);
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
