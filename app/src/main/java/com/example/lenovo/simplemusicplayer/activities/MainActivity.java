package com.example.lenovo.simplemusicplayer.activities;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lenovo.simplemusicplayer.R;
import com.example.lenovo.simplemusicplayer.entity.Mp3Info;
import com.example.lenovo.simplemusicplayer.helpers.MediaPlayerHelper;
import com.example.lenovo.simplemusicplayer.service.SystemService;
import com.example.lenovo.simplemusicplayer.util.mp3.MediaUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.Date;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.example.lenovo.simplemusicplayer.util.constant.MessageConst.DOWNLOADSIGNAL;
import static com.example.lenovo.simplemusicplayer.util.constant.MessageConst.UPDATETIMESIGNAL;

public  class MainActivity extends BaseActivity {


    private ImageView mIvBg;
    private AVLoadingIndicatorView mRolling;

    private MediaPlayerHelper mMediaPlayerHelper;
    private static boolean isPlaying = false;
    private String mPath;
    private volatile boolean exit = false;
    private static volatile boolean isSeek=false;


    private ImageButton startButton;
    private ImageButton pauseButton;
    private ImageButton lastButton;
    private ImageButton nextButton;
    private  SeekBar seekBar;
    private  TextView currentTimeText;
    private  TextView endTimeText;
    private TextView currentMusicText;

    private  Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            updateSeekText(msg.what);
            if (isSeek) return;
            seekBar.setProgress(msg.what);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //隐藏头部
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initWigets();
    }


    private void initWigets() {
        //页面组件初始化

        //背景模糊化
        mIvBg = fd(R.id.iv_bg);
        mRolling = fd(R.id.loading);
        mRolling.show();

        //播放Helper初始化
        mMediaPlayerHelper=MediaPlayerHelper.getInstance(getApplicationContext());

        Mp3Info mp3Info = (Mp3Info) getIntent().getSerializableExtra("mp3");
        int index = getIntent().getIntExtra("index",0);
        mMediaPlayerHelper.setCurrentIndex(index);
        mPath = mp3Info.getUrl();
        if (mPath==null){
            Toast.makeText(getApplicationContext(),"音乐不存在！",Toast.LENGTH_SHORT).show();
            finish();
        }

        Glide.with(this)
                .load(R.drawable.demo)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25,10)))
                .into(mIvBg);


        startButton = findViewById(R.id.btStart);
        pauseButton = findViewById(R.id.pause);
        seekBar = findViewById(R.id.seekbar);
        currentTimeText = findViewById(R.id.current_Time_Text);
        endTimeText = findViewById(R.id.end_Time_Text);
        currentMusicText = findViewById(R.id.current_music);

        currentMusicText.setText(mp3Info.getDisplayName()+"——"+mp3Info.getArtist());
        endTimeText.setText(MediaUtil.formatTime(mp3Info.getDuration()));
        seekBar.setMax((int) mp3Info.getDuration());

        //上一首,下一首
        lastButton=fd(R.id.before);
        nextButton=fd(R.id.next);
        lastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = mMediaPlayerHelper.getMp3Infos().size();
                int newIndex = (mMediaPlayerHelper.getCurrentIndex()-1)%size;
                if (newIndex<0) newIndex+=size;
                mMediaPlayerHelper.setCurrentIndex(newIndex);
                Mp3Info last = mMediaPlayerHelper.getMp3Infos().get(newIndex);
                mPath=last.getUrl();
                currentMusicText.setText(last.getDisplayName()+"——"+last.getArtist());
                endTimeText.setText(MediaUtil.formatTime(last.getDuration()));
                seekBar.setMax((int) last.getDuration());
                if (isPlaying){
                    mRolling.hide();
                    playMusic(mPath);
                }else {
                    trigger();
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = mMediaPlayerHelper.getMp3Infos().size();
                int newIndex = (mMediaPlayerHelper.getCurrentIndex()+1)%size;
                mMediaPlayerHelper.setCurrentIndex(newIndex);
                Mp3Info next = mMediaPlayerHelper.getMp3Infos().get(newIndex);
                mPath=next.getUrl();
                currentMusicText.setText(next.getDisplayName()+"——"+next.getArtist());
                endTimeText.setText(MediaUtil.formatTime(next.getDuration()));
                seekBar.setMax((int) next.getDuration());
                if (isPlaying){
                    mRolling.hide();
                    playMusic(mPath);
                }else {
                    trigger();
                }
            }
        });



        if (isPlaying){
            mRolling.hide();
            playMusic(mPath);
        }else {
            trigger();
        }


    }


    private void trigger(){
        if (isPlaying){
            stopMusic();
        }else {
            playMusic(mPath);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exit=true;
    }

    /**
     * 暂停音乐
     */
    public void stopMusic() {
        isPlaying = false;
        exit=true;

        startButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
        mMediaPlayerHelper.pause();
    }

    /**
     * 播放音乐
     * @param path
     */
    public void playMusic(String path){

        mPath = path;

        isPlaying = true;
        if (mMediaPlayerHelper.getPath()!=null&& mMediaPlayerHelper.getPath().equals(path)){
            mMediaPlayerHelper.start();
        }else {
            mMediaPlayerHelper.setPath(path);
            mMediaPlayerHelper.setOnBeforePlayerHelperListener(new MediaPlayerHelper.OnBeforePlayerHelperListener() {
                @Override
                public void onBefore(MediaPlayer mp) {
                    mRolling.show();
                }
            });
            mMediaPlayerHelper.setOnMediaPlayerHelperFinishHelper(new MediaPlayerHelper.OnMediaPlayerHelperFinishHelper() {
                @Override
                public void onCompleted(MediaPlayer mp) {
                    exit=true;

                    startButton.setVisibility(View.VISIBLE);
                    pauseButton.setVisibility(View.GONE);
                }
            });
            mMediaPlayerHelper.setOnMediaPlayerHelperListener(new MediaPlayerHelper.OnMediaPlayerHelperListener() {
                @Override
                public void onPrepared(final MediaPlayer mp) {

                    //播放
                    startButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            playMusic(mPath);
                        }
                    });
                    //暂停
                    pauseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            stopMusic();
                        }
                    });
                    mRolling.hide();
                    mMediaPlayerHelper.start();
                    updateSeekBar();
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                            if (fromUser){
                                mp.seekTo(progress);
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                            isSeek=true;
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            isSeek = false;
                        }
                    });
                }
            });
        }

        startButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);

        updateSeekBar();
    }

    /**
     * 返回键监听器
     * @param view
     */
    public void onBackClick(View view){
        onBackPressed();
    }

    private  void updateSeekText(long duration){
        currentTimeText.setText(MediaUtil.formatTime(duration));
    }
    private void updateSeekBar(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                exit=false;
                while (!exit){
                    try {
                        Message message = new Message();
                        message.what= (int) mMediaPlayerHelper.getCurrent();
                        handler.sendMessage(message);
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
