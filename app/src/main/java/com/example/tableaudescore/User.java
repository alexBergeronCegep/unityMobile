package com.example.tableaudescore;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    int id;

    @SerializedName("user")
    String user;

    @SerializedName("password")
    String password;

    @SerializedName("score")
    int score;

    public User(int id, String user, String password, int score)
    {
        this.id = id;
        this.user = user;
        this.password = password;
        this.score = score;
    }

    public String getUser() {
        return user;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public int getScore() {
        return score;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String heure()
    {
        int score = getScore();
        int milisec = score % 100;
        int sec = (score / 100) % 60;
        int min = (score / 100) / 60 % 60;
        int hour = (score / 100) / 60 / 60 % 60;
        return hour + "h " + min + "m " + sec + "s, " + milisec + "ms";
    }
}
