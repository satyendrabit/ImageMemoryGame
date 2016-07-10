package game.image.memory.com.imagememorygame;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import game.image.memory.com.imagememorygame.pojo.FlickrPhoto;
import game.image.memory.com.imagememorygame.pojo.Photo;
import game.image.memory.com.imagememorygame.util.ImageAdapter;
import game.image.memory.com.imagememorygame.util.ImageDownloader;
import game.image.memory.com.imagememorygame.util.InformationDialogFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageGridFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageGridFragment extends Fragment implements InformationDialogFragment.CallBackForButtons{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String FLICKRAPIKEY = "http://www.flickr.com/services/api/keys/"+ Constants.FLICKR_KEY;
    private static final String TAG = "IMAGEGRIDFRAGMENT" ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog progressBar=null;
    private FlickrPhoto flickrPhoto=null;
    private ImageDownloader imageDownloader=null;
    List<Photo> photoList=null;
    private HashMap<Integer, Bitmap> bitmapMap=null;
    private TextView textViewTimer=null;
    private Button buttonRetry=null;

    public ImageGridFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageGridFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageGridFragment newInstance(String param1, String param2) {
        ImageGridFragment fragment = new ImageGridFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_image_grid, container, false);
        textViewTimer=(TextView)view.findViewById(R.id.textViewTimer);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        buttonRetry=(Button)view.findViewById(R.id.buttonRetry);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ImageAdapter(getActivity(),null);
        mRecyclerView.setAdapter(mAdapter);
//        RecyclerView.ItemDecoration itemDecoration =
//                new RecyclerView.DefaultItemAnimator();

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());



        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getImageList();
        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageList();
                buttonRetry.setVisibility(View.GONE);
            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onOkClick() {
        updateAdpater();

    }

    @Override
    public void onCancelClick() {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void getImageList(){
        progressBar = new ProgressDialog(getActivity());
        progressBar.setCancelable(false);
        progressBar.setMessage("Loading images from Flickr. Please wait...");
        progressBar.show();
        String searchPattern="all";
        String url="https://api.flickr.com/services/rest/?method=flickr.photos.search&text=" + searchPattern + "&api_key=" + Constants.FLICKR_KEY +"&safe_search=1&per_page="+ "9" + "&format=json";
        StringRequest request =new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int start = response.indexOf("(") + 1;
                        int end = response.length() - 1;
                        response=response.substring(start,end);
                        Gson gson=new Gson();
                        flickrPhoto=gson.fromJson(response, FlickrPhoto.class);
                        photoList=flickrPhoto.getPhotos().getPhoto();
                        Log.i(TAG,response);
                        loadImageViaLoader();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error instanceof NoConnectionError){
                    textViewTimer.setText(getString(R.string.nointernetconnectionerror));

                }
                else{
                    textViewTimer.setText(getString(R.string.genricerromessage));
                }
                Log.e(TAG, "onErrorResponse: "+error.toString() );
                error.printStackTrace();
                progressBar.dismiss();
                buttonRetry.setVisibility(View.VISIBLE);
            }
        });

        MyApplication.getAppInstance().getRequestQueue().add(request);

    }

    public void loadImageViaLoader(){
        imageDownloader=new ImageDownloader(photoList,this);
        imageDownloader.startDownload();
       /* if(flickrPhoto!=null){
             photoList= flickrPhoto.getPhotos().getPhoto();
            mAdapter.updateAdapter(photoList);
        }*/
    }


    public void updateImageInList(HashMap<Integer,Bitmap> bitmapMap ){

        Log.i(TAG, "updateImageInList: "+bitmapMap.size());
        this.bitmapMap=bitmapMap;

        if(progressBar!=null) {
            progressBar.dismiss();
        }
       InformationDialogFragment dialog=new InformationDialogFragment
               (this.getContext(),getString(R.string.information),
               this,false);
        dialog.setCancelable(false);
        dialog.show();

    }

    public void updateAdpater(){
        if(this.bitmapMap!=null){
            mAdapter.updateAdapter(bitmapMap);
            startTimer();

        }
    }


    public void startTimer(){
        new Thread(new Runnable() {
            private int count=15;
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(1000);

                        ImageGridFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    textViewTimer.setText(count+"");
                            }
                        });

                        count--;
                        if(count<=0){
                            if(getActivity()!=null) {
                                Intent intent = new Intent(getActivity(), GameActivity.class);
                                MyApplication.getAppInstance().setExtraData(bitmapMap);
                                getActivity().startActivity(intent);
                                getActivity().finish();
                            }
                            break;
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }

                }

            }
        }).start();
    }


}


