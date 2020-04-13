package com.example.jsontoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
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

    ConnectHttpThread mconnectThread;
    TextView textView;

    private String[] nameList;
    private String[] genderList;
    private String[] banList;
    private String[] jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textview);

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

                            for(int i =0;i<nameList.length;i++){
                                textView.setText("이름: " + nameList[i] + " 성별: " + genderList[i] + " 반: " + banList[i]);
                                Log.d("run","name is "+nameList[i]);
                                try {
                                    sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
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

    private void jsonRead() {
        int list_cnt;
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(receiveMsg);
            JSONArray jsonArray = (JSONArray)jsonObject.get("result");
            list_cnt = jsonArray.size();
            Log.e("main", "list_cnt is "+list_cnt);

            nameList = new String[list_cnt];
            banList = new String[list_cnt];
            genderList = new String[list_cnt];

            for (int i = 0; i < list_cnt; i++) {
                JSONObject tmp = (JSONObject)jsonArray.get(i);
                nameList[i] = (String)tmp.get("name");
                genderList[i] = (String)tmp.get("gender");
                banList[i] = (String)tmp.get("class");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}



























































