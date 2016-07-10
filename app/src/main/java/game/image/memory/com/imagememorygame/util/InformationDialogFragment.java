package game.image.memory.com.imagememorygame.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import game.image.memory.com.imagememorygame.R;


public class InformationDialogFragment extends Dialog {

    private Context mContext;
    private String mTitle;
    private String mContent;

    private CallBackForButtons mCallBack = null;
    Object mCancelButtonTag = null;
    boolean iscancelable=false;
    Object mOkButtonTag = null;

    public interface CallBackForButtons{
        void onOkClick();
        void onCancelClick();
    }



    public InformationDialogFragment(Context context, String content,
                           CallBackForButtons callback,boolean iscancelable) {
        super(context);

        mContext = context;
        mContent = content;
        mCallBack = callback;
        this.iscancelable=iscancelable;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_information);


        if(mContent != null){
            TextView textContent = (TextView)findViewById(R.id.textViewInfo);
            textContent.setText(mContent);
        }

        Button buttonOK = (Button) findViewById(R.id.buttonOk);
        Button buttonCancel=(Button)findViewById(R.id.buttonCancel);
        if(!iscancelable)
        {
            buttonCancel.setVisibility(View.GONE);
        }
        buttonOK.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                if(mCallBack != null){
                    mCallBack.onOkClick();
                }
                dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCallBack != null){
                    mCallBack.onCancelClick();
                }
                dismiss();
            }
        });

        if(mOkButtonTag != null){
            buttonOK.setTag(mOkButtonTag);
        }

    }
}
