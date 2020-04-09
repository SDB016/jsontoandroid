package com.example.jsontoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    String receiveMsg;
    Handler mhandler;
    TextView textView;

    ConnectHttpThread mconnectThread;

    private ArrayList<String>nameList;
    private ArrayList<String>genderList;
    private ArrayList<String>banList;
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textview);
        nameList = new ArrayList<>();
        genderList = new ArrayList<>();
        banList = new ArrayList<>();

        mhandler = new Handler();
        mconnectThread = new ConnectHttpThread();
        mconnectThread.start();
    }

    class ConnectHttpThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                String url = "";
                InputStream is = null;
                try {
                    Log.e("main", "running");

                    is = new URL("http://chamdong.hopto.org/php_connection.php").openStream();

                    if (is != null) {
                        Log.e("main", "not null");
                    } else {
                        Log.e("main", "null");
                    }
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String str;
                    StringBuffer buffer = new StringBuffer();
                    while ((str = rd.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();


                    mhandler.post(new Runnable() {
                        @Override
                        public void run() {
                            //textView.setText(receiveMsg);
                            jsonRead();
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void jsonRead(){
        try {
            jsonArray = new JSONArray(receiveMsg);
            for (int i = 0; i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String gender = jsonObject.getString("gender");
                String ban = jsonObject.getString("class");
                nameList.add(name);
                genderList.add(gender);
                banList.add(ban);
                textView.setText(nameList.toString() + "\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}



























































