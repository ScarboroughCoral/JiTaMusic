package com.example.lenovo.simplemusicplayer.helpers;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.SeekBar;

import com.example.lenovo.simplemusicplayer.entity.Mp3Info;

import java.io.IOException;
import java.util.List;

public class MediaPlayerHelper {
    private static MediaPlayerHelper instance;
    private List<Mp3Info> mp3Infos;
    private int currentIndex;

    private Context context;
    private MediaPlayer mediaPlayer;

    private OnMediaPlayerHelperListener onMediaPlayerHelperListener;
    private OnMediaPlayerHelperFinishHelper onMediaPlayerHelperFinishHelper;
    private OnBeforePlayerHelperListener onBeforePlayerHelperListener;


    private String mPath;

    public void setMp3Infos(List<Mp3Info> mp3Infos) {
        this.mp3Infos = mp3Infos;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public List<Mp3Info> getMp3Infos() {
        return mp3Infos;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setOnMediaPlayerHelperListener(OnMediaPlayerHelperListener onMediaPlayerHelperListener) {
        this.onMediaPlayerHelperListener = onMediaPlayerHelperListener;
    }

    public void setOnMediaPlayerHelperFinishHelper(OnMediaPlayerHelperFinishHelper onMediaPlayerHelperFinishHelper) {
        this.onMediaPlayerHelperFinishHelper = onMediaPlayerHelperFinishHelper;
    }

    public void setOnBeforePlayerHelperListener(OnBeforePlayerHelperListener onBeforePlayerHelperListener) {
        this.onBeforePlayerHelperListener = onBeforePlayerHelperListener;
    }

    public static MediaPlayerHelper getInstance(Context context){
        if (instance==null){
            synchronized (MediaPlayerHelper.class){
                instance=new MediaPlayerHelper(context);
            }
        }
        return instance;
    }

    private MediaPlayerHelper(Context context){
        this.context=context;
        mediaPlayer = new MediaPlayer();

    }

    public long getCurrent(){
        return mediaPlayer.getCurrentPosition();
    }
    public void reset(){
        mediaPlayer.reset();
    }
    /**
     * 设置播放音乐并准备播放
     * @param path
     */
    public void setPath(String path){
        mPath = path;
        //1.如果正在播放则重置
//        if (mediaPlayer.isPlaying()){
//            mediaPlayer.reset();
//        }

        mediaPlayer.reset();
        if (onBeforePlayerHelperListener!=null){
            onBeforePlayerHelperListener.onBefore(mediaPlayer);
        }
        //2.设置播放源
        try {
            mediaPlayer.setDataSource(context, Uri.parse(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //3.准备播放
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (onMediaPlayerHelperListener!=null){
                    onMediaPlayerHelperListener.onPrepared(mp);
                }
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (onMediaPlayerHelperFinishHelper!=null){
                    onMediaPlayerHelperFinishHelper.onCompleted(mp);
                }
            }
        });
    }


    public String getPath() {
        return mPath;
    }

    /**
     * 播放音乐
     */
    public void start(){
        if (mediaPlayer.isPlaying()) return;
        mediaPlayer.start();
    }

    /**
     * 暂停播放
     */

    public void pause(){
        mediaPlayer.pause();
    }


    public interface OnBeforePlayerHelperListener{
        void onBefore(MediaPlayer mp);
    }
    public interface OnMediaPlayerHelperListener{
        void onPrepared(MediaPlayer mp);
    }

    public interface OnMediaPlayerHelperFinishHelper{
        void onCompleted(MediaPlayer mp);
    }
}
