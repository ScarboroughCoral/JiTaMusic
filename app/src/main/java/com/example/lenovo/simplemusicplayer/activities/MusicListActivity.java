package com.example.lenovo.simplemusicplayer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.simplemusicplayer.R;
import com.example.lenovo.simplemusicplayer.adapters.MusicListAdapter;
import com.example.lenovo.simplemusicplayer.entity.Mp3Info;
import com.example.lenovo.simplemusicplayer.helpers.MediaPlayerHelper;
import com.example.lenovo.simplemusicplayer.util.mp3.MediaUtil;

import java.util.List;

public class MusicListActivity extends BaseActivity {

    private ListView lvMusicList;
    private MusicListAdapter musicListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        initView();
    }

    private void initView() {
        initNavBar(false,"播放列表",true);

        OnImportMusicListener onImportMusicListener = new OnImportMusicListener() {

            @Override
            public void onImport(final List<Mp3Info> mp3Infos) {
                Toast.makeText(getApplicationContext(),"导入成功！",Toast.LENGTH_SHORT).show();
                lvMusicList=fd(R.id.lv_music);
                musicListAdapter = new MusicListAdapter(mp3Infos,getLayoutInflater());
                lvMusicList.setAdapter(musicListAdapter);
                MediaPlayerHelper.getInstance(getApplicationContext()).setMp3Infos(mp3Infos);

                lvMusicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MusicListActivity.this,MainActivity.class);
                        Mp3Info mp3Info = mp3Infos.get(position);
                        intent.putExtra("mp3",mp3Info);
                        intent.putExtra("index",position);
                        startActivity(intent);
                    }
                });
            }
        };
        super.setOnImportMusicListener(onImportMusicListener);

        List<Mp3Info> mp3Infos = MediaUtil.getMusicInfo(getApplicationContext());
        onImportMusicListener.onImport(mp3Infos);

    }
}
