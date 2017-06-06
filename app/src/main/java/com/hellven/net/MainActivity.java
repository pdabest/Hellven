package com.hellven.net;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ListView listview;
    ListViewAdapter adapter;
    //뮤직플레이어 변수
    private MediaPlayer mp;

    //리스트 목록
    ArrayList<String> data;

    //버전확인 용 변수
    private int versioncode = 7; //어플내 버전
    private ProgressDialog dialog;
    String xml;

    //공지용 변수
    int fish=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar3));



        // ActionBar에 타이틀 변경
        getSupportActionBar().setTitle("Hellven App");

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3383231168405808~2012876371");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //추가한 라인
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();


        final ImageView img = (ImageView)findViewById(R.id.img);


        final String URL = "http://bugye6143.000webhostapp.com/Music/";
        Uri file1 = Uri.parse(URL + "1.mp3");
        mp = MediaPlayer.create(MainActivity.this, file1);

        //공지사항
        SharedPreferences pref = getSharedPreferences("save", 0);
        fish = pref.getInt("fish", 0);

        if (fish == 0) {
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
                        public void onClick(DialogInterface dialog, int which) {
                            fish = 1;
                            SharedPreferences pref = getSharedPreferences("save", 0);
                            SharedPreferences.Editor edit = pref.edit();

                            edit.putInt("fish", fish);
                            edit.commit();
                        }
                    });
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
        try {//Start Try
            String urlAddr = "https://bugye6143.000webhostapp.com/Ver.txt";
            URL url = new URL(urlAddr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {//Start if
                conn.setConnectTimeout(20000);
                conn.setUseCaches(false);
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {//Start if
                    InputStreamReader isr = new InputStreamReader(conn.getInputStream());
                    BufferedReader br = new BufferedReader(isr);
                    while (true) {//Start While
                        String line = br.readLine();
                        if (line == null) {//Start if
                            break;
                        }//end if
                        sBuffer.append(line);
                    }//end while
                    br.close();
                    conn.disconnect();
                }//end if
            }//end if
            xml = sBuffer.toString();
            CountDownTimer _timer = new CountDownTimer(3 * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    dialog.dismiss();
                    if (Integer.parseInt(xml) == versioncode)

                    {//new version
                        Toast.makeText(MainActivity.this, "최신버전 입니다.", 0).show();
                    } else if (Integer.parseInt(xml) > versioncode)

                    {//crack version

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                MainActivity.this);


                        // Setting Dialog Title
                        // Setting Dialog Message

                        alertDialog.setTitle("업데이트 하시겠습니까?");

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
                                        Toast.makeText(MainActivity.this, "업데이트를 해주세요!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                        // Showing Alert Message
                        alertDialog.show();
                    } else

                    {//old version
                        Toast.makeText(MainActivity.this, "점검중입니다.", 0).show();
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


        final EditText edt = (EditText)findViewById(R.id.edt);
        Button hellven = (Button)findViewById(R.id.hellven);

        hellven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = edt.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                i.setPackage("com.android.chrome");
                startActivity(i);
            }
        });
         Button update = (Button)findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Update.class);
                startActivity(i);
            }
        });

       /* data = new ArrayList<String>();
        data.add("노래끄기");
        data.add("Counting Stars");
        data.add("Heart Attack");
        data.add("See You Again");
        data.add("鏡花水月");
        data.add("Catch My Breath");
        data.add("One More Night");
        data.add("Hello Mr. My Yesterday");
        data.add("Komm Süsser Tod");
        data.add("VOODOO KINGDOM");
        data.add("Don't Cry");
        */

