package game.image.memory.com.imagememorygame;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements ImageGridFragment.OnFragmentInteractionListener {
    public  final int INTERNET_PERMISSION=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},INTERNET_PERMISSION);

        }else {
            FragmentManager mgr = getSupportFragmentManager();
            Fragment fragment = new ImageGridFragment();
            mgr.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==INTERNET_PERMISSION){
            if(grantResults.length>0){
                FragmentManager mgr=getSupportFragmentManager();
                Fragment fragment=new ImageGridFragment();
                mgr.beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        }
    }
}
