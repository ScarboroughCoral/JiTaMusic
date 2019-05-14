package com.example.lenovo.simplemusicplayer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.simplemusicplayer.R;
import com.example.lenovo.simplemusicplayer.entity.Mp3Info;

import java.util.List;

public class MusicListAdapter extends BaseAdapter {
    private List<Mp3Info> musicList;
    private LayoutInflater layoutInflater;

    public MusicListAdapter(List<Mp3Info> musicList,LayoutInflater layoutInflater) {
        this.musicList = musicList;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return musicList.size();
    }

    @Override
    public Object getItem(int position) {
        return musicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = layoutInflater.inflate(R.layout.list_item,null);
        ImageView ivMusicImage = v.findViewById(R.id.iv_music_img);
        TextView tvMusicName = v.findViewById(R.id.tv_music_name);
        TextView tvMusicAuthor = v.findViewById(R.id.tv_music_author);
        ImageView ivMusicPlay = v.findViewById(R.id.iv_play);

        String name = musicList.get(position).getDisplayName();
        if (name==null||name.length()==0){
            musicList.get(position).setDisplayName("<未知歌曲>");
        }
        String author = musicList.get(position).getArtist();
        if (author.equals("<unknown>"))
            musicList.get(position).setArtist("<未知歌手>");
        tvMusicAuthor.setText(musicList.get(position).getArtist());
        tvMusicName.setText(musicList.get(position).getDisplayName());
        return v;
    }
}
