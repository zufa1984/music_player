package uz.eskishahar.app.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.util.ArrayList;

import android.content.ContentUris;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.PowerManager;
import android.util.Log;

import java.util.Random;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private MediaPlayer player;

    private ArrayList<Music> musics;

    private int musicPosition;

    private final IBinder musicBind = new MusicBinder();

    private String musicTitle = "";
    private static final int NOTIFY_ID = 1;

    private boolean shuffle = false;
    private Random random;

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    public void onCreate() {
        super.onCreate();
        musicPosition = 0;
        player = new MediaPlayer();
        initializationMusicPlayer();
        random = new Random();
    }

    public void initializationMusicPlayer() {
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Music> theMusics) {
        musics = theMusics;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (player.getCurrentPosition() > 0) {
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    public void playMusic() {
        player.reset();
        Music playMusic = musics.get(musicPosition);
        long currMusic = playMusic.getId();
        musicTitle = playMusic.getTitle();
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currMusic);

        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public void setMusic(int musicIndex) {
        musicPosition = musicIndex;
    }


    public int getPosn() {
        return player.getCurrentPosition();
    }

    public int getDur() {
        return player.getDuration();
    }

    public boolean isPng() {
        return player.isPlaying();
    }

    public void pausePlayer() {
        player.pause();
    }

    public void seek(int posn) {
        player.seekTo(posn);
    }

    public void go() {
        player.start();
    }

    public void playPrev() {
        musicPosition--;
        if (musicPosition < 0) musicPosition = musics.size() - 1;
        playMusic();
    }

    public void playNext() {
        if (shuffle) {
            int newSong = musicPosition;
            while (newSong == musicPosition) {
                newSong = random.nextInt(musics.size());
            }
            musicPosition = newSong;
        } else {
            musicPosition++;
            if (musicPosition >= musics.size()) musicPosition = 0;
        }
        playMusic();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    public void setShuffle() {
        if (shuffle) shuffle = false;
        else shuffle = true;
    }
}
