package hsu.icesimon.facebookfresco;

/**
 * Created by Simon Hsu on 15/5/24.
 * Activity Container for Fragment with GridView which use Picasso to load local images.
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;


public class LocalImagePicassoActivity extends Activity {

    Fragment localImagePicassoFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_image);
        localImagePicassoFragment = LocalImagePicassoFragment.newInstance();
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction
                .replace(R.id.localImageContainer, localImagePicassoFragment, null)
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
