package game.image.memory.com.imagememorygame.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by inssingh35 on 6/21/2016.
 */
public class LRUBitmapCache extends LruCache<String,Bitmap> implements ImageLoader.ImageCache {


    public static int getMaxSize(){
        final  int maxsize = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cachesize = maxsize / 8;
        return cachesize;
    }
    public LRUBitmapCache() {

        super(getMaxSize());
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
            put(url,bitmap);
    }
}
