package game.image.memory.com.imagememorygame.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.HashMap;
import java.util.List;

import game.image.memory.com.imagememorygame.MyApplication;
import game.image.memory.com.imagememorygame.R;
import game.image.memory.com.imagememorygame.pojo.Photo;

/**
 * Created by inssingh35 on 6/20/2016.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyviewHolder> {


    private static final String TAG ="ImageAdapter" ;
    private Context context=null;
    HashMap<Integer,Bitmap> bitmapMap=null;
   // private ImageLoader imageLoader=null;
    //private boolean isClickable=false;

    public ImageAdapter(Context context,HashMap<Integer,Bitmap> bitmapMap){
        super();
        this.context=context;
        this.bitmapMap=bitmapMap;
       // imageLoader= MyApplication.getAppInstance().getImageLoader();

    }


    public void updateAdapter(HashMap<Integer,Bitmap> bitmapMap ){
        this.bitmapMap=bitmapMap;
        notifyDataSetChanged();
    }
    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_row,parent,false);
        return new MyviewHolder(view);

    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {

        Bitmap photo=bitmapMap.get(position);
        try {
           // holder.imageView.setImageUrl(FlickrDownloadApi.constructFlickrImgUrl(photo), imageLoader);
            holder.imageView.setImageBitmap(photo);
          //  Log.i(TAG, "onBindViewHolder: Image"+photo.toString());
            holder.textView.setText(Integer.toString(position+1));
        }
        catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {

        if(bitmapMap!=null){
            return bitmapMap.size();
        }
        return 0;
    }

    public static class MyviewHolder extends  RecyclerView.ViewHolder{

        public ImageView imageView=null;
        public TextView  textView=null;
        public MyviewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView) itemView.findViewById(R.id.row_image_view);
            textView=(TextView)itemView.findViewById(R.id.textViewNumber);
        }
    }
}
