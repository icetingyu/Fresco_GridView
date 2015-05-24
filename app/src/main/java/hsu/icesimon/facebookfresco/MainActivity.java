package hsu.icesimon.facebookfresco;

/**
 * Created by Simon Hsu on 15/5/24.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.backends.pipeline.Fresco;

public class MainActivity extends Activity  {

    private Button removeFrescoBtn;
    private Button localFrescoBtn;
    private Button localPicassoBtn;
    private Button localUILBtn;
    private Button localPicassoUILBtn;
    private Button localAllHybridBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(MainActivity.this);
        setContentView(R.layout.activity_main);
        removeFrescoBtn = (Button) findViewById(R.id.remoteFrescoBtn);
        localFrescoBtn = (Button) findViewById(R.id.localFrescoBtn);
        localPicassoBtn = (Button) findViewById(R.id.localPicassoBtn);
        localUILBtn = (Button) findViewById(R.id.localUILBtn);
        localPicassoUILBtn = (Button) findViewById(R.id.localPicassoUILBtn);
        localAllHybridBtn = (Button) findViewById(R.id.localAllHybridBtn);
        removeFrescoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RemoteImageActivity.class);
                startActivityForResult(intent, Activity.RESULT_OK);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        localFrescoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocalImageActivity.class);
                startActivityForResult(intent,Activity.RESULT_OK);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        localPicassoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocalImagePicassoActivity.class);
                startActivityForResult(intent,Activity.RESULT_OK);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        localUILBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocalImageUILActivity.class);
                startActivityForResult(intent,Activity.RESULT_OK);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        localPicassoUILBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocalImagePicassoUILHybirdActivity.class);
                startActivityForResult(intent,Activity.RESULT_OK);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        localAllHybridBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocalImageAllHybirdActivity.class);
                startActivityForResult(intent,Activity.RESULT_OK);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
