package com.leecode1988.accountingapp.bean;

/**
 * 网易云热评实体类
 * author:LeeCode
 * create:2019/6/18 1:40
 */
public class HotReview {
    /**
     * {
     *   "song_id": 413812448,
     *   "title": "大鱼",
     *   "images": "https://p2.music.126.net/aiPQXP8mdLovQSrKsM3hMQ==/1416170985079958.jpg",
     *   "author": "周深",
     *   "album": "大鱼",
     *   "description": "歌手：周深。所属专辑：大鱼。",
     *   "mp3_url": "https://api.comments.hk/music/413812448",
     *   "pub_date": "2016-05-19 16:00:00",
     *   "comment_id": 157052468,
     *   "comment_user_id": 77262263,
     *   "comment_nickname": "筱白玉霜",
     *   "comment_avatar_url": "https://p2.music.126.net/0MFrJCYCrwkCUEyXtdJwSw==/18691697674000979.jpg",
     *   "comment_liked_count": 46058,
     *   "comment_content": "你如此苍白是不是 已倦于在空中攀登并凝望地面你倦于 孑然一生的漫游在 有不同身世的群星之间你盈缺无常像眼睛含着忧愁是因为 看不到什么值得凝眸吗",
     *   "comment_pub_date": "2016-05-19 16:12:19"
     * }
     */
    private long song_id;
    private String title;
    private String images;
    private String author;
    private String album;
    private String description;
    private String mp3_url;
    private String pub_date;
    private long comment_id;
    private long comment_user_id;
    private String comment_nickname;
    private String comment_avatar_url;
    private int comment_liked_count;
    private String comment_content;
    private String comment_pub_date;


    @Override public String toString() {
        return "HotReview{" +
            "song_id=" + song_id +
            ", title='" + title + '\'' +
            ", images='" + images + '\'' +
            ", author='" + author + '\'' +
            ", album='" + album + '\'' +
            ", description='" + description + '\'' +
            ", mp3_url='" + mp3_url + '\'' +
            ", pub_date='" + pub_date + '\'' +
            ", comment_id=" + comment_id +
            ", comment_user_id=" + comment_user_id +
            ", comment_nickname='" + comment_nickname + '\'' +
            ", comment_avatar_url='" + comment_avatar_url + '\'' +
            ", comment_liked_count=" + comment_liked_count +
            ", comment_content='" + comment_content + '\'' +
            ", comment_pub_date='" + comment_pub_date + '\'' +
            '}';
    }


    public long getSong_id() {
        return song_id;
    }

    public void setSong_id(long song_id) {
        this.song_id = song_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMp3_url() {
        return mp3_url;
    }

    public void setMp3_url(String mp3_url) {
        this.mp3_url = mp3_url;
    }

    public String getPub_date() {
        return pub_date;
    }

    public void setPub_date(String pub_date) {
        this.pub_date = pub_date;
    }

    public long getComment_id() {
        return comment_id;
    }

    public void setComment_id(long comment_id) {
        this.comment_id = comment_id;
    }

    public long getComment_user_id() {
        return comment_user_id;
    }

    public void setComment_user_id(long comment_user_id) {
        this.comment_user_id = comment_user_id;
    }

    public String getComment_nickname() {
        return comment_nickname;
    }

    public void setComment_nickname(String comment_nickname) {
        this.comment_nickname = comment_nickname;
    }

    public String getComment_avatar_url() {
        return comment_avatar_url;
    }

    public void setComment_avatar_url(String comment_avatar_url) {
        this.comment_avatar_url = comment_avatar_url;
    }

    public int getComment_liked_count() {
        return comment_liked_count;
    }

    public void setComment_liked_count(int comment_liked_count) {
        this.comment_liked_count = comment_liked_count;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getComment_pub_date() {
        return comment_pub_date;
    }

    public void setComment_pub_date(String comment_pub_date) {
        this.comment_pub_date = comment_pub_date;
    }
}