// Adapter 생성
        adapter = new ListViewAdapter() ;

        //어댑터 생성
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        //      this, android.R.layout.simple_list_item_1, data);
        listview = (ListView)findViewById(R.id.list);
        listview.setAdapter(adapter);

        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_stop_black_24dp),
                "노래 끄기", "") ;
        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.countingstars),
                "Counting Stars", "Alex Goot, Kurt Schneider, and Chrissy Costanza Cover") ;
        // 세 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.heartattack),
                "Heart Attack", "Sam Tsui & Chrissy Costanza of ATC") ;
        // 네 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.seeyouagain),
                "See You Again", "Against The Current Cover") ;
        // 다섯 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.kyoukasuigetsu),
                "鏡花水月", "まふまふ") ;
        // 여섯 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.catchmybreath),
                "Catch My Breath", "Alex Goot & Against The Current") ;
        // 일곱 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.onemorenight),
                "One More Night", "Alex Goot & Friends") ;
        // 여덟 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.hellomrmyyesterday),
                "Hello Mr. My Yesterday", "명탐정 코난 10기 Opening") ;
        // 아홉 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.kommsussertod),
                "Komm Süsser Tod", "The End Of Evangelion") ;
        // 열 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.voodookingdom),
                "VOODOO KINGDOM", "죠죠의 기묘한 모험 극장판 팬텀 블러드") ;
        // 열하나 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.dontcry),
                "Don't Cry", "하현우") ;





        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                List_Item item = (List_Item) adapterView.getItemAtPosition(i);
                String titlerStr = item.getTitle();
                String artistStr = item.getArtist();
                Drawable img = item.getImage();

                switch (i) {
                    case 0 :
                        if (!mp.isPlaying()) {
                            Toast.makeText(MainActivity.this, "끌 노래가 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            mp.stop();
                        }
                        break;
                    case 1 :
                        if (!mp.isPlaying()) {
                            Uri file = Uri.parse(URL + "1.mp3");
                            mp = MediaPlayer.create(MainActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                        } else {
                            mp.stop();
                            Uri file = Uri.parse(URL + "1.mp3");
                            mp = MediaPlayer.create(MainActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                        }
                        break;
                    case 2 :
                        if (!mp.isPlaying()) {
                            Uri file = Uri.parse(URL + "2.mp3");
                            mp = MediaPlayer.create(MainActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                        } else {
                            mp.stop();
                            Uri file = Uri.parse(URL + "2.mp3");
                            mp = MediaPlayer.create(MainActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                        }
                        break;
                    case 3 :
                        if (!mp.isPlaying()) {
                            Uri file = Uri.parse(URL + "3.mp3");
                            mp = MediaPlayer.create(MainActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                        } else {
                            mp.stop();
                            Uri file = Uri.parse(URL + "3.mp3");
                            mp = MediaPlayer.create(MainActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                        }
                        break;
                    case 4 :
                        if (!mp.isPlaying()) {
                            Uri file = Uri.parse(URL + "4.mp3");
                            mp = MediaPlayer.create(MainActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                        } else {
                            mp.stop();
                            Uri file = Uri.parse(URL + "4.mp3");
                            mp = MediaPlayer.create(MainActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                        }
                        break;
                    case 5 :
                        if (!mp.isPlaying()) {
                            Uri file = Uri.parse(URL + "5.mp3");
                            mp = MediaPlayer.create(MainActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                        } else {
                            mp.stop();
                            Uri file = Uri.parse(URL + "5.mp3");
                            mp = MediaPlayer.create(MainActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                        }
                        break;
                    case 6 :
                        if (!mp.isPlaying()) {
                            Uri file = Uri.parse(URL + "6.mp3");
                            mp = MediaPlayer.create(MainActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                        } else {
                            mp.stop();
                            Uri file = Uri.parse(URL + "6.mp3");
                            mp = MediaPlayer.create(MainActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                        }
                        break;
                    case 7 :
                        if (!mp.isPlaying()) {
                            Uri file = Uri.parse(URL + "7.mp3");
                            mp = MediaPlayer.create(MainActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                        } else {
                            mp.stop();
                            Uri file = Uri.parse(URL + "7.mp3");
                            mp = MediaPlayer.create(MainActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                        }
                        break;
                    case 8 :
                        if (!mp.isPlaying()) {
                            Uri file = Uri.parse(URL + "8.mp3");
                            mp = MediaPlayer.create(MainActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                        } else {
                            mp.stop();
                            Uri file = Uri.parse(URL + "8.mp3");
                            mp = MediaPlayer.create(MainActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                        }
                        break;
                    case 9 :
                        if (!mp.isPlaying()) {
                            Uri file = Uri.parse(URL + "9.mp3");
                            mp = MediaPlayer.create(MainActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                        } else {
                            mp.stop();
                            Uri file = Uri.parse(URL + "9.mp3");
                            mp = MediaPlayer.create(MainActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                        }
                        break;
                    case 10 :
                        if (!mp.isPlaying()) {
                            Uri file = Uri.parse(URL + "10.mp3");
                            mp = MediaPlayer.create(MainActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                        } else {
                            mp.stop();
                            Uri file = Uri.parse(URL + "10.mp3");
                            mp = MediaPlayer.create(MainActivity.this, file);
                            mp.setLooping(true);
                            mp.start();
                        }
                        break;

                }
                /*
                if (data.get(i) == "노래끄기") {
                    if (!mp.isPlaying()) {
                        Toast.makeText(MainActivity.this, "끌 노래가 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        mp.stop();
                    }
                }
                if (data.get(i) == "Counting Stars") {
                    if (!mp.isPlaying()) {
                        Uri file = Uri.parse(URL + "1.mp3");
                        mp = MediaPlayer.create(MainActivity.this, file);
                        mp.setLooping(true);
                        mp.start();
                    } else {
                        mp.stop();
                        Uri file = Uri.parse(URL + "1.mp3");
                        mp = MediaPlayer.create(MainActivity.this, file);
                        mp.setLooping(true);
                        mp.start();
                    }
                }
                if (data.get(i) == "Heart Attack") {
                    if (!mp.isPlaying()) {
                        Uri file = Uri.parse(URL + "2.mp3");
                        mp = MediaPlayer.create(MainActivity.this, file);
                        mp.setLooping(true);
                        mp.start();
                    } else {
                        mp.stop();
                        Uri file = Uri.parse(URL + "2.mp3");
                        mp = MediaPlayer.create(MainActivity.this, file);
                        mp.setLooping(true);
                        mp.start();
                    }
                }
                if (data.get(i) == "See You Again") {
                    if (!mp.isPlaying()) {
                        Uri file = Uri.parse(URL + "3.mp3");
                        mp = MediaPlayer.create(MainActivity.this, file);
                        mp.setLooping(true);
                        mp.start();
                    } else {
                        mp.stop();
                        Uri file = Uri.parse(URL + "3.mp3");
                        mp = MediaPlayer.create(MainActivity.this, file);
                        mp.setLooping(true);
                        mp.start();
                    }
                }
                if (data.get(i) == "鏡花水月") {
                    if (!mp.isPlaying()) {
                        Uri file = Uri.parse(URL + "4.mp3");
                        mp = MediaPlayer.create(MainActivity.this, file);
                        mp.setLooping(true);
                        mp.start();
                    } else {
                        mp.stop();
                        Uri file = Uri.parse(URL + "4.mp3");
                        mp = MediaPlayer.create(MainActivity.this, file);
                        mp.setLooping(true);
                        mp.start();
                    }
                }
                if (data.get(i) == "Catch My Breath") {
                    if (!mp.isPlaying()) {
                        Uri file = Uri.parse(URL + "5.mp3");
                        mp = MediaPlayer.create(MainActivity.this, file);
                        mp.setLooping(true);
                        mp.start();
                    } else {
                        mp.stop();
                        Uri file = Uri.parse(URL + "5.mp3");
                        mp = MediaPlayer.create(MainActivity.this, file);
                        mp.setLooping(true);
                        mp.start();
                    }
                }
                if (data.get(i) == "One More Night") {
                    if (!mp.isPlaying()) {
                        Uri file = Uri.parse(URL + "6.mp3");
                        mp = MediaPlayer.create(MainActivity.this, file);
                        mp.setLooping(true);
                        mp.start();
                    } else {
                        mp.stop();
                        Uri file = Uri.parse(URL + "6.mp3");
                        mp = MediaPlayer.create(MainActivity.this, file);
                        mp.setLooping(true);
                        mp.start();
                    }
                }
                if (data.get(i) == "Hello Mr. My Yesterday") {
                    if (!mp.isPlaying()) {
                        Uri file = Uri.parse(URL + "7.mp3");
                        mp = MediaPlayer.create(MainActivity.this, file);
                        mp.setLooping(true);
                        mp.start();
                    } else {
                        mp.stop();
                        Uri file = Uri.parse(URL + "7.mp3");
                        mp = MediaPlayer.create(MainActivity.this, file);
                        mp.setLooping(true);
                        mp.start();
                    }
                }
                if (data.get(i) == "Komm Süsser Tod") {
                    if (!mp.isPlaying()) {
                        Uri file = Uri.parse(URL + "8.mp3");
                        mp = MediaPlayer.create(MainActivity.this, file);
                        mp.setLooping(true);
                        mp.start();
                    } else {
                        mp.stop();
                        Uri file = Uri.parse(URL + "8.mp3");
                        mp = MediaPlayer.create(MainActivity.this, file);
                        mp.setLooping(true);
                        mp.start();
                    }
                }
                if (data.get(i) == "VOODOO KINGDOM") {
                    if (!mp.isPlaying()) {
                        Uri file = Uri.parse(URL + "9.mp3");
                        mp = MediaPlayer.create(MainActivity.this, file);
                        mp.setLooping(true);
                        mp.start();
                    } else {
                        mp.stop();
                        Uri file = Uri.parse(URL + "9.mp3");
                        mp = MediaPlayer.create(MainActivity.this, file);
                        mp.setLooping(true);
                        mp.start();
                    }
                }
                if (data.get(i) == "Don't Cry") {
                    if (!mp.isPlaying()) {
                        Uri file = Uri.parse(URL + "10.mp3");
                        mp = MediaPlayer.create(MainActivity.this, file);
                        mp.setLooping(true);
                        mp.start();
                    } else {
                        mp.stop();
                        Uri file = Uri.parse(URL + "10.mp3");
                        mp = MediaPlayer.create(MainActivity.this, file);
                        mp.setLooping(true);
                        mp.start();
                    }
                }
                */
            }
        });

        Spinner s = (Spinner) findViewById(R.id.bg);
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
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.setting) {
            Toast.makeText(this, "설정 테스트", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this, JoinActivity.class);
            startActivity(i);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    public void tumblr (View v) {
        Intent i = new Intent(MainActivity.this, Tumblr.class);
        startActivity(i);
    }


    public void hitomi (View v) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/0B4T5bWOlBITkRFFyb2Z6UHhKNVk/view"));
        startActivity(i);
    }
    public void random (View v) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://10000img.com"));
        startActivity(i);
    }
    public void chat (View v) {
        Intent i = new Intent(this, chatting.class);
        startActivity(i);
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
    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);


        // Setting Dialog Title
        // Setting Dialog Message

        alertDialog.setTitle("종료");

        // I've included a simple dialog icon in my project named "dialog_icon", which's image file is copied and pasted in all "drawable" folders of "res" folders of the project. You can include any dialog image of your wish and rename it to dialog_icon.

        alertDialog.setMessage("종료하시겠습니까?\n" +
                "종료시 노래도 같이 꺼집니다.");

        // Setting Icon to Dialog
        // Setting Positive "Yes" Button

        alertDialog.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (!mp.isPlaying()) {
                            finish();
                        } else {
                            mp.stop();
                            finish();
                        }
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
