package com.example.ttrpgmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    public DatabaseHelper(Context context) {
        // change version to recreate the database
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // A foreign key should make it so that that field has to match a primary key in a different table

        // AUTOINCREMENT is a SQLite keyword that should handle the creation of the primary key and make sure it doesn't repeat
        // Documentation: https://www.sqlite.org/autoinc.html

        db.execSQL("CREATE TABLE " + USERS_TABLE + " (username TEXT PRIMARY KEY NOT NULL, password TEXT);");

        // u1 (unit1) will contain a UnitID or it will be null (no unit)
        // currently the Games table is setup to hold 12 units (a mix of players and NPCS)
        db.execSQL("CREATE TABLE " + GAMES_TABLE + " (gameID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " DMUsername TEXT NOT NULL, u0 TEXT, u1 TEXT, u2 TEXT, u3 TEXT, u4 TEXT, u5 TEXT," +
                " u6 TEXT, u7 TEXT, u8 TEXT, u9 TEXT, u10 TEXT, u11 TEXT, FOREIGN KEY (DMUsername)" +
                " REFERENCES " + USERS_TABLE + "(Username));");

        //is probably important to differentiate NPCS and Characters so we will include an isNpc BIT (boolean)
        // In figma it doesn't look like we need to store temporary health separately so it was
        // removed and the database version was changed.
        db.execSQL("CREATE TABLE " + ACTIVE_UNITS_TABLE + " (unitID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " gameID INT NOT NULL, isNpc BIT, name TEXT, maxHealth INT, currentHealth INT," +
                " initiative INT, FOREIGN KEY (gameID) REFERENCES " + GAMES_TABLE + "(gameID));");

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
            db.execSQL("INSERT INTO " + USERS_TABLE + " VALUES('DMuser', 'password');");

            db.close();

            initTables = true;
        }
        if(rowsInGamesTable() == 0)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("INSERT INTO " + GAMES_TABLE + " (DMUsername, u0, u1) VALUES('DMuser'," +
                    " 'DummyCharacter', 'DummyNPC');");

            db.close();

            initTables = true;
        }

        return initTables;
    }

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
        //table variables

        User tempUser = new User();

        if(cursor.moveToFirst())
        {
            do
            {
                tempUser.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                tempUser.setPassword(cursor.getString(cursor.getColumnIndex("password")));

                listOfUsers.add(tempUser);
            }
            while(cursor.moveToNext());
        }
        db.close();

        return listOfUsers;
    }

    //endregion

    //region GamesTable
    public void createNewGame(){

    }

    public ArrayList<Game> getUsersGames(String username){
        SQLiteDatabase db = getReadableDatabase();

        return null;
    }

    public int rowsInGamesTable(){
        SQLiteDatabase db = this.getReadableDatabase();

        int numRows = (int) DatabaseUtils.queryNumEntries(db, GAMES_TABLE);

        db.close();

        return numRows;
    }
    //endregion

}
