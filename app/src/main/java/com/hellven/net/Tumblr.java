package com.hellven.net;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by PC on 2017-06-05.
 */

public class Tumblr extends AppCompatActivity {

    ArrayList<String> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tumblr);


        data = new ArrayList<String>();
        data.add("동양 저장소");
        data.add("한글 동인지");
        data.add("추가예정");
        data.add("추가예정");
        data.add("추가예정");
        data.add("추가예정");
        data.add("추가예정");
        data.add("추가예정");
        data.add("추가예정");
        data.add("추가예정");
        data.add("추가예정");
        data.add("추가예정");
        data.add("추가예정");



        //어댑터 생성
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, data);
        //어댑터 연결
        ListView listview = (ListView)findViewById(R.id.tumblr);
        listview.setAdapter(adapter);



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                {
                    if (data.get(position) == "동양 저장소") {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://save33334.tumblr.com/"));
                        startActivity(i);
                    }
                    if (data.get(position) == "한글 동인지") {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://dongintumb0.tumblr.com/"));
                        startActivity(i);
                    }
                }
            }});

        }
}
