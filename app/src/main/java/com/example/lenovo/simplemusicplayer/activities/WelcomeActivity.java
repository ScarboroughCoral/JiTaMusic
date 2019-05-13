package com.example.lenovo.simplemusicplayer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lenovo.simplemusicplayer.R;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends BaseActivity {

    //3s
    //跳转
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        init();

    }

    /**
     * 初始化
     */
    private void init() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toMain();
                finish();
            }
        },3000);
    }

    /**
     * 跳转到主页面
     */
    private void toMain() {
        Intent intent = new Intent(this,MusicListActivity.class);
        startActivity(intent);
    }


}
