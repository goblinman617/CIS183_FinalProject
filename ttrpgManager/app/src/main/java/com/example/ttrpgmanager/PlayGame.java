package com.example.ttrpgmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class PlayGame extends AppCompatActivity {
    DatabaseHelper db;
    Button btn_j_unitClicked; //This button is standing in for a working list view
    Button btn_j_back;
    Button btn_j_addUnit;
    Button btn_j_advanceTurn;
    User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        btn_j_unitClicked = findViewById(R.id.btn_play_unitClicked);
        btn_j_back = findViewById(R.id.btn_play_back);
        btn_j_addUnit = findViewById(R.id.btn_play_addUnit);
        btn_j_advanceTurn = findViewById(R.id.btn_play_advanceTurn);

        curUser = getCurrentUser();

        buttonEventHandler();
    }

    private void buttonEventHandler(){
        btn_j_unitClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to edit the unit
                // Pass the 'User', 'Game', and 'unit'

                Intent editUnit = new Intent(PlayGame.this, EditUnit.class);
                editUnit.putExtra("User", curUser);
                startActivity(editUnit);
            }
        });

        btn_j_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gamesPage = new Intent(PlayGame.this, GamesPage.class);
                gamesPage.putExtra("User", curUser);
                startActivity(gamesPage);
            }
        });

        btn_j_addUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addUnit = new Intent(PlayGame.this, AddUnit.class);
                addUnit.putExtra("User", curUser);
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