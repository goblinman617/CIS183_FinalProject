package com.example.ttrpgmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Rpg.db";
    private static final String USERS_TABLE = "Users";
    private static final String GAMES_TABLE = "Games";
    private static final String ACTIVE_UNITS_TABLE = "ActiveUnits";

    // Honestly we might not even end up using the NPC_Prefabs table
    // I imagine it working like: DM goes to create a new unit, they fill in name & stats, choose if new unit is NPC or not, if the unit is an
    // NPC they could "save NPC for future use". If they wanted to add that same NPC to the scene again they wouldn't need to fill in its stats.
    private static final String NPC_PREFABS_TABLE = "NpcPrefabs";

    //=================
    //region Database
    public DatabaseHelper(Context context) {
        // change version to recreate the database
        super(context, DATABASE_NAME, null, 8);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // A foreign key should make it so that that field has to match a primary key in a different table

        // AUTOINCREMENT is a SQLite keyword that should handle the creation of the primary key and make sure it doesn't repeat
        // Documentation: https://www.sqlite.org/autoinc.html

        db.execSQL("CREATE TABLE " + USERS_TABLE + " (username TEXT PRIMARY KEY NOT NULL, password TEXT NOT NULL);");

        // u1 (unit1) will contain a UnitID or it will be null (no unit)
        // currently the Games table is setup to hold 12 units (a mix of players and NPCS)
        db.execSQL("CREATE TABLE " + GAMES_TABLE + " (gameID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " DMUsername TEXT NOT NULL, gameName TEXT, FOREIGN KEY (DMUsername)" +
                " REFERENCES " + USERS_TABLE + "(username));");

        db.execSQL("CREATE TABLE " + ACTIVE_UNITS_TABLE + " (unitID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " gameID INT NOT NULL, isNpc INT, name TEXT, maxHealth INT, currentHealth INT," +
                " initiative INT, myTurn INT, FOREIGN KEY (gameID) REFERENCES " + GAMES_TABLE + "(gameID));");

        //we may not even end up using this table. I'll see what the designer wants
        db.execSQL("CREATE TABLE " + NPC_PREFABS_TABLE + " (gameID INT NOT NULL, name TEXT NOT NULL," +
                " maxHealth INT, PRIMARY KEY(gameID, name), FOREIGN KEY (gameID) REFERENCES "
                + GAMES_TABLE + "(gameID));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //drop all tables
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + GAMES_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + ACTIVE_UNITS_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + NPC_PREFABS_TABLE + ";");

        // create new tables
        onCreate(db);
    }


    // I changed this function a bit so that it will work with all tables
    //tables with default information
    public boolean initializeTables()
    {
        boolean initTables = false;

        if(rowsInUsersTable() == 0)
        {
            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL("INSERT INTO " + USERS_TABLE + " VALUES('DMleo', 'pass123');");

            db.close();
            initTables = true;
        }
        if(rowsInGamesTable() == 0)
        {
            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL("INSERT INTO " + GAMES_TABLE + " (DMUsername, gameName) VALUES('DMleo', 'Tower of Sycrus');");

            db.close();
            initTables = true;
        }
        if(rowsInUnitsTable() == 0)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            //how to handle the booleans??? once the isNpc bool works then units listview should work i think?
            db.execSQL("INSERT INTO " + ACTIVE_UNITS_TABLE + " (gameID, isNpc, name, maxHealth, currentHealth, initiative, myTurn) " +
                    " VALUES(1, 0, 'Noah', 10, 10, 7, 0);");

            db.execSQL("INSERT INTO " + ACTIVE_UNITS_TABLE + " (gameID, isNpc, name, maxHealth, currentHealth, initiative, myTurn) " +
                    " VALUES(1, 1, 'Xande', 10, 0, 10, 1);");

            db.close();
            initTables = true;
        }

        return initTables;
    }
    //endregion
    //=================

    //=================
    //region UserTable
    //checks if anything is in the users table
    public int rowsInUsersTable()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        int numRows = (int) DatabaseUtils.queryNumEntries(db, USERS_TABLE);

        db.close();

        return numRows;
    }

    //to store the user table
    @SuppressLint("Range")
    public ArrayList<User> getAllUserRows()
    {
        ArrayList<User> listOfUsers = new ArrayList<User>();
        //select from table
        String selectQry = "SELECT * FROM " + USERS_TABLE + ";";
        //reading the database
        SQLiteDatabase db = this.getReadableDatabase();
        //cursor to cycle through
        Cursor cursor = db.rawQuery(selectQry, null);

        if(cursor.moveToFirst())
        {
            do
            {
                // Object must be created in the while loop
                // I broke this earlier.
                User tempUser = new User();

                tempUser.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                tempUser.setPassword(cursor.getString(cursor.getColumnIndex("password")));

                listOfUsers.add(tempUser);
            }
            while(cursor.moveToNext());
        }
        db.close();

        return listOfUsers;
    }

    // Returns null on no user found
    @SuppressLint("Range")
    public User getUser(String username){
        if (username.equals(null)){
            return null;
        }

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + USERS_TABLE + " WHERE username = '" + username + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);

        User user = new User();

        if (cursor.moveToFirst()){
            user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));

            return user;
        }
        Log.d("db error check", "no user found");
        return null;
    }
    public boolean registerUser(User newUser){
        if (newUser.getUsername().equals("") || newUser.getPassword().equals("")){
            Log.d("db error check", "username or password is empty");
            return false;
        }

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT username FROM " + USERS_TABLE + " WHERE username = '" + newUser.getUsername() + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Username is taken
        if (cursor.moveToFirst()){
            db.close();
            Log.d("db error check", "Registering username is taken");
            return false;
        }
        // close readable db
        db.close();
        db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " + USERS_TABLE + " VALUES ('" + newUser.getUsername()
                + "','" + newUser.getPassword() + "');");

        db.close();
        return true;
    }

    // returns true on successful delete
    public boolean deleteUser(User curUser){

        int rowsBefore = rowsInUsersTable();

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + USERS_TABLE + " WHERE username = '" + curUser.getUsername() + "';");
        db.close();

        int rowsAfter = rowsInUsersTable();

        if (rowsBefore > rowsAfter){
            Log.d("db success", (rowsBefore - rowsAfter)+ " user's deleted");
            return true;
        }
        return false;
    }

    public void updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE " + USERS_TABLE + " SET password = '" + user.getPassword() + "' WHERE username = '" + user.getUsername() + "';");

        db.close();
    }

    // returns true if username and password are correct
    public boolean validateLogin(User curUser){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT username FROM " + USERS_TABLE + " WHERE username = '" + curUser.getUsername()
                + "' AND password = '" + curUser.getPassword() + "';";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            db.close();
            return true;
        }
        db.close();
        return false;
    }

    //endregion
    //=================

    //=================
    //region GamesTable

    // 'Game' must contain a DMUsername and gameName
    public boolean createNewGame(Game game){
        if (game.getGameName().equals("")){
            Log.d("db error check", "Empty game name");
            return false;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT gameID FROM " + GAMES_TABLE + " WHERE DMUsername = '" + game.getDMUsername() + "' AND gameName = '"
                + game.getGameName() + "';";

        Cursor cursor = db.rawQuery(selectQuery, null);

        // If there is a game with a matching DMUsername and gameName
        if(cursor.moveToFirst()){
            db.close();
            Log.d("db error check", "User already has a game with that name");
            return false;
        }

        db.close();
        db = this.getWritableDatabase();

        String insertStatement = "INSERT INTO " + GAMES_TABLE + " (DMUsername, gameName)" +
                " VALUES('" + game.getDMUsername() + "','" + game.getGameName() + "');";

        db.execSQL(insertStatement);

        db.close();
        return true;
    }

    // 'Game' contains gameID or DMUsername and gameName
    public boolean deleteGame(Game game){
        if (game.getGameID() == 0){ // int defaults to 0. The autoincrement starts at 1
            game = fillGameIDByNames(game); // fillGameIDByNames returns null if no game is found
            if (game == null){
                Log.d("db error check", "No matching game");
                return false;
            }
        }

        int gameRowsBefore = rowsInGamesTable();

        // delete game by gameID
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + GAMES_TABLE + " WHERE gameID = '" + game.getGameID() + "';");
        db.close();

        int gameRowsAfter = rowsInGamesTable();

        deleteUnitsFrom(game.getGameID());

        if (gameRowsBefore > gameRowsAfter) {
            Log.d("db success", (gameRowsBefore - gameRowsAfter) + " games deleted");
            return true;
        }

        return false;
    }

    public void updateGame(Game game){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE " + GAMES_TABLE + " SET gameName = '" + game.getGameName() + "' WHERE gameID = " + game.getGameID() + ";");

        db.close();
    }

    // returns games owned by username
    @SuppressLint("Range")
    public ArrayList<Game> getUsersGames(String currentUsername)
    {
        ArrayList<Game> listOfGames = new ArrayList<Game>();
        String selectQuery = "SELECT * FROM " + GAMES_TABLE + " WHERE DMUsername = '" + currentUsername + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if(cursor.moveToFirst())
        {
            do
            {
                // This does need to be in the do while loop
                // I put it in the wrong spot, sorry
                Game tempGame = new Game();

                tempGame.setGameID(cursor.getInt(cursor.getColumnIndex("gameID")));
                tempGame.setDMUsername(cursor.getString(cursor.getColumnIndex("DMUsername")));
                tempGame.setGameName(cursor.getString(cursor.getColumnIndex("gameName")));

                listOfGames.add(tempGame);
            }
            while(cursor.moveToNext());
        }
        db.close();

        return listOfGames;
    }

    // returns matching game by gameID or DMUsername and gameName
    @SuppressLint("Range")
    public Game buildGame(Game game){
        if (game.getGameID() == 0){ // int defaults to 0. The autoincrement starts at 1
            game = fillGameIDByNames(game);
            if (game == null){
                Log.d("db error check", "No matching game");
                return null;
            }
        }

        String selectQuery = "SELECT * FROM " + GAMES_TABLE + " WHERE gameID = " + game.getGameID() + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);



        if (cursor.moveToFirst()) {
            // Game has ID
            game.setDMUsername(cursor.getString(cursor.getColumnIndex("DMUsername")));
            game.setGameName(cursor.getString(cursor.getColumnIndex("gameName")));

            // Close DB so we can run another DB function
            db.close();

            game.setUnits(getUnitsByGameID(game.getGameID()));

            setUnitTurnIfNoneExists(game.getGameID());
        }
        db.close();

        return game;
    }

    public int rowsInGamesTable(){
        SQLiteDatabase db = this.getReadableDatabase();

        int numRows = (int) DatabaseUtils.queryNumEntries(db, GAMES_TABLE);

        db.close();

        return numRows;
    }

    //region Private Functions
    @SuppressLint("Range")
    private Game fillGameIDByNames(Game game){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT gameID FROM " + GAMES_TABLE + " WHERE DMUsername = '" + game.getDMUsername()
                + "' AND gameName = '" + game.getGameName() + "';";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            game.setGameID(cursor.getInt(cursor.getColumnIndex("gameID")));

            db.close();
            return game;
        }
        db.close();
        Log.d("Couldn't find game by username and gameName", "Game is null");
        return null;
    }
    //endregion
    //endregion
    //=================

    //=================
    //region UnitsTable
    public int rowsInUnitsTable()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        int numRows = (int) DatabaseUtils.queryNumEntries(db, ACTIVE_UNITS_TABLE);

        db.close();

        return numRows;
    }

    // This is your function. I copied and pasted the whole thing. sorry...
    @SuppressLint("Range")
    public ArrayList<Unit> getUnitsByGameID(int gameID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Unit> listOfUnits = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ACTIVE_UNITS_TABLE + " WHERE gameID = " + gameID + " ORDER BY initiative DESC, name DESC;";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst())
        {
            do
            {
                Unit tempUnit = new Unit();

                tempUnit.setUnitID(cursor.getInt(cursor.getColumnIndex("unitID")));
                tempUnit.setGameID(cursor.getInt(cursor.getColumnIndex("gameID")));
                tempUnit.setNPC(cursor.getInt(cursor.getColumnIndex("isNpc")));
                tempUnit.setName(cursor.getString(cursor.getColumnIndex("name")));
                tempUnit.setMaxHealth(cursor.getInt(cursor.getColumnIndex("maxHealth")));
                tempUnit.setCurHealth(cursor.getInt(cursor.getColumnIndex("currentHealth")));
                tempUnit.setInitiative(cursor.getInt(cursor.getColumnIndex("initiative")));
                tempUnit.setMyTurn(cursor.getInt(cursor.getColumnIndex("myTurn")));

                listOfUnits.add(tempUnit);
            }
            while(cursor.moveToNext());
        }

        db.close();

        return listOfUnits;
    }

    public void advanceTurn(Game game){
        ArrayList<Unit> unitList = getUnitsByGameID(game.getGameID());

        int unitID[] = new int[2];

        for (int i = 0; i < unitList.size(); i++){
            if (unitList.get(i).isMyTurn()){
                unitID[0] = unitList.get(i).getUnitID();

                // if
                if (i+1 == unitList.size()){
                    i=0;
                }else{
                    i++;
                }

                unitID[1] = unitList.get(i).getUnitID();

                break;
            }
        }

        swapTurnState(unitID[0], unitID[1]);

        SQLiteDatabase db = this.getWritableDatabase();

    }

    // no error checking besides missing game ID
    public boolean addUnit(Unit u){
        if (u.getGameID() == 0){
            Log.d("db error check", "no game id");
            return false;
        }
        // all other fields should be error checked when filled out

        String insertQuery = "INSERT INTO " + ACTIVE_UNITS_TABLE + " (gameID, isNPC, name, maxHealth, currentHealth, initiative, myTurn) " +
                "VALUES(" + u.getGameID() + "," + u.isNPC() + ",'" + u.getName()
                + "'," + u.getMaxHealth() + "," + u.getCurHealth() + ","+ u.getInitiative() + "," + u.isMyTurn() + ");";
        // boolean values are stored as 0,1 in the database

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(insertQuery);
        db.close();

        return true;
    }

    public void updateUnit(Unit u){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE " + ACTIVE_UNITS_TABLE + " SET isNPC = " + u.isNPC() + ", name = '" + u.getName() +
                "', maxHealth = " + u.getMaxHealth() + ", currentHealth = " + u.getCurHealth() + ", initiative = " + u.getInitiative() +
                " WHERE unitID = " + u.getUnitID() + ";");

        db.close();
    }

    public boolean deleteUnit(Unit u){
        int rowsBefore = rowsInUnitsTable();

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ACTIVE_UNITS_TABLE + " WHERE unitID = " + u.getUnitID() + ";");
        db.close();

        int rowsAfter = rowsInUnitsTable();

        if (rowsBefore > rowsAfter){
            return true;
        }
        return false;
    }

    //region Private Functions
    private void deleteUnitsFrom(int gameID){
        String deleteQuery = "DELETE FROM " + ACTIVE_UNITS_TABLE + " WHERE gameID = " + gameID + ";";


        int rowsBefore = rowsInUnitsTable();

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteQuery);
        db.close();

        int rowsAfter = rowsInUnitsTable();

        if (rowsBefore > rowsAfter){
            Log.d("db success", (rowsBefore - rowsAfter) + " Units Deleted");
        }
    }

    private void swapTurnState(int cur, int next){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE " + ACTIVE_UNITS_TABLE + " SET myTurn = " + 0 + " WHERE unitID = " + cur + ";");
        db.execSQL("UPDATE " + ACTIVE_UNITS_TABLE + " SET myTurn = " + 1 + " WHERE unitID = " + next + ";");

        db.close();
    }

    @SuppressLint("Range")
    private void setUnitTurnIfNoneExists(int gameID){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT myTurn, unitID FROM " + ACTIVE_UNITS_TABLE + " WHERE gameID = " + gameID + " ORDER BY initiative DESC;";

        Cursor cursor = db.rawQuery(selectQuery, null);


        boolean gameUnitHasMyTurn = false;
        ArrayList<Integer> unitIDs = new ArrayList<>();

        if (cursor.moveToFirst()){
            do  {
                // its the units turn
                // and no other unit in query says its their turn
                if (cursor.getInt(cursor.getColumnIndex("myTurn")) == 1 && gameUnitHasMyTurn == false) {
                    gameUnitHasMyTurn = true;
                } else if (cursor.getInt(cursor.getColumnIndex("myTurn")) == 1){
                    Log.d("db error check", "another has myTurn = 1");
                    unitIDs.add(cursor.getInt(cursor.getColumnIndex("unitID")));
                }
            }while (cursor.moveToNext());

            // set first unit myTurn to true if non have myTurn = true
            if (gameUnitHasMyTurn == false){
                cursor.moveToFirst();
                int firstUnitID = cursor.getInt(cursor.getColumnIndex("unitID"));

                db.close();
                db = this.getWritableDatabase();
                db.execSQL("UPDATE " + ACTIVE_UNITS_TABLE + " SET myTurn = 1 WHERE unitID = " + firstUnitID + ";");
                //close the db outside of this statement
            }
        }

        db.close();

        if (unitIDs.size() > 0){
            setMyTurnFalse(unitIDs);
        }
    }

    private void setMyTurnFalse(ArrayList<Integer> unitWrongTurn){
        SQLiteDatabase db = this.getWritableDatabase();
        String updateQuery;

        for (int i = 0; i < unitWrongTurn.size(); i++) {
            updateQuery = "UPDATE " + ACTIVE_UNITS_TABLE + " SET myTurn = 0 WHERE unitID = " + unitWrongTurn.get(i) + ";";

            db.execSQL(updateQuery);
            Log.d("db error check", "unit id " + unitWrongTurn.get(i) + " myTurn set to false. More than 1 myTurn was true");
        }

        db.close();
    }

    //endregion
    //endregion
    //=================
}
