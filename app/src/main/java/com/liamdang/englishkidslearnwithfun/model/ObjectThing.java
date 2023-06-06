package com.liamdang.englishkidslearnwithfun.model;

import android.net.Uri;

import java.net.URL;

public class ObjectThing {
    private int image;
    private int sound;
    private String vietSub;
    private String text;
    private int noise;


    private String urlImage;


    public ObjectThing(int image, int sound,String vietSub, String text, int noise) {
        this.image = image;
        this.sound = sound;
        this.vietSub = vietSub;
        this.text = text;
        this.noise = noise;
    }

    public ObjectThing( String imgUrl, String vietSub, String text, int noise) {
        this.image = 0;
        this.sound = 0;

        this.urlImage = imgUrl;

        this.vietSub = vietSub;
        this.text = text;
        this.noise = noise;
    }

    public ObjectThing() {

    }

    public ObjectThing(int image, int sound, String vietSub, String text) {
        this(image, sound, vietSub, text, 0);
    }

    public ObjectThing(String imageUrl, String vietSub, String text) {
        this(imageUrl, vietSub, text, 0);
    }

    public String getVietSub() {
        return vietSub;
    }

    public void setVietSub(String vietSub) {
        this.vietSub = vietSub;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNoise() {
        return noise;
    }

    public boolean hasNoise() {
        return this.noise != 0;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getUrlImage() {
        return urlImage;
    }
}
