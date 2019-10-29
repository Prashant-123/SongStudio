package com.songstudio.models;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import java.io.Serializable;

public class SongModel implements Serializable {
    private String song_name, url, artist, cover_image;


    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    @BindingAdapter({ "cover_image" })
    public static void loadImage(ImageView imageView, String imageURL) {
        Glide.with(imageView.getContext())
                .load(imageURL).override(1000).fitCenter()
                .into(imageView);
    }

    public SongModel() {
    }
}
