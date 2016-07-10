package game.image.memory.com.imagememorygame.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import game.image.memory.com.imagememorygame.Constants;
import game.image.memory.com.imagememorygame.pojo.Photo;

/**
 * Created by inssingh35 on 6/20/2016.
 */
public class FlickrDownloadApi {




    private static final String FLICKRAPIKEY = "http://www.flickr.com/services/api/keys/"+ Constants.FLICKR_KEY;


    public String constructFlickrImgUrl(JSONObject input, Enum size) throws JSONException {
        String FARMID = input.getString("farm");
            String SERVERID = input.getString("server");
        String SECRET = input.getString("secret");
        String ID = input.getString("id");

        StringBuilder sb = new StringBuilder();

        sb.append("http://farm");
        sb.append(FARMID);
        sb.append(".static.flickr.com/");
        sb.append(SERVERID);
        sb.append("/");
        sb.append(ID);
        sb.append("_");
        sb.append(SECRET);
        sb.append(size.toString());
        sb.append(".jpg");

        return sb.toString();
    }

    public static String constructFlickrImgUrl(Photo photo) throws JSONException {
        String FARMID = photo.getFarm()+"";
        String SERVERID = photo.getServer();//input.getString("server");
        String SECRET = photo.getSecret();//input.getString("secret");
        String ID = photo.getId();//input.getString("id");

        StringBuilder sb = new StringBuilder();

        sb.append("https://farm");
        sb.append(FARMID);
        sb.append(".static.flickr.com/");
        sb.append(SERVERID);
        sb.append("/");
        sb.append(ID);
        sb.append("_");
        sb.append(SECRET);
       // sb.append("1");
        sb.append(".jpg");

        Log.i("GenratedURL", "constructFlickrImgUrl: "+ sb.toString());

        return sb.toString();
    }


}
