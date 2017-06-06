package com.hellven.net;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by PC on 2017-06-05.
 */

public class JoinActivity extends AppCompatActivity {

    final Context context = this;

    public String url= "https://reqres.in/api/users/2";

    CheckBox Auto_Login;
    EditText id, pw;
    Button lg;

    SharedPreferences setting;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.enableDefaults();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy); // 강제적으로 네트워크 접속

        id = (EditText)findViewById(R.id.id);
        pw = (EditText)findViewById(R.id.pw);
        lg = (Button)findViewById(R.id.lg);
        Auto_Login = (CheckBox)findViewById(R.id.auto);

        setting = getSharedPreferences("setting",0);
        editor = setting.edit();

        if(setting.getBoolean("Auto_Login_enabled", false)){
            id.setText(setting.getString("ID", ""));
            pw.setText(setting.getString("PW", ""));
            Auto_Login.setChecked(true);
        }

        lg.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {

                                      Login();
                                  }
                              });


        
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://hellven.net")
                .build();

        Auto_Login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    String ID = id.getText().toString();
                    String PW = pw.getText().toString();

                    editor.putString("ID", ID);
                    editor.putString("PW", PW);
                    editor.putBoolean("Auto_Login_enabled", true);
                    editor.commit();
                }
                else {
                    editor.remove("ID");
                    editor.remove("PW");
                    editor.remove("Auto_Login_enabled");
                    editor.clear();
                    editor.commit();
                }
            }
        });

    }


    private void Login() {

        AlertDialog.Builder dialler = new AlertDialog.Builder(context);
        dialler.setTitle("실패");
        dialler.setMessage("지금은 로그인 할 수 없습니다.")
                .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = dialler.create();
        dialog.show();
    }

}