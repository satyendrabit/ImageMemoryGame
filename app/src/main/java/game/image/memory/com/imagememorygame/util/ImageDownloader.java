package game.image.memory.com.imagememorygame.util;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import game.image.memory.com.imagememorygame.ImageGridFragment;
import game.image.memory.com.imagememorygame.MyApplication;
import game.image.memory.com.imagememorygame.pojo.Photo;

/**
 * Created by inssingh35 on 6/21/2016.
 */
public class ImageDownloader implements Response.Listener<Bitmap>,Response.ErrorListener {

    private static final String TAG = "ImageDownloader" ;
    private Handler uiHandler=null;
    List<Photo> photoList=null;
    HashMap<Integer,Bitmap> bitmapMap=null;
    ImageGridFragment fragment=null;
    private Object mutex=new Object();
    int count=0;

    public ImageDownloader(List<Photo> list, ImageGridFragment fragment) {
        //super(name);
        //this.uiHandler=handler;
        this.photoList=list;
        this.fragment=fragment;
        bitmapMap=new HashMap<>();
    }

    public ImageDownloader(String name, int priority) {
        //super(name, priority);
    }

    public void startDownload(){
        for (Photo obj: photoList) {
            try {
                String url = FlickrDownloadApi.constructFlickrImgUrl(obj);
                ImageRequest request=new ImageRequest(url,this,0,0,null,null,this);
                MyApplication.getAppInstance().getRequestQueue().add(request);

            }
            catch (Exception e){

            }
        }
    }


    @Override
    public void onResponse(Bitmap response) {
        synchronized (mutex){
           Bitmap bitmapthumnail= ThumbnailUtils.extractThumbnail(response,100,100);
        bitmapMap.put(count,bitmapthumnail);

            count++;
            if(count>8){
                if(fragment!=null && fragment.getActivity()!=null){
                    fragment.updateImageInList(bitmapMap);

                }
            }
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        Log.e(TAG,"Error in downloading image"+error.toString());
        synchronized (mutex){
            count++;
        }


    }


}
