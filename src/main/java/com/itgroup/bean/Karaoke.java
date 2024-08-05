package com.itgroup.bean;

public class Karaoke {
    // 그냥 써본거유
    // 오늘 점심 뭐 먹지?
    // 철수가 먼저 푸시함
    // 수정
    private int id;
    private String songname;
    private String singer;
    private String genre;
    private String songdate;
    private String image;
    private String url;

    public Karaoke() {
    }

    public Karaoke(int id, String songname, String singer, String genre, String songdate, String image, String url) {
        this.id = id;
        this.songname = songname;
        this.singer = singer;
        this.genre = genre;
        this.songdate = songdate;
        this.image = image;
        this.url = url;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Karaoke(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSongdate() {
        return songdate;
    }

    public void setSongdate(String songdate) {
        this.songdate = songdate;
    }
}
