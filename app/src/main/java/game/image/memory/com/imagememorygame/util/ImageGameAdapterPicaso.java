package game.image.memory.com.imagememorygame.util;

/**
 * Created by inssingh35 on 6/21/2016.
 */

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.HashMap;

import game.image.memory.com.imagememorygame.MyApplication;
import game.image.memory.com.imagememorygame.R;

/**
 * Created by inssingh35 on 6/20/2016.
 */
public class ImageGameAdapterPicaso extends RecyclerView.Adapter<ImageGameAdapterPicaso.MyviewHolder> {


    private static final String TAG ="ImageAdapter" ;
    private Context context=null;
    HashMap<Integer,Bitmap> bitmapMap=null;
    private ImageLoader imageLoader=null;
    private boolean isClickable=false;
    private ItemClickInterface itemClickInterface;

    public ImageGameAdapterPicaso(Context context, HashMap<Integer,Bitmap> bitmapMap, ItemClickInterface clickInterface){
        super();
        this.context=context;
        this.bitmapMap=bitmapMap;
        imageLoader= MyApplication.getAppInstance().getImageLoader();
        this.isClickable=isClickable;
        this.itemClickInterface=clickInterface;
    }


    public void updateAdapter(HashMap<Integer,Bitmap> bitmapMap ){
        this.bitmapMap=bitmapMap;
        notifyDataSetChanged();
    }
    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_row,parent,false);
        return new MyviewHolder(view,itemClickInterface);

    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {

        Bitmap photo=bitmapMap.get(position);
        try {
            // holder.imageView.setImageUrl(FlickrDownloadApi.constructFlickrImgUrl(photo), imageLoader);
            holder.imageView.setImageBitmap(photo);
            Log.i(TAG, "onBindViewHolder: Image"+photo.toString());
            holder.textView.setText(Integer.toString(position+1));
            setFlipRight(holder.itemView);
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

    private void setFlipRight(View view) {
//        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
//        anim.setDuration(500);
//        view.startAnimation(anim);
        ObjectAnimator animator=ObjectAnimator.ofFloat(view,"y",0,1);
        animator.setDuration(1000);
        animator.start();;
//        Animation flipright = AnimationUtils.loadAnimation(context,
//                R.anim.card_flip_right);
//        flipright.setDuration(800);
//        view.startAnimation(flipright);
    }

    public static class MyviewHolder extends  RecyclerView.ViewHolder{

        public ImageView imageView=null;
        public TextView textView=null;
        public MyviewHolder(View itemView, final ItemClickInterface clickInterface) {
            super(itemView);
            imageView=(ImageView) itemView.findViewById(R.id.row_image_view);
            textView=(TextView)itemView.findViewById(R.id.textViewNumber);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=getAdapterPosition();
                    clickInterface.onItemClick(pos);

                }
            });
        }
    }
}
