package com.hellven.net;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by PC on 2017-05-30.
 */

public class Update extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);

        TextView tv = (TextView) findViewById(R.id.tv1);

        tv.setText("본 앱은 공식 앱이 아닙니다.\n" +
                "본 앱을 사용해 피해를 볼 경우 책임지지 않습니다.\n" +
                "본 앱은 개인정보를 탈취하지 않습니다.\n" +
                "개인 개발입니다. 개인개발이라 디자인 및 아이디어가 고갈되어있습니다.\n" +
                "개발자의 능력 부족으로 여러 기능들을 구현하지 못합니다");

    }
}
