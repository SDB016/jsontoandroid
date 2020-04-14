package com.example.jsontoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
    ListView listView;

    private String[] nameList;
    private String[] genderList;
    private String[] banList;
    private String[] userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

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
                            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,userInfo);
                            listView.setAdapter(adapter);
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(5000);
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
            userInfo = new String[list_cnt];

            for (int i = 0; i < list_cnt; i++) {
                JSONObject tmp = (JSONObject)jsonArray.get(i);
                nameList[i] = (String)tmp.get("name");
                genderList[i] = (String)tmp.get("gender");
                banList[i] = (String)tmp.get("class");
                userInfo[i] = (String)tmp.get("name") + ' ' + (String)tmp.get("class") + ' ' + (String)tmp.get("gender");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}



















