package uz.eskishahar.app.musicplayer;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MusicAdapter extends BaseAdapter {

    private ArrayList<Music> musics;
    private LayoutInflater musicInf;

    public MusicAdapter(Context c, ArrayList<Music> theSongs) {
        musics = theSongs;
        musicInf = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return musics.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout musicLayout = (LinearLayout) musicInf.inflate
                (R.layout.music, parent, false);

        TextView musicView = (TextView) musicLayout.findViewById(R.id.music_title);
        TextView singerView = (TextView) musicLayout.findViewById(R.id.music_singer);

        Music currMusic = musics.get(position);

        musicView.setText(currMusic.getTitle());
        singerView.setText(currMusic.getSinger());

        musicLayout.setTag(position);
        return musicLayout;
    }
}
