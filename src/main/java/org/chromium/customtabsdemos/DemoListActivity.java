// Copyright 2015 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.chromium.customtabsdemos;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class DemoListActivity extends AppCompatActivity implements View.OnClickListener {

        private EditText mUrlEditText;

    //뮤직플레이어 변수수
   private MediaPlayer mp;

   //버전확인 용 변수
    private int versioncode = 1; //어플내 버전
    private ProgressDialog dialog;
    String xml;

    //공지용 변수
    int fish=0;

    //이미지비뷰 변수
    ImageView img;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);



            img = (ImageView)findViewById(R.id.img);

            Spinner s = (Spinner)findViewById(R.id.bg);
            s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    switch (position) {
                        case 0:
                            img.setImageResource(R.drawable.madoromi);
                            break;
                        case 1:
                            img.setImageResource(R.drawable.madoromi2);
                            break;
                        case 2:
                            img.setImageResource(R.drawable.madoromi3);
                            break;
                        case 3:
                            img.setImageResource(R.drawable.madoromi4);
                            break;
                        case 4:
                            img.setImageResource(R.drawable.madoromi5);
                            break;
                        case 5:
                            img.setImageResource(R.drawable.madoromi6);
                            break;
                        case 6:
                            img.setImageResource(R.drawable.madoromi7);
                            break;
                        case 7:
                            img.setImageResource(R.drawable.madoromi8);
                            break;


                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });


            //공지사항
            SharedPreferences pref = getSharedPreferences("save", 0);
            fish = pref.getInt("fish", 0);

            if(fish==0)
            {
                AlertDialog.Builder builder = new
                        AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle("공지");
                builder.setMessage("이 앱은 공식 어플이 아닙니다.\n" +
                        "본 앱을 사용한 피해는 모두 본인 책임입니다.\n" +
                        "공지사항은 앱을 처음 받았을 때에만 한 번 뜹니다.");
                builder.setPositiveButton("확인", new
                        DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                fish=1;
                                SharedPreferences pref=getSharedPreferences("save", 0);
                                SharedPreferences.Editor edit=pref.edit();

                                edit.putInt("fish", fish);
                                edit.commit();
                            }});
                builder.show();
            }

            //버전확인
            dialog = new ProgressDialog(this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setMessage("버전 확인 중");
            dialog.show();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
            StringBuilder sBuffer = new StringBuilder();
            try
            {//Start Try
                String urlAddr = "https://bugye6143.000webhostapp.com/Ver.txt";
                URL url = new URL(urlAddr);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if(conn != null){//Start if
                    conn.setConnectTimeout(20000);
                    conn.setUseCaches(false);
                    if(conn.getResponseCode()==HttpURLConnection.HTTP_OK)
                    {//Start if
                        InputStreamReader isr = new InputStreamReader(conn.getInputStream());
                        BufferedReader br = new BufferedReader(isr);
                        while(true)
                        {//Start While
                            String line = br.readLine();
                            if(line==null){//Start if
                                break;
                            }//end if
                            sBuffer.append(line);
                        }//end while
                        br.close();
                        conn.disconnect();
                    }//end if
                }//end if
                xml = sBuffer.toString();
                CountDownTimer _timer = new CountDownTimer(3 * 1000, 1000)
                {
                    public void onTick(long millisUntilFinished)
                    {}
                    public void onFinish()
                    {
                        dialog.dismiss();
                        if(Integer.parseInt(xml)==versioncode)

                        {//new version
                            Toast.makeText(DemoListActivity.this,"최신버전 입니다.",0).show();
                        }
                        else if(Integer.parseInt(xml)>versioncode)

                        {//crack version

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                    DemoListActivity.this);


                            // Setting Dialog Title
                            // Setting Dialog Message

                            alertDialog.setTitle("업데이트");

                            // I've included a simple dialog icon in my project named "dialog_icon", which's image file is copied and pasted in all "drawable" folders of "res" folders of the project. You can include any dialog image of your wish and rename it to dialog_icon.
                            alertDialog.setMessage("업데이트 하시겠습니까?");

                            // Setting Icon to Dialog
                            // Setting Positive "Yes" Button

                            alertDialog.setPositiveButton("예",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://bugye6143.000webhostapp.com"));
                                            startActivity(i);
                                            finish();
                                        }

                                    });

                            // Setting Negative "NO" Button

                            alertDialog.setNegativeButton("아니오",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Write your code here to invoke NO event
                                            Toast.makeText(DemoListActivity.this, "업데이트를 해주세요!", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                            // Showing Alert Message
                            alertDialog.show();
                        }
                        else

                        {//old version
                            Toast.makeText(DemoListActivity.this,"점검중입니다.",0).show();
                            finish();
                        }
                    }
                };
                _timer.start();
            }//end try
            catch (Exception e)

            {//start catch
                Toast.makeText(getApplicationContext(), "버전로딩중 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
            }//end catch
            Spinner music = (Spinner)findViewById(R.id.music);
            music.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i) {

                        case 0 :
                            Uri file = Uri.parse("http://bugye6143.000webhostapp.com/Music/1.mp3");
                            mp = MediaPlayer.create(DemoListActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                            break;
                        case 1:
                            mp.stop();
                            Uri file2 = Uri.parse("http://bugye6143.000webhostapp.com/Music/2.mp3");
                            mp = MediaPlayer.create(DemoListActivity.this, file2);
                            mp.setLooping(true);
                            mp.start();
                            break;
                        case 2:
                            mp.stop();
                            Uri file3 = Uri.parse("http://bugye6143.000webhostapp.com/Music/3.mp3");
                            mp = MediaPlayer.create(DemoListActivity.this, file3);
                            mp.setLooping(true);
                            mp.start();
                            break;
                        case 3 :
                            mp.stop();
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            findViewById(R.id.start_custom_tab).setOnClickListener(this);

            mUrlEditText = (EditText)findViewById(R.id.url);
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();

            switch (viewId) {
                case R.id.start_custom_tab:
                    String url = mUrlEditText.getText().toString();
                    CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                    CustomTabActivityHelper.openCustomTab(
                            this, customTabsIntent, Uri.parse(url), new WebviewFallback());
                    break;
                default:
                    //Unknown View Clicked
            }
        }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    backButtonHandler();
                    break;
            }
        }
        return true;
    }

        public void hitomi (View v) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/0B4T5bWOlBITkRFFyb2Z6UHhKNVk/view"));
            startActivity(i);
        }


            public void backButtonHandler() {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        DemoListActivity.this);


                // Setting Dialog Title
                // Setting Dialog Message

                alertDialog.setTitle("종료");

                // I've included a simple dialog icon in my project named "dialog_icon", which's image file is copied and pasted in all "drawable" folders of "res" folders of the project. You can include any dialog image of your wish and rename it to dialog_icon.

                alertDialog.setMessage("종료하시겠습니까?" +
                        "종료시 노래도 같이 꺼집니다.");

                // Setting Icon to Dialog
                // Setting Positive "Yes" Button

                alertDialog.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mp.stop();
                                finish();
                            }
                        });

                // Setting Negative "NO" Button

                alertDialog.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke NO event
                                dialog.cancel();
                            }
                        });
                // Showing Alert Message
                alertDialog.show();
            }
        }
