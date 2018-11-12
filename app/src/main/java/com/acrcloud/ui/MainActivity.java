package com.acrcloud.ui;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.acrcloud.utils.ACRCloudRecognizer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView mVolume, mResult, tv_time;

    private boolean mProcessing = false;
    private boolean initState = false;

    private String path = "";


    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    String res = (String) msg.obj;
                    mResult.setText(res);
                    break;

                default:
                    break;
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        path = Environment.getExternalStorageDirectory().toString()
//                + "/acrcloud/model";
//
        path = Environment.getExternalStorageDirectory().toString()
                + "/Download/Despacito.mp3";

        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }

        mResult = (TextView) findViewById(R.id.result);

        Button recBtn = (Button) findViewById(R.id.rec);
        recBtn.setText(getResources().getString(R.string.rec));

        findViewById(R.id.rec).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                rec();
            }
        });
    }

    class RecThread extends Thread {

        public void run() {
            Map<String, Object> config = new HashMap<String, Object>();
            // Replace "xxxxxxxx" below with your project's host, access_key and access_secret.
            config.put("access_key", "37a24216f7bdbfd272dab7035927e4cd");
            config.put("access_secret", "pfVkL0Vg4frc3Wk92qoqvfGgT8u5nQLLuS3AfHNK");
            config.put("host", "identify-eu-west-1.acrcloud.com");
            config.put("debug", false);
            config.put("timeout", 5);

            ACRCloudRecognizer re = new ACRCloudRecognizer(config);
            String result = re.recognizeByFile(path , 10);
            System.out.println(result);

            //File file = new File(path + "/test.mp3");
            //byte[] buffer = new byte[3 * 1024 * 1024];
            //if (!file.exists()) {
            //    return;
            //}
            //FileInputStream fin = null;
            //int bufferLen = 0;
            //try {
            //    fin = new FileInputStream(file);
            //    bufferLen = fin.read(buffer, 0, buffer.length);
            //} catch (Exception e) {
            //    e.printStackTrace();
            //} finally {
            //    try {
            //        if (fin != null) {
            //            fin.close();
            //        }
            //    } catch (IOException e) {
            //        e.printStackTrace();
            //    }
            //}
            //System.out.println("bufferLen=" + bufferLen);

            //if (bufferLen <= 0)
            //    return;

            //String result = re.recognizeByFileBuffer(buffer, bufferLen, 80);

            try {
                Message msg = new Message();
                msg.obj = result;

                msg.what = 1;
                mHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void rec() {
        new RecThread().start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
