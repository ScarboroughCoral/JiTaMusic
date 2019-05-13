package com.example.lenovo.simplemusicplayer.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.simplemusicplayer.R;
import com.example.lenovo.simplemusicplayer.entity.Mp3Info;
import com.example.lenovo.simplemusicplayer.util.mp3.MediaUtil;

import java.util.List;

public class BaseActivity extends Activity {

    private ImageView mIvBack,mIvList;
    private TextView mTvTitle;
    protected List<Mp3Info> mp3Infos;
    private OnImportMusicListener onImportMusicListener;

    public void setOnImportMusicListener(OnImportMusicListener onImportMusicListener) {
        this.onImportMusicListener = onImportMusicListener;
    }

    /**
     * findViewById
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T fd(@IdRes int id){
        return findViewById(id);
    }

    /**
     * 初始化NavBar
     * @param isShowBack
     * @param title
     * @param isShowList
     */
    protected void initNavBar(boolean isShowBack, String title,boolean isShowList){

        mIvBack=fd(R.id.iv_back);
        mIvList=fd(R.id.iv_list);
        mTvTitle=fd(R.id.tv_title);

        mIvBack.setVisibility(isShowBack ? View.VISIBLE : View.GONE);
        mIvList.setVisibility(isShowList ? View.VISIBLE : View.GONE);
        mTvTitle.setText(title);

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mIvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(BaseActivity.this,MusicListActivity.class));
                List<Mp3Info> mp3Infos = MediaUtil.getMusicInfo(getApplicationContext());
                if (onImportMusicListener!=null){
                    onImportMusicListener.onImport(mp3Infos);
                }
                for (Mp3Info mp3Info:mp3Infos){
                    Log.e("_____________",mp3Info.toString());
                }


            }
        });
    }

    public interface  OnImportMusicListener{
        void onImport(List<Mp3Info> mp3Infos);
    }
}
