package game.image.memory.com.imagememorygame;

import android.app.Application;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import game.image.memory.com.imagememorygame.util.LRUBitmapCache;

/**
 * Created by inssingh35 on 6/21/2016.
 */
public class MyApplication extends Application {

    private RequestQueue requestQueue=null;
    private RequestQueue cachedRequestQueue=null;
    private ImageLoader  imageLoader=null;
    private Object extraData=null;
    private Object extraData2=null;

    private static MyApplication appInstance=null;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance=this;
    }

    public static synchronized MyApplication   getAppInstance(){
        return appInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (imageLoader == null) {
            imageLoader = new ImageLoader(this.requestQueue,
                    new ImageLoader.ImageCache(){
                        private final LruCache<String, Bitmap>
                                cache = new LruCache<String, Bitmap>(20);
                        @Override
                        public Bitmap getBitmap(String url) {
                           return cache.get(url);

                        }

                        @Override
                        public void putBitmap(String url, Bitmap bitmap) {
                                cache.put(url,bitmap);
                        }
                    });
        }
        return this.imageLoader;
    }

    public Object getExtraData() {
        return extraData;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }

    public Object getExtraData2() {
        return extraData2;
    }

    public void setExtraData2(Object extraData2) {
        this.extraData2 = extraData2;
    }
}
