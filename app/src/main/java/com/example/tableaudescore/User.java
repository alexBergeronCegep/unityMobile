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
        int sec = getScore();
        int heure = sec / 3600;
        int min = sec % 3600 / 60;
        return heure + "h " + min + "m ";
    }
}
