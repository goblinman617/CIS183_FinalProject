package com.example.ttrpgmanager;

import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    private int gameID;
    private String DMUsername;
    private String gameName;
    private ArrayList<Unit> units;
    private final int maxSize = 12;

    Game(){

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
        if (units.size() <= maxSize) {
            this.units = units;
        }
    }

    public void addUnit(Unit unit){
        if (units.size() < maxSize) {
            this.units.add(unit);
        }
    }

    public void removeUnitAtIndex(int i){
        if (units.get(i) != null){
            units.remove(i);
        }
    }
    //endregion
}
