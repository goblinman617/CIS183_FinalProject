package com.example.ttrpgmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class GamesPage extends AppCompatActivity {
    DatabaseHelper db;
    ArrayList<String> usersGames = new ArrayList<>();
    Button btn_j_newGame;
    Button btn_j_logout;
    User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_page);

        btn_j_newGame = findViewById(R.id.btn_games_newGame);
        btn_j_logout = findViewById(R.id.btn_games_logout);

        currentUser = getCurrentUser();

        buttonEventHandler();
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


                Intent playGame;
                playGame = new Intent(GamesPage.this, PlayGame.class);
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
}