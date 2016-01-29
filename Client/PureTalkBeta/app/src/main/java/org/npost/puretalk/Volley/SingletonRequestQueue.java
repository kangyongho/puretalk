package org.npost.puretalk.Volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

// Google Sample Code 참고
public class SingletonRequestQueue {

    private static SingletonRequestQueue sInstance;
    private RequestQueue sRequestQueue;
    private ImageLoader sImageLoader;
    private static Context sContext;

    private SingletonRequestQueue(Context context) {
        sContext = context;
        sRequestQueue = getRequestQueue();

        sImageLoader = new ImageLoader(sRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized SingletonRequestQueue getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SingletonRequestQueue(context);
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        if (sRequestQueue == null) {
            sRequestQueue = Volley.newRequestQueue(sContext.getApplicationContext());
        }
        return sRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getsImageLoader() {
        return sImageLoader;
    }
}
