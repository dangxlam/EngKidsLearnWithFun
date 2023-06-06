package com.liamdang.englishkidslearnwithfun.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email;
    private String password;
    private String user_name;
    public static ArrayList<Integer> scores;
    //public int[] scores;

    public User() {
    }

    public User (String email, String user_name) {
        this.email = email;
        this.user_name = user_name;
        scores = new ArrayList<>(6);

    }
    public User (String email, String user_name, ArrayList<Integer> scores) {
        this.email = email;
        this.user_name = user_name;
        this.scores = scores;

    }



    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setScores(ArrayList<Integer> scores) {
        this.scores = scores;
    }
    public void setTopicScore(int index, int score) {
        this.scores.set(index, score);
    }

    public int getTopicScore(int index) {
        return this.scores.get(index);
    }

    //public static void

    public ArrayList<Integer> getScores() {
        return scores;
    }
}
