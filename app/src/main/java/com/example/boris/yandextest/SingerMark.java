package com.example.boris.yandextest;

import java.util.ArrayList;
import java.util.logging.StreamHandler;

/**
 * Created by Boris on 03.04.2016.
 */
public class SingerMark {
    private String imageSmallUrl;
    private String imageBigUrl;
    private String name;
    private String tracks;
    private String description;
    private String link;
    private String albums;
    private ArrayList<String> genres;

    //Геттеры и сеттеры
    public String getImageBigUrl() {
        return imageBigUrl;
    }

    public void setImageBigUrl(String imageBigUrl) {
        this.imageBigUrl = imageBigUrl;
    }

    public String getImageSmallUrl() {
        return imageSmallUrl;
    }

    public void setImageSmallUrl(String imageSmallUrl) {
        this.imageSmallUrl = imageSmallUrl;
    }

    public String getName() {
        return name;
    }

    public void setTracks(String tracks) {
        this.tracks = tracks;
    }

    public String getTracks() {
        return tracks;
    }

    public void setAlbums(String albums) {
        this.albums = albums;
    }

    public String getAlbums() {
        return albums;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() { return description; }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink(){ return link; }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

}
