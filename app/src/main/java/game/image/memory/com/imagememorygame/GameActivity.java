package game.image.memory.com.imagememorygame;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import game.image.memory.com.imagememorygame.util.ImageAdapter;
import game.image.memory.com.imagememorygame.util.ImageGameAdapter;
import game.image.memory.com.imagememorygame.util.InformationDialogFragment;
import game.image.memory.com.imagememorygame.util.ItemClickInterface;

public class GameActivity extends AppCompatActivity implements ItemClickInterface,InformationDialogFragment.CallBackForButtons {

    private static final String TAG = "GameActivity";
    private RecyclerView mRecyclerView=null;
    ImageGameAdapter mAdapter=null;
    int numberoftry=0;
    HashMap<Integer,Bitmap> bitmapMap=null;
    HashMap<Integer,Bitmap> adapterBitMap=null;
    ArrayList<Integer> randomdata=null;
     private int currentDisplayedImageIndex=0;
    private ImageView imageView=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_game);

        bitmapMap=(HashMap<Integer,Bitmap>)MyApplication.getAppInstance().getExtraData();
        MyApplication.getAppInstance().setExtraData(null);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        imageView=(ImageView)findViewById(R.id.imageView);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mLayoutManager);
         mAdapter = new ImageGameAdapter(this, null,this);
        mRecyclerView.setAdapter(mAdapter);

//        RecyclerView.ItemDecoration itemDecoration =
//                new RecyclerView.DefaultItemAnimator();

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
       // Toast.makeText(this,"size of image is"+bitmapMap.size(),Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentDisplayedImageIndex=(int)savedInstanceState.getInt("currentDisplayedImageIndex");
        numberoftry=(int)savedInstanceState.getInt("numberoftry");
        bitmapMap=(HashMap)MyApplication.getAppInstance().getExtraData();
        if(bitmapMap==null){
            Toast.makeText(this,getString(R.string.unabletocontinue),Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        adapterBitMap=(HashMap)MyApplication.getAppInstance().getExtraData2();
        MyApplication.getAppInstance().setExtraData2(null);
        MyApplication.getAppInstance().setExtraData(null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setDummyImage();
        mAdapter.notifyDataSetChanged();
    }

    public void  setDummyImage(){
        adapterBitMap=new HashMap<>();
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.info);
        for(int i=0;i<9;i++){
            adapterBitMap.put(i,bitmap);
        }
        mAdapter.updateAdapter(adapterBitMap);
        randomdata=new ArrayList<Integer>();
        for(int i=0;i<9;i++){
            randomdata.add(i);
        }
        Collections.shuffle(randomdata);
        displayRandomImage();


    }

    @Override
    public void onItemClick(int position) {

        if(currentDisplayedImageIndex>=randomdata.size()){
           // Toast.makeText(this,getString(R.string.winning_message),Toast.LENGTH_SHORT).show();
            displayWinningMessage();
            return;
        }
        numberoftry++;
        int key=randomdata.get(currentDisplayedImageIndex);
        if(key==position){
             adapterBitMap.put(key,bitmapMap.get(key));
            mAdapter.notifyItemChanged(key);
            currentDisplayedImageIndex++;
            displayRandomImage();
        }
      //  Toast.makeText(this,"Item Clicked "+(position)+","+key,Toast.LENGTH_LONG).show();


    }

    public void displayRandomImage(){



        if(currentDisplayedImageIndex>=randomdata.size()){
           // Toast.makeText(this,getString(R.string.winning_message) ,Toast.LENGTH_SHORT).show();
            displayWinningMessage();
            return;
        }

       int key=randomdata.get(currentDisplayedImageIndex);
        Log.i(TAG,"key "+key+" ,"+currentDisplayedImageIndex);

       // adapterBitMap.put(key,bitmapMap.get(key));
        imageView.setImageBitmap(bitmapMap.get(key));
        //mAdapter.notifyItemChanged(key-1);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentDisplayedImageIndex",currentDisplayedImageIndex);
        outState.putInt("numberoftry",numberoftry);
        MyApplication.getAppInstance().setExtraData(bitmapMap);
        MyApplication.getAppInstance().setExtraData2(adapterBitMap);
    }

    @Override
    public void onBackPressed() {

        if(currentDisplayedImageIndex<8){
          //  InformationDialogFragment.
            InformationDialogFragment dialog=new
                    InformationDialogFragment
                    (this, getString(R.string.exitwarning), new InformationDialogFragment.CallBackForButtons() {
                        @Override
                        public void onOkClick() {
                            finish();
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    }, true);
            dialog.setCancelable(false);
            dialog.show();
        }
       // super.onBackPressed();
    }

    @Override
    public void onOkClick() {
        finish();
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCancelClick() {
       finish();
    }

    public void displayWinningMessage(){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(getString(R.string.winning_message));
        stringBuilder.append("You took "+ numberoftry+ " click to finish.");

        if(numberoftry<15){
            stringBuilder.append(" You are a Pro");
            stringBuilder.append(". Want to Play Again");
        }else if(numberoftry<=20){
            stringBuilder.append(" You are good");
            stringBuilder.append(". Want to Improve your Skills. Play Again");
        }else{
            stringBuilder.append(" You can be better.");
            stringBuilder.append(". Want to Improve your Skills. Play Again.");
        }
        InformationDialogFragment dialogFragment=
                new InformationDialogFragment(this,stringBuilder.toString(),this,true);
        dialogFragment.setCancelable(false);
        dialogFragment.show();

    }
}
