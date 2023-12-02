package com.example.ttrpgmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class PlayGame extends AppCompatActivity {
    DatabaseHelper dbHelper;
    Button btn_j_unitClicked;
    Button btn_j_back;
    Button btn_j_addUnit;
    Button btn_j_advanceTurn;
    Game game;
    ListView lv_j_units;
    UnitListAdapter unitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        btn_j_unitClicked = findViewById(R.id.btn_play_unitClicked);
        btn_j_back = findViewById(R.id.btn_play_back);
        btn_j_addUnit = findViewById(R.id.btn_play_addUnit);
        btn_j_advanceTurn = findViewById(R.id.btn_play_advanceTurn);
        lv_j_units = findViewById(R.id.lv_play_units);

        dbHelper = new DatabaseHelper(this);
        dbHelper.initializeTables();

        buildCurrentGame();

        buttonEventHandler();
        fillUnitListView();

        //debugAllGameInfo();
    }

    private void buttonEventHandler(){
        btn_j_unitClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to edit the unit
                // Pass the 'User', 'Game', and 'unit'

                Intent editUnit = new Intent(PlayGame.this, EditUnit.class);
                editUnit.putExtra("Game", game);
                startActivity(editUnit);
            }
        });

        btn_j_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gamesPage = new Intent(PlayGame.this, GamesPage.class);
                gamesPage.putExtra("Game", game);
                startActivity(gamesPage);
            }
        });

        btn_j_addUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addUnit = new Intent(PlayGame.this, AddUnit.class);
                addUnit.putExtra("Game", game);
                startActivity(addUnit);
            }
        });

        btn_j_advanceTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("btn", "Advances turn in the list view");
            }
        });
    }

    // Rebuilds the entire 'game' object from the database.
    // If you need to visually update anything
    private void buildCurrentGame(){
        Intent cameFrom = getIntent();

        //It's fine that we store this in curGame
        game = (Game) cameFrom.getSerializableExtra("Game");
        game = dbHelper.buildGame(game);
    }

    public void fillUnitListView()
    {
        unitAdapter = new UnitListAdapter(this, game.getUnits());
        lv_j_units.setAdapter(unitAdapter);
    }

    private void debugAllGameInfo(){
        Log.d("=====================", "========================");
        Log.d("GameID", "Info");
        Log.d("" + game.getGameID(), "" + game.getGameName());
        Log.d("" + game.getGameID(), "" + game.getDMUsername());
        Log.d("=====================", "=========================");
        Log.d("unitID", "Info");
        for (int i = 0; i < game.getUnits().size(); i++){
            Log.d("" + game.getUnits().get(i).getUnitID(), "" + game.getUnits().get(i).getGameID());
            Log.d("" + game.getUnits().get(i).getUnitID(), "" + game.getUnits().get(i).getName());
            Log.d("" + game.getUnits().get(i).getUnitID(), "npc " + game.getUnits().get(i).isNPC());
            Log.d("" + game.getUnits().get(i).getUnitID(), "" + game.getUnits().get(i).getCurHealth() + " cur hp");
            Log.d("" + game.getUnits().get(i).getUnitID(), "" + game.getUnits().get(i).getMaxHealth() + " max hp");
            Log.d("" + game.getUnits().get(i).getUnitID(), "init. " + game.getUnits().get(i).getInitiative());
            Log.d("=====================", "=====================");
        }
    }
}