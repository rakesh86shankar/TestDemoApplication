package com.example.ra283478.testdemoapplication;




import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BaseActivity{
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NewsPaperTypesFragment fragment = new NewsPaperTypesFragment();
        fragment.setActivityCallBack(this);
        Log.v("Within Fragment","Taggeer");
        loadFragment(fragment);
    }

    private void loadFragment(Fragment currentFragment) {
        // create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, currentFragment);
        fragmentTransaction.commit(); // save the changes
    }

    @Override
    public void getStringResponse(final String value) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,value,Toast.LENGTH_LONG).show();
            }
        },1000);
    }

    @Override
    public void updateUI(final String value) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,value,Toast.LENGTH_LONG).show();
            }
        },1000);
    }
}
