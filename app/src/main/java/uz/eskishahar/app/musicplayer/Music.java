package uz.eskishahar.app.musicplayer;

public class Music {
    private long id;
    private String title;
    private String singer;

    public Music(long id, String title, String singer) {
        this.id = id;
        this.title = title;
        this.singer = singer;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSinger() {
        return singer;
    }
}
