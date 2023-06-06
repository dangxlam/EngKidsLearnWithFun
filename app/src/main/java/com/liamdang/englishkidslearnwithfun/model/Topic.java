package com.liamdang.englishkidslearnwithfun.model;

import android.net.Uri;

import com.liamdang.englishkidslearnwithfun.model.HighScores;
import com.liamdang.englishkidslearnwithfun.model.ObjectThing;

import java.net.URL;
import java.util.ArrayList;

public class Topic {

    public ArrayList<ObjectThing> listOfThings;
    public int currentIndex = 0;
    public String title;
    public String viet_title;
    public int image;
    public int color;
    public int highScore;
    public int theme;
    public String columnName;



    public String imageUrl;

    public Topic(String title, String viet_title, int image, int highScore, int color, int theme, String columnName) {
        this.title = title;
        this.viet_title = viet_title;
        this.image = image;
        this.highScore = highScore;
        this.color = color;
        this. theme = theme;
        this.columnName = columnName;

        this.listOfThings = new ArrayList<>();
    }

    public Topic(String title, String viet_title, String imageUrl, int highScore, int color, int theme, String columnName) {
        this.title = title;
        this.viet_title = viet_title;

        this.imageUrl = imageUrl;
        this.highScore = highScore;
        this.color = color;
        this. theme = theme;
        this.columnName = columnName;

        this.listOfThings = new ArrayList<>();
    }

    public Topic(String title, String viet_title, String imagUrl, int highScore, int color, int theme, String columnName, ArrayList<ObjectThing> list_obj) {
        this.title = title;
        this.viet_title = viet_title;

        this.imageUrl = imagUrl;
        this.highScore = highScore;
        this.color = color;
        this.theme = theme;
        this.columnName = columnName;

        this.listOfThings = list_obj;
    }

    public Topic() {

    }

    public void addObject(ObjectThing object) { listOfThings.add(object); }

    public ArrayList<ObjectThing> getListOfThings () { return  this.listOfThings ;}

    public ObjectThing nextObject() {
        return listOfThings.get(++currentIndex);
    }

    public ObjectThing prevObject() {
        return listOfThings.get(--currentIndex);
    }

    public ObjectThing currentObj() {
        return listOfThings.get(currentIndex);
    }

    public boolean hasNextObj () {
        return currentIndex < listOfThings.size() -1;
    }

    public boolean hasPrevObj() {
        return currentIndex > 0;
    }

    public void goFirstObj() {
        currentIndex = 0;
    }

    public void updateHighScore() {
        this.highScore = HighScores.getHighScore(this.columnName);
    }

    public void setListOfThings(ArrayList<ObjectThing> listOfThings) {
        this.listOfThings = listOfThings;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setViet_title(String viet_title) {
        this.viet_title = viet_title;
    }

    public String getViet_title() {
        return viet_title;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }



    public String getUrl_Image() {
        return imageUrl;
    }



    public void setUrl_Image(String url_Image) {
        this.imageUrl = url_Image;
    }
}
