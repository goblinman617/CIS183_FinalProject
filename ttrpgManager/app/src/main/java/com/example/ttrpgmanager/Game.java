package com.example.ttrpgmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    private int gameID;
    private String DMUsername;
    private String gameName;
    private ArrayList<Unit> units;
    // We no longer have a max size of units

    Game(){
        units = new ArrayList<>();
    }
    Game(String uname, String gameName){
        DMUsername = uname;
        this.gameName = gameName;
        units = new ArrayList<>();
    }

    //region Getters and Setters
    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getDMUsername() {
        return DMUsername;
    }

    public void setDMUsername(String DMUsername) {
        this.DMUsername = DMUsername;
    }
    public String getGameName() { return gameName; }
    public void setGameName(String gameName) { this.gameName = gameName; }
    public ArrayList<Unit> getUnits() {
        return units;
    }
    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }
    //endregion
}
