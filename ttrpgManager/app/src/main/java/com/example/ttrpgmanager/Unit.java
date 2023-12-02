package com.example.ttrpgmanager;


import java.io.Serializable;

public class Unit implements Serializable {
    private int unitID;
    private int gameID;
    private boolean NPC;
    private String name;
    private int maxHealth;
    private int curHealth;
    private int initiative;

    Unit(){

    }

    //region Getters and Setters
    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public boolean isNPC() {
        return NPC;
    }

    public void setNPC(boolean NPC) {
        this.NPC = NPC;
    }

    public void setNPC(int NPC){
        if (NPC == 1){
            this.NPC = true;
        }else {
            this.NPC = false;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getCurHealth() {
        return curHealth;
    }

    public void setCurHealth(int curHealth) {
        this.curHealth = curHealth;
    }

    public int getInitiative() {
        return initiative;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }
    //endregion
}
