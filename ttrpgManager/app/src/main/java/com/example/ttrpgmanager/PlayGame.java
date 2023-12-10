package com.example.ttrpgmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class PlayGame extends AppCompatActivity {
    DatabaseHelper dbHelper;
    Button btn_j_unitClicked;
    Button btn_j_back;
    Button btn_j_addUnit;
    Button btn_j_advanceTurn;
    Button btn_j_updateGame;
    Game game;
    User user;
    ListView lv_j_units;
    UnitListAdapter unitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        btn_j_unitClicked = findViewById(R.id.btn_play_back);
        btn_j_back = findViewById(R.id.btn_play_back);
        btn_j_addUnit = findViewById(R.id.btn_play_addUnit);
        btn_j_updateGame = findViewById(R.id.btn_play_update);
        btn_j_advanceTurn = findViewById(R.id.btn_play_advanceTurn);
        lv_j_units = findViewById(R.id.lv_play_units);

        dbHelper = new DatabaseHelper(this);
        dbHelper.initializeTables();

        buildCurrentGame();

        buttonEventHandler();
        fillUnitListView();

        debugAllGameInfo();
    }

    private void buttonEventHandler(){
        //go to edit unit
        lv_j_units.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent editUnit = new Intent(PlayGame.this, EditUnit.class);
                //need to get game by unit position
                editUnit.putExtra("Unit", game.getUnits().get(i));
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
                // go to create a new unit
                Intent addUnit = new Intent(PlayGame.this, AddUnit.class);
                addUnit.putExtra("Game", game);
                startActivity(addUnit);
            }
        });

        btn_j_advanceTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.advanceTurn(game);
                buildCurrentGame();
                debugAllGameInfo();
            }
        });

        btn_j_updateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameFrom = getIntent();
                user = (User) cameFrom.getSerializableExtra("User");

                Intent updateGame = new Intent(PlayGame.this, UpdateGame.class);
                updateGame.putExtra("Game", game);
                updateGame.putExtra("User", user);

                startActivity(updateGame);
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
        for (int i = 0; i < game.getUnits().size(); i++) {
            Log.d("" + game.getUnits().get(i).getUnitID(), "" + game.getUnits().get(i).getName());
            Log.d("" + game.getUnits().get(i).getUnitID(), "npc " + game.getUnits().get(i).isNPC());
            Log.d("" + game.getUnits().get(i).getUnitID(), "" + game.getUnits().get(i).getCurHealth() + " cur hp");
            Log.d("" + game.getUnits().get(i).getUnitID(), "" + game.getUnits().get(i).getMaxHealth() + " max hp");
            Log.d("" + game.getUnits().get(i).getUnitID(), "init. " + game.getUnits().get(i).getInitiative());
            Log.d("" + game.getUnits().get(i).getUnitID(), "myTurn " + game.getUnits().get(i).isMyTurn());
            Log.d("=====================", "=====================");
        }
    }
}