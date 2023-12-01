package com.example.ttrpgmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class GamesPage extends AppCompatActivity {
    DatabaseHelper db;
    ArrayList<Game> usersGames = new ArrayList<>();
    Button btn_j_newGame;
    Button btn_j_logout;
    User currentUser;
    ListView lv_j_games;
    GameListAdapter adapter;
    Intent playGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_page);

        btn_j_newGame = findViewById(R.id.btn_games_newGame);
        btn_j_logout = findViewById(R.id.btn_games_logout);
        lv_j_games = findViewById(R.id.lv_games);

        currentUser = getCurrentUser();

        db = new DatabaseHelper(this);
        db.initializeTables();
        usersGames = db.getUsersGames();

        playGame = new Intent(GamesPage.this, PlayGame.class);

        buttonEventHandler();
        fillGamesListView();
        currentGameUnits();
    }

    // For now this button just brings us to the PlayGame Intent
    private void buttonEventHandler(){
        btn_j_newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //=== We still need to ===
                // Add a new game to the database
                // Pass that that game or its primary key to the PlayGame Intent
                // Pass the user to the new intent to pass it back later or pass the user back using
                // the DMUsername from 'Game' object


                //playGame = new Intent(GamesPage.this, PlayGame.class);
                playGame.putExtra("User", currentUser);

                startActivity(playGame);
            }
        });

        btn_j_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(GamesPage.this, MainActivity.class);
                // We don't have to put extras I think
                startActivity(mainActivity);
            }
        });
    }

    private User getCurrentUser(){
        Intent cameFrom = getIntent();

        User curUser;
        curUser = (User) cameFrom.getSerializableExtra("User");

        if (curUser == null){
            Log.d("Error", "No current user received");
            return null;
        }
        return curUser;
    }

    public void fillGamesListView()
    {
        adapter = new GameListAdapter(this, usersGames);
        lv_j_games.setAdapter(adapter);
    }

    public void currentGameUnits()
    {
        lv_j_games.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(playGame);
            }
        });
    }
}