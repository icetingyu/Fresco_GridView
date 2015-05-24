package hsu.icesimon.facebookfresco;

/**
 * Created by Simon Hsu on 15/5/24.
 * Activity Container for Fragment with GridView which combined with Fresco, Universal Image Loader and Picasso.
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;


public class LocalImageAllHybirdActivity extends Activity {

    Fragment localImageAllHybridFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_image);
        localImageAllHybridFragment = LocalImageAllHybridFragment.newInstance();
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction
                .replace(R.id.localImageContainer, localImageAllHybridFragment, null)
                .commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }


    private void finishActivity() {
        this.finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}
