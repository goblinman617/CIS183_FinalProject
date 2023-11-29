package com.example.ttrpgmanager;

import java.util.ArrayList;

public class Game {
    private int gameID;
    private String DMUsername;
    private ArrayList<Unit> units;

    private final int maxSize = 12;

    Game(){
        // Create the arrayList in memory
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
